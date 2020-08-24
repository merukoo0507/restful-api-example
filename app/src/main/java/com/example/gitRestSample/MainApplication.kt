package com.example.gitRestSample

import android.app.Application
import com.example.gitRestSample.util.FlipperHelper
//import com.facebook.stetho.Stetho
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        Timber.plant(Timber.DebugTree())
//        Stetho.initializeWithDefaults(this)
        FlipperHelper.initialize(this)
    }
}