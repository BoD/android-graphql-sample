package com.example.graphqlsample.api.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.apollographql.apollo.api.Input
import com.example.graphqlsample.api.apollo.ApolloClientManager
import com.example.graphqlsample.core.apollo.suspendQuery
import com.example.graphqlsample.queries.UserRepositoryListQuery
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class RepositoryDataSource(
    private val userLogin: String,
    private val loadingInitial: MutableLiveData<Boolean>,
    private val isError: MutableLiveData<Boolean>
) : ItemKeyedDataSource<String, UserRepositoryListQuery.Edge>() {
    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<UserRepositoryListQuery.Edge>
    ) {
        try {
            loadingInitial.postValue(true)
            val userRepositoryList = runBlocking {
                ApolloClientManager.apolloClient
                    .suspendQuery(
                        UserRepositoryListQuery(
                            userLogin = userLogin,
                            first = params.requestedLoadSize
                        )
                    )
                    .data!!
            }
            callback.onResult(
                userRepositoryList.user!!.repositories.edges!!,
                0,
                userRepositoryList.user.repositories.totalCount
            )
            isError.postValue(false)
        } catch (e: Exception) {
            Timber.w(e, "Could not fetch user repository list")
            isError.postValue(true)
        } finally {
            loadingInitial.postValue(false)
        }
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<UserRepositoryListQuery.Edge>
    ) {
        try {
            val userRepositoryList = runBlocking {
                ApolloClientManager.apolloClient
                    .suspendQuery(
                        UserRepositoryListQuery(
                            userLogin = userLogin,
                            first = params.requestedLoadSize,
                            after = Input.fromNullable(params.key)
                        )
                    )
                    .data!!
            }
            callback.onResult(userRepositoryList.user!!.repositories.edges!!)
            isError.postValue(false)
        } catch (e: Exception) {
            Timber.w(e, "Could not fetch user repository list")
            isError.postValue(true)
        }
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<UserRepositoryListQuery.Edge>
    ) {
        // Ignored, since we only ever append to our initial load
    }

    override fun getKey(item: UserRepositoryListQuery.Edge): String {
        return item.cursor
    }
}
