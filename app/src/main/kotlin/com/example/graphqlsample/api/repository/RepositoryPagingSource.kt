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

        try {
            val userRepositoryList: UserRepositoryListQuery.Data =
                apolloClient
                    .query(
                        UserRepositoryListQuery(
                            userLogin = userLogin,
                            first = params.loadSize,
                            after = params.key,
                        )
                    )
                    .execute()
                    .dataAssertNoErrors

            val data = userRepositoryList.user.repositories.edges
            loadedItems += data.size
            return LoadResult.Page(
                data = data.map { it!!.node },
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
