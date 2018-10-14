package com.jshvarts.lexiesample

import android.app.Application
import com.jshvarts.lexie.Lexie
import timber.log.Timber

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Lexie.enableLogging()
    }
}