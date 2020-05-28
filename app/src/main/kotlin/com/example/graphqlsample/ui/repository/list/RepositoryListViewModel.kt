package com.example.graphqlsample.ui.repository.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.graphqlsample.R
import com.example.graphqlsample.api.repository.RepositoryDataSourceFactory
import com.example.graphqlsample.ui.repository.adapter.simple.SimpleRepositoryUiModel

class RepositoryListViewModel(application: Application, userLogin: String) :
    AndroidViewModel(application) {
    val loading: LiveData<Boolean>
    val isError: LiveData<Boolean>
    val repositoryList: LiveData<PagedList<SimpleRepositoryUiModel>>

    init {
        val repositoryDataSourceFactory = RepositoryDataSourceFactory(userLogin)
        loading = repositoryDataSourceFactory.loadingInitial
        isError = repositoryDataSourceFactory.isError
        repositoryList = repositoryDataSourceFactory.map { item ->
            SimpleRepositoryUiModel(
                name = item!!.node!!.fragments.repositoryFields.name,
                description = item.node!!.fragments.repositoryFields.description
                    ?: getApplication<Application>().getString(R.string.repository_noDescription),
                stars = item.node.fragments.repositoryFields.stargazers.totalCount.toString()
            )
        }
            .toLiveData(PAGE_SIZE)
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
