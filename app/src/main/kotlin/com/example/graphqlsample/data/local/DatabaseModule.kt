package com.example.graphqlsample.data.local

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create

private fun provideAppDatabase(applicationContext: Context): AppDatabase {
  return Room.databaseBuilder<AppDatabase>(applicationContext, "database")
    .setDriver(AndroidSQLiteDriver())
    .build()
}

val databaseModule = module {
  single { create(::provideAppDatabase) }
}
