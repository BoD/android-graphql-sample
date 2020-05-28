package com.example.graphqlsample.ui.repository.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.graphqlsample.api.apollo.ApolloClientManager.apolloClient
import com.example.graphqlsample.core.apollo.suspendQuery
import com.example.graphqlsample.queries.SearchQuery
import com.example.graphqlsample.ui.repository.adapter.search.OwnerType
import com.example.graphqlsample.ui.repository.adapter.search.SearchRepositoryUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

class RepositorySearchViewModel(application: Application) : AndroidViewModel(application) {
    val loading = MutableLiveData(true)
    val isError = MutableLiveData<Boolean>()
    val repositoryList = MutableLiveData<List<SearchRepositoryUiModel>>()

    init {
        viewModelScope.launch {
            try {
                val searchResults = apolloClient
                    .suspendQuery(SearchQuery())
                    .data!!

                repositoryList.value = searchResults.search.edges!!.map { edge ->
                    val searchResultRepositoryFields =
                        edge!!.node!!.fragments.searchResultRepositoryFields!!
                    val owner = searchResultRepositoryFields.owner
                    val ownerType =
                        if (owner.fragments.searchResultOrganizationFields != null) OwnerType.ORGANIZATION else OwnerType.USER
                    SearchRepositoryUiModel(
                        name = searchResultRepositoryFields.name,
                        ownerType = ownerType,
                        ownerName = when (ownerType) {
                            OwnerType.USER -> owner.fragments.searchResultUserFields!!.name
                                ?: "(no name)"
                            OwnerType.ORGANIZATION -> owner.fragments.searchResultOrganizationFields!!.name!!
                        },
                        ownerUserBio = when (ownerType) {
                            OwnerType.USER -> owner.fragments.searchResultUserFields!!.bio?.trim().takeIf { !it.isNullOrBlank() }
                            OwnerType.ORGANIZATION -> null
                        }
                    )
                }
                isError.value = false
            } catch (e: Exception) {
                Timber.w(e, "Could not fetch search results")
                isError.value = true
            } finally {
                loading.value = false
            }
        }


    }
}
