package com.example.veterinaria.network

import android.util.Log
import com.example.veterinaria.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        val token = tokenProvider()
        
        val request = if (token != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }
        
        val response = chain.proceed(request)
        
        // Log para debugging (solo en debug builds)
        if (BuildConfig.LOGGING_ENABLED) {
            Log.d("API_REQUEST", "${request.method} ${request.url}")
            Log.d("API_RESPONSE", "Code: ${response.code}")
        }
        
        return response
    }
}