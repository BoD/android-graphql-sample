package com.example.graphqlsample.ui.repository.adapter.simple

sealed class RepositoryUiModel

data class SimpleRepositoryUiModel(
    val name: String,
    val description: String,
    val stars: String
) : RepositoryUiModel()

object SeeMoreRepositoryUiModel : RepositoryUiModel()
