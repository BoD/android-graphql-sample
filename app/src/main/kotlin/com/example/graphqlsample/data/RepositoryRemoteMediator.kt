package com.example.graphqlsample.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room3.withWriteTransaction
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloGraphQLException
import com.example.graphqlsample.data.local.AppDatabase
import com.example.graphqlsample.data.local.RepositoryEntity
import com.example.graphqlsample.graphql.UserRepositoryListQuery

// See https://developer.android.com/topic/libraries/architecture/paging/v3-network-db
@OptIn(ExperimentalPagingApi::class)
class RepositoryRemoteMediator(
  private val userLogin: String,
  private val apolloClient: ApolloClient,
  private val database: AppDatabase,
) : RemoteMediator<Int, RepositoryEntity>() {
  private val repositoryDao = database.repositoryDao()

  override suspend fun initialize(): InitializeAction {
    // The very first time (database empty), we want REFRESH.
    // Subsequent times (database not empty) we want APPEND.
    if (repositoryDao.count() == 0) {
      return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
    return InitializeAction.SKIP_INITIAL_REFRESH
  }

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, RepositoryEntity>,
  ): MediatorResult {
    val loadKey = when (loadType) {
      LoadType.REFRESH -> null

      LoadType.PREPEND ->
        // Prepend is not supported
        return MediatorResult.Success(endOfPaginationReached = true)

      LoadType.APPEND -> {
        state.lastItemOrNull()?.cursor
      }
    }
    val response = apolloClient.query(
      UserRepositoryListQuery(
        userLogin = userLogin,
        first = state.config.pageSize,
        after = loadKey,
      ),
    )
      .execute()

    if (response.hasErrors() || response.data == null) {
      return MediatorResult.Error(if (response.exception != null) response.exception!! else ApolloGraphQLException(response.errors!!.first()))
    }
    database.withWriteTransaction {
      if (loadType == LoadType.REFRESH) {
        repositoryDao.clearAll()
      }
      repositoryDao.insertAll(
        response.data!!.user!!.repositories.edges!!.map { edge ->
          val fields = edge!!.node!!.repositoryFields
          RepositoryEntity(
            id = fields.id,
            name = fields.name,
            description = fields.description ?: "",
            stars = fields.stargazers.totalCount,
            createdAt = fields.createdAt.toString(),
            cursor = edge.cursor,
          )
        },
      )
    }

    return MediatorResult.Success(
      endOfPaginationReached = !response.data!!.user!!.repositories.pageInfo.hasNextPage,
    )
  }
}
