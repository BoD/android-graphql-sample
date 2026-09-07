package com.example.graphqlsample.ui.repository.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.apollographql.apollo.ApolloClient
import com.example.graphqlsample.R
import com.example.graphqlsample.data.RepositoryRemoteMediator
import com.example.graphqlsample.data.local.AppDatabase
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
  private val appDatabase: AppDatabase,
  @Assisted val destination: Destination.RepositoryList,
) : ViewModel() {

  @OptIn(ExperimentalPagingApi::class)
  val pagingDataflow: Flow<PagingData<SimpleRepositoryItemUiModel>> =
    Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      remoteMediator = RepositoryRemoteMediator(
        userLogin = destination.userLogin,
        apolloClient = apolloClient,
        database = appDatabase,
      ),
      pagingSourceFactory = {
        appDatabase.repositoryDao().pagingSource()
      },
    )
      .flow
      .map { data ->
        data.map { item ->
          SimpleRepositoryItemUiModel(
            id = item.id,
            name = item.name,
            description = item.description
              ?: application.getString(R.string.repository_noDescription),
            stars = item.stars.toString(),
          )
        }
      }
      .cachedIn(viewModelScope)

  @AssistedFactory
  interface Factory {
    fun create(destination: Destination.RepositoryList): RepositoryListViewModel
  }

  companion object {
    private const val PAGE_SIZE = 15
  }
}
