package com.example.graphqlsample.ui.repository.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.apollographql.apollo3.ApolloClient
import com.example.graphqlsample.R
import com.example.graphqlsample.api.repository.RepositoryPagingSource
import com.example.graphqlsample.ui.navigation.NavigationArguments
import com.example.graphqlsample.ui.repository.item.SimpleRepositoryItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class RepositoryListViewModel @Inject constructor(
    application: Application,
    apolloClient: ApolloClient,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    val pagingDataflow: Flow<PagingData<SimpleRepositoryItemUiModel>> =
        Pager(PagingConfig(pageSize = PAGE_SIZE)) {
            RepositoryPagingSource(
                userLogin = savedStateHandle.get<String>(NavigationArguments.USER_LOGIN)!!,
                apolloClient = apolloClient
            )
        }.flow
            .map { data ->
                data.map { item ->
                    SimpleRepositoryItemUiModel(
                        name = item.repositoryFields.name,
                        description = item.repositoryFields.description
                            ?: getApplication<Application>().getString(R.string.repository_noDescription),
                        stars = item.repositoryFields.stargazers.totalCount.toString()
                    )
                }
            }
            .cachedIn(viewModelScope)

    companion object {
        private const val PAGE_SIZE = 10
    }
}
