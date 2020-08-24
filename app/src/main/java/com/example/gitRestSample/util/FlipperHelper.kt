package com.example.gitRestSample.util

import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.core.FlipperClient
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.leakcanary.LeakCanaryFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader

object FlipperHelper{
    val networkPlugin = NetworkFlipperPlugin()

    fun initialize(cxt: Context) {
        SoLoader.init(cxt, false)
        //layout inspector
        val client: FlipperClient = AndroidFlipperClient.getInstance(cxt)
        client.addPlugin(InspectorFlipperPlugin(cxt, DescriptorMapping.withDefaults()))
        client.addPlugin(networkPlugin)
//        client.addPlugin(DatabasesFlipperPlugin(cxt))
//        client.addPlugin(
//            SharedPreferencesFlipperPlugin(cxt, "flipper")
//        )
        client.addPlugin(LeakCanaryFlipperPlugin())
        client.addPlugin(CrashReporterPlugin.getInstance())
        client.start()
    }
}