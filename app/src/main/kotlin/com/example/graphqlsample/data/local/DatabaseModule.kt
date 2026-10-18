package com.example.graphqlsample.data.local

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
  @Provides
  @Singleton
  fun provideAppDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
    return Room.databaseBuilder<AppDatabase>(applicationContext, "database")
      .setDriver(AndroidSQLiteDriver())
      .build()
  }
}
