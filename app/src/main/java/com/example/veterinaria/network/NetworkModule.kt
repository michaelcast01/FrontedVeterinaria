package com.example.veterinaria.network

import android.content.Context
import com.example.veterinaria.BuildConfig
import com.example.veterinaria.network.api.*
import com.example.veterinaria.util.Constants
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object NetworkModule {

    // Use a development-friendly default that works with the Android emulator (10.0.2.2)
    private val BASE_URL: String = when {
        BuildConfig.DEBUG -> Constants.Network.BASE_URL_DEV
        else -> Constants.Network.BASE_URL_PROD
    }

    // Cache settings (optional — initialize via init(context) to enable)
    private const val HTTP_CACHE_SIZE: Long = 10L * 1024L * 1024L // 10 MB
    private var cacheDir: File? = null

    // Provider to check network availability. Can be set from Application via setNetworkAvailabilityProvider
    private var isNetworkAvailableProvider: () -> Boolean = { true }

    fun init(context: Context) {
        // Call this from Application.onCreate() if you want to enable caching
        cacheDir = File(context.cacheDir, "http_cache")
    }

    fun setNetworkAvailabilityProvider(provider: () -> Boolean) {
        isNetworkAvailableProvider = provider
    }
    
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create()
    
    private fun createOkHttpClient(tokenProvider: () -> String?): OkHttpClient {
        val timeout = Constants.Network.TIMEOUT_SECONDS

        val builder = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(tokenProvider))
            .retryOnConnectionFailure(true)

        // Cache control network interceptor: set short max-age for fresh responses
        val networkCacheInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(60, TimeUnit.SECONDS) // 1 minute
                .build()
            response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
        }

        // Offline cache interceptor: serve stale responses when offline
        val offlineCacheInterceptor = Interceptor { chain ->
            var request = chain.request()
            if (!isNetworkAvailableProvider()) {
                val cacheControl = CacheControl.Builder()
                    .onlyIfCached()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }

        // Solo agregar logging en builds de debug
        if (BuildConfig.LOGGING_ENABLED) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }

        // Add cache interceptors if cacheDir was initialized
        cacheDir?.let { dir ->
            try {
                val cache = Cache(dir, HTTP_CACHE_SIZE)
                builder.cache(cache)
                builder.addNetworkInterceptor(networkCacheInterceptor)
                builder.addInterceptor(offlineCacheInterceptor)
            } catch (_: Exception) {
                // Ignore cache errors — app continues without caching
            }
        }

        return builder.build()
    }
    
    private fun createRetrofit(tokenProvider: () -> String?): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient(tokenProvider))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    fun provideAuthApiService(): AuthApiService {
        return createRetrofit { null }.create(AuthApiService::class.java)
    }
    
    fun provideClienteApiService(tokenProvider: () -> String?): ClienteApiService {
        return createRetrofit(tokenProvider).create(ClienteApiService::class.java)
    }
    
    fun provideMascotaApiService(tokenProvider: () -> String?): MascotaApiService {
        return createRetrofit(tokenProvider).create(MascotaApiService::class.java)
    }
    
    fun provideCitaApiService(tokenProvider: () -> String?): CitaApiService {
        return createRetrofit(tokenProvider).create(CitaApiService::class.java)
    }
}