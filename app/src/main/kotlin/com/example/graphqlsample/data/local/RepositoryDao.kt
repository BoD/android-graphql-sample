package com.example.graphqlsample.data.local

import androidx.paging.PagingSource
import androidx.room3.Dao
import androidx.room3.DaoReturnTypeConverters
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.paging.PagingSourceDaoReturnTypeConverter

@Dao
@DaoReturnTypeConverters(PagingSourceDaoReturnTypeConverter::class)
interface RepositoryDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(repositories: List<RepositoryEntity>)

  @Query("SELECT * FROM repository ORDER BY createdAt DESC")
  fun pagingSource(): PagingSource<Int, RepositoryEntity>

  @Query("DELETE FROM repository")
  suspend fun clearAll()

  @Query("SELECT COUNT(*) FROM repository")
  suspend fun count(): Int
}
