package com.example.graphqlsample.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface Destination {
  @Serializable
  data object ViewerInfo : NavKey

  @Serializable
  data object Misc : NavKey

  @Serializable
  data object RepositorySearch : NavKey

  @Serializable
  data class RepositoryList(
    val userLogin: String,
  ) : NavKey
}
