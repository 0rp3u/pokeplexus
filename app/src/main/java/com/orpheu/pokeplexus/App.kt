package com.orpheu.pokeplexus

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.orpheu.pokeplexus.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(repositoryModule, viewModelModule, retrofitModule, apiModule, databaseModule)
            )
        }
    }
}