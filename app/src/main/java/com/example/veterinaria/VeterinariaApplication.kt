package com.example.veterinaria

import android.app.Application
import android.content.Context

class VeterinariaApplication : Application() {
    
    companion object {
        lateinit var instance: VeterinariaApplication
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    
    fun getAppContext(): Context = applicationContext
}