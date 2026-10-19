package com.castorama.krakathon

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KrakathonApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KrakathonApp)
            modules(appModules)
        }
    }
}
