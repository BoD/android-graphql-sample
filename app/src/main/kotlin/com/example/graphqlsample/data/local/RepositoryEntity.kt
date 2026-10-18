package com.example.graphqlsample.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "repository")
data class RepositoryEntity(
  @PrimaryKey val id: String,
  val name: String,
  val description: String,
  val stars: Int,
  val createdAt: String,
  val cursor: String,
)
