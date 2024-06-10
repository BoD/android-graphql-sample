package com.example.graphqlsample.ui.repository.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.example.graphqlsample.graphql.SearchQuery
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
            val apolloResponse = apolloClient
                .query(SearchQuery())
                .execute()

            val searchResults: SearchQuery.Data? = apolloResponse.data
            if (searchResults != null) {
                uiModel.value =
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
            } else if (apolloResponse.exception != null) {
                Timber.w(apolloResponse.exception, "Could not fetch search results")
                uiModel.value = RepositorySearchUiModel.Error
            }
        }
    }

    sealed interface RepositorySearchUiModel {
        data object Loading : RepositorySearchUiModel
        data object Error : RepositorySearchUiModel
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
