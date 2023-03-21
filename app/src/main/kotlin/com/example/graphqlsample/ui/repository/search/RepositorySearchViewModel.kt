package com.example.graphqlsample.ui.repository.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.example.graphqlsample.queries.SearchQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RepositorySearchViewModel @Inject constructor(
    application: Application,
    apolloClient: ApolloClient,
) : AndroidViewModel(application) {

    val uiModel: MutableStateFlow<RepositorySearchUiModel> = MutableStateFlow(RepositorySearchUiModel.Loading)

    init {
        viewModelScope.launch {
            val response = apolloClient
                .query(SearchQuery())
                .execute()
            uiModel.value = when {
                response.exception != null -> {
                    Timber.w(response.exception!!, "Could not fetch search results")
                    RepositorySearchUiModel.Error
                }

                response.hasErrors() -> {
                    Timber.w("Could not fetch search results: ${response.errors!!.joinToString { it.message }}")
                    RepositorySearchUiModel.Error
                }

                else -> {
                    val searchResults: SearchQuery.Data = response.data!!
                    RepositorySearchUiModel.Loaded(
                        searchResults.search.edges.map { edge ->
                            val searchResultRepositoryFields =
                                edge!!.node.searchResultRepositoryFields!!
                            val owner = searchResultRepositoryFields.owner
                            val ownerType = if (owner.searchResultOrganizationFields != null) {
                                RepositorySearchItemUiModel.OwnerType.ORGANIZATION
                            } else {
                                RepositorySearchItemUiModel.OwnerType.USER
                            }
                            RepositorySearchItemUiModel(
                                name = searchResultRepositoryFields.name,
                                ownerType = ownerType,
                                ownerName = when (ownerType) {
                                    RepositorySearchItemUiModel.OwnerType.USER -> owner.searchResultUserFields!!.name
                                        ?: "(no name)"

                                    RepositorySearchItemUiModel.OwnerType.ORGANIZATION -> owner.searchResultOrganizationFields!!.name!!
                                },
                                ownerUserBio = when (ownerType) {
                                    RepositorySearchItemUiModel.OwnerType.USER -> owner.searchResultUserFields!!.bio?.trim()
                                        .takeIf { !it.isNullOrBlank() }

                                    RepositorySearchItemUiModel.OwnerType.ORGANIZATION -> null
                                }
                            )
                        }
                    )
                }
            }
        }
    }

    sealed interface RepositorySearchUiModel {
        object Loading : RepositorySearchUiModel
        object Error : RepositorySearchUiModel
        data class Loaded(
            val repositorySearchItemList: List<RepositorySearchItemUiModel>,
        ) : RepositorySearchUiModel
    }

    data class RepositorySearchItemUiModel(
        val name: String,
        val ownerType: OwnerType,
        val ownerName: String,
        val ownerUserBio: String?,
    ) {
        enum class OwnerType {
            USER, ORGANIZATION
        }
    }
}
