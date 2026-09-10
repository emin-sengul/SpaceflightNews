package com.emin.spaceflightnews

import android.app.Application
import com.emin.spaceflightnews.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class SpaceflightNewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            enableNetworkLogging = BuildConfig.DEBUG,
        ) {
            androidLogger()

            androidContext(this@SpaceflightNewsApplication)
        }
    }
}
