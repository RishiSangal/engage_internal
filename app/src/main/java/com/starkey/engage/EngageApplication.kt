package com.starkey.engage

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import io.paperdb.Paper

@HiltAndroidApp
class EngageApplication : Application() {
    var isDebuggingEnabled: Boolean = false

// Testing Commit
    companion object {
        private var sInstance: EngageApplication? = null
        fun getInstance(): EngageApplication? {
            return sInstance;
        }

        private lateinit var appContext: Context

        @JvmStatic
        fun getGlobalAppContext(): Context {
            return appContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        Paper.init(this)
        appContext = applicationContext
        sInstance = this
        isDebuggingEnabled = BuildConfig.DEBUG
    }
}