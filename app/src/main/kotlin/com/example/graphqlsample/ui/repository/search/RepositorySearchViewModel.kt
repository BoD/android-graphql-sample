package com.example.graphqlsample.ui.repository.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graphqlsample.api.apollo.ApolloClientManager.apolloClient
import com.example.graphqlsample.queries.SearchQuery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class RepositorySearchViewModel(application: Application) : AndroidViewModel(application) {

    val uiModel: MutableStateFlow<RepositorySearchUiModel> = MutableStateFlow(RepositorySearchUiModel.Loading)

    init {
        viewModelScope.launch {
            try {
                val searchResults: SearchQuery.Data = apolloClient
                    .query(SearchQuery())
                    .execute()
                    .data!!

                uiModel.value = RepositorySearchUiModel.Loaded(searchResults.search.edges!!.map { edge ->
                    val searchResultRepositoryFields =
                        edge!!.node!!.searchResultRepositoryFields!!
                    val owner = searchResultRepositoryFields.owner
                    val ownerType =
                        if (owner.searchResultOrganizationFields != null) RepositorySearchItemUiModel.OwnerType.ORGANIZATION else RepositorySearchItemUiModel.OwnerType.USER
                    RepositorySearchItemUiModel(
                        name = searchResultRepositoryFields.name,
                        ownerType = ownerType,
                        ownerName = when (ownerType) {
                            RepositorySearchItemUiModel.OwnerType.USER -> owner.searchResultUserFields!!.name
                                ?: "(no name)"
                            RepositorySearchItemUiModel.OwnerType.ORGANIZATION -> owner.searchResultOrganizationFields!!.name!!
                        },
                        ownerUserBio = when (ownerType) {
                            RepositorySearchItemUiModel.OwnerType.USER -> owner.searchResultUserFields!!.bio?.trim().takeIf { !it.isNullOrBlank() }
                            RepositorySearchItemUiModel.OwnerType.ORGANIZATION -> null
                        }
                    )
                }
                )
            } catch (e: Exception) {
                Timber.w(e, "Could not fetch search results")
                uiModel.value = RepositorySearchUiModel.Error
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
