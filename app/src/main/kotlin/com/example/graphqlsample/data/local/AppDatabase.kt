package com.example.graphqlsample.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(entities = [RepositoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun repositoryDao(): RepositoryDao
}
