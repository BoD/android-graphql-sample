package com.example.graphqlsample.ui.repository.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.graphqlsample.R
import com.example.graphqlsample.api.repository.RepositoryPagingSource
import com.example.graphqlsample.ui.repository.item.SimpleRepositoryItemUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class RepositoryListViewModel(application: Application, userLogin: String) :
    AndroidViewModel(application) {

    val loading = MutableLiveData<Boolean>()

    val pagingDataflow: Flow<PagingData<SimpleRepositoryItemUiModel>> = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        RepositoryPagingSource(userLogin = userLogin)
    }.flow
        .mapLatest { data ->
            data.map { item ->
                SimpleRepositoryItemUiModel(
                    name = item.node!!.repositoryFields.name,
                    description = item.node.repositoryFields.description
                        ?: getApplication<Application>().getString(R.string.repository_noDescription),
                    stars = item.node.repositoryFields.stargazers.totalCount.toString()
                )
            }
        }
        .cachedIn(viewModelScope)

    companion object {
        private const val PAGE_SIZE = 10
    }
}
