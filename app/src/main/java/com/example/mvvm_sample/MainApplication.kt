package com.example.mvvm_sample

import android.app.Application
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        Timber.plant(Timber.DebugTree())
    }
}