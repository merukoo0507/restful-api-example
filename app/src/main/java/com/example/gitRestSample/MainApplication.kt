package com.example.gitRestSample

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        initFlipper()
    }

    private fun initFlipper() {
        SoLoader.init(this, false)
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val flipperClient = AndroidFlipperClient.getInstance(this)
            flipperClient.addPlugin(
                InspectorFlipperPlugin(
                    this,
                    DescriptorMapping.withDefaults()
                )
            )
            flipperClient.addPlugin(networkFlipperPlugin)
            flipperClient.start()
        }
    }

    companion object {
        val networkFlipperPlugin = NetworkFlipperPlugin()
    }
}