package com.example.graphqlsample.api.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo.api.Input
import com.example.graphqlsample.api.apollo.ApolloClientManager
import com.example.graphqlsample.core.apollo.suspendQuery
import com.example.graphqlsample.queries.UserRepositoryListQuery
import timber.log.Timber

class RepositoryPagingSource(
    private val userLogin: String,
) : PagingSource<String, UserRepositoryListQuery.Edge>() {

    override fun getRefreshKey(state: PagingState<String, UserRepositoryListQuery.Edge>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, UserRepositoryListQuery.Edge> {
        try {
            val userRepositoryList: UserRepositoryListQuery.Data =
                ApolloClientManager.apolloClient
                    .suspendQuery(
                        UserRepositoryListQuery(
                            userLogin = userLogin,
                            first = params.loadSize,
                            after = Input.fromNullable(params.key),
                        )
                    )
                    .data!!

            return LoadResult.Page(
                data = userRepositoryList.user!!.repositories.edges!! as List<UserRepositoryListQuery.Edge>,
                prevKey = null,
                nextKey = userRepositoryList.user.repositories.edges!!.lastOrNull()?.cursor
            )
        } catch (e: Exception) {
            Timber.w(e, "Could not fetch user repository list")
            return LoadResult.Error(e)
        }
    }
}
