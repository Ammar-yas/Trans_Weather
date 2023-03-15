package com.example.transweather.core

import android.app.Application
import com.example.transweather.BuildConfig
import com.pluto.Pluto
import com.pluto.plugins.network.PlutoNetworkPlugin
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TransWeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        plantTimberTrees()
        initPluto()
    }

    private fun plantTimberTrees() {
        Timber.plant(FirebaseCrashReportingTree())
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    //Network logger works only in debug mode to debug apps easily
    private fun initPluto(){
        val plutoInstaller = Pluto.Installer(this)
        plutoInstaller.addPlugin(PlutoNetworkPlugin("Network"))
        plutoInstaller.install()
    }

}