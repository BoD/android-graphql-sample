package com.example.graphqlsample.api.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo3.ApolloClient
import com.example.graphqlsample.queries.UserRepositoryListQuery
import timber.log.Timber

class RepositoryPagingSource(
    private val userLogin: String,
    private val apolloClient: ApolloClient,
) : PagingSource<String, UserRepositoryListQuery.Node>() {
    private var loadedItems = 0

    override fun getRefreshKey(state: PagingState<String, UserRepositoryListQuery.Node>): String? =
        null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, UserRepositoryListQuery.Node> {
        if (params is LoadParams.Refresh) loadedItems = 0
        val response = apolloClient
            .query(
                UserRepositoryListQuery(
                    userLogin = userLogin,
                    first = params.loadSize,
                    after = params.key,
                )
            )
            .execute()

        return when {
            response.exception != null -> {
                Timber.w(response.exception, "Could not fetch user repository list")
                LoadResult.Error(response.exception!!)
            }

            response.hasErrors() -> {
                val errorMessage = response.errors!!.joinToString { it.message }
                Timber.w("Could not fetch user repository list: $errorMessage")
                LoadResult.Error(Exception("Could not fetch user repository list: $errorMessage"))
            }

            else -> {
                val edges = response.data!!.user.repositories.edges
                loadedItems += edges.size
                LoadResult.Page(
                    data = edges.map { it!!.node },
                    prevKey = null,
                    nextKey = edges.lastOrNull()?.cursor,
                    itemsAfter = response.data!!.user.repositories.totalCount - loadedItems,
                )
            }
        }
    }
}
