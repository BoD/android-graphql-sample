package com.example.graphqlsample.api.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.graphqlsample.queries.UserRepositoryListQuery

class RepositoryDataSourceFactory(
    private val userLogin: String
) : DataSource.Factory<String, UserRepositoryListQuery.Edge>() {
    val loadingInitial = MutableLiveData<Boolean>()
    val isError = MutableLiveData<Boolean>()

    override fun create(): DataSource<String, UserRepositoryListQuery.Edge> {
        return RepositoryDataSource(userLogin, loadingInitial, isError)
    }
}
