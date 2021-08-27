package com.example.graphqlsample.ui.repository.item

sealed interface RepositoryItemUiModel

data class SimpleRepositoryItemUiModel(
    val name: String,
    val description: String,
    val stars: String,
) : RepositoryItemUiModel

object SeeMoreRepositoryItemUiModel : RepositoryItemUiModel
