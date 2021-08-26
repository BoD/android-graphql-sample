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
    private var loadedItems = 0

    override fun getRefreshKey(state: PagingState<String, UserRepositoryListQuery.Edge>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, UserRepositoryListQuery.Edge> {
        if (params is LoadParams.Refresh) loadedItems = 0

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

            val data = userRepositoryList.user!!.repositories.edges!! as List<UserRepositoryListQuery.Edge>
            loadedItems += data.size
            return LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = data.lastOrNull()?.cursor,
                itemsAfter = userRepositoryList.user.repositories.totalCount - loadedItems,
            )
        } catch (e: Exception) {
            Timber.w(e, "Could not fetch user repository list")
            return LoadResult.Error(e)
        }
    }
}
