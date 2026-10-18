package com.example.graphqlsample

import android.app.Application
import com.example.graphqlsample.data.local.databaseModule
import com.example.graphqlsample.data.remote.apollo.apolloModule
import com.example.graphqlsample.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import timber.log.Timber.DebugTree

class Application : Application() {
  override fun onCreate() {
    super.onCreate()
    Timber.plant(DebugTree())

    startKoin {
      androidLogger(Level.DEBUG)
      androidContext(this@Application)
      modules(databaseModule, apolloModule, uiModule)
    }
  }
}
