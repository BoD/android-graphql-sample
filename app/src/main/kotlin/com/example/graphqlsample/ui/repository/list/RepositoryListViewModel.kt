package com.example.graphqlsample.ui.repository.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.apollographql.apollo.ApolloClient
import com.example.graphqlsample.R
import com.example.graphqlsample.api.repository.RepositoryPagingSource
import com.example.graphqlsample.ui.navigation.Destination
import com.example.graphqlsample.ui.repository.item.SimpleRepositoryItemUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@HiltViewModel(assistedFactory = RepositoryListViewModel.Factory::class)
class RepositoryListViewModel @AssistedInject constructor(
  private val application: Application,
  apolloClient: ApolloClient,
  @Assisted val destination: Destination.RepositoryList,
) : ViewModel() {

  val pagingDataflow: Flow<PagingData<SimpleRepositoryItemUiModel>> =
    Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        RepositoryPagingSource(
          userLogin = destination.userLogin,
          apolloClient = apolloClient,
        )
      },
    )
      .flow
      .map { data ->
        data.map { item ->
          SimpleRepositoryItemUiModel(
            id = item.repositoryFields.id,
            name = item.repositoryFields.name,
            description = item.repositoryFields.description
              ?: application.getString(R.string.repository_noDescription),
            stars = item.repositoryFields.stargazers.totalCount.toString(),
          )
        }
      }
      .cachedIn(viewModelScope)

  @AssistedFactory
  interface Factory {
    fun create(destination: Destination.RepositoryList): RepositoryListViewModel
  }

  companion object {
    private const val PAGE_SIZE = 10
  }
}
