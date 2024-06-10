package com.example.graphqlsample.ui.repository.item

sealed interface RepositoryItemUiModel

data class SimpleRepositoryItemUiModel(
    val id: String,
    val name: String,
    val description: String,
    val stars: String,
) : RepositoryItemUiModel

data object SeeMoreRepositoryItemUiModel : RepositoryItemUiModel
