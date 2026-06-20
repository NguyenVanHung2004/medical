package com.example.medical

import android.app.Application
import com.example.medical.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MedicalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@MedicalApplication)
            modules(appModule)
        }
    }
}
