package com.example.graphqlsample.ui.repository.item

sealed interface RepositoryUiModel

data class SimpleRepositoryUiModel(
    val name: String,
    val description: String,
    val stars: String
) : RepositoryUiModel

object SeeMoreRepositoryUiModel : RepositoryUiModel
