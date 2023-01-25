package com.app.sample_login.init

import android.content.Context
import androidx.startup.Initializer
import com.app.sample_login.BuildConfig
import com.app.sample_login.reporting.TimberReleaseTree
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(TimberReleaseTree())
        }
        Timber.d("Initialization of Timber completed")
        return
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}