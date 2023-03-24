package com.example.graphqlsample.ui.repository.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.compose.paging.Pager
import com.example.graphqlsample.R
import com.example.graphqlsample.queries.UserRepositoryListQuery
import com.example.graphqlsample.ui.navigation.NavigationArguments
import com.example.graphqlsample.ui.repository.item.SimpleRepositoryItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class RepositoryListViewModel @Inject constructor(
    application: Application,
    private val apolloClient: ApolloClient,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    private val userLogin = savedStateHandle.get<String>(NavigationArguments.USER_LOGIN)!!

    val pagingDataflow: Flow<PagingData<SimpleRepositoryItemUiModel>> =
        createPager()
            .flow
            .map { data ->
                data.map { item ->
                    SimpleRepositoryItemUiModel(
                        name = item.repositoryFields.name,
                        description = item.repositoryFields.description
                            ?: getApplication<Application>().getString(R.string.repository_noDescription),
                        stars = item.repositoryFields.stargazers.totalCount.toString()
                    )
                }
            }
            .cachedIn(viewModelScope)

    @OptIn(ApolloExperimental::class)
    private fun createPager(): Pager<ApolloCall<UserRepositoryListQuery.Data>, UserRepositoryListQuery.Node> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            appendCall = { response, loadSize ->
                val edges = response?.data?.user?.repositories?.edges
                if (edges != null && edges.isEmpty()) {
                    // Reached the end of the list
                    return@Pager null
                }
                apolloClient
                    .query(
                        UserRepositoryListQuery(
                            userLogin = userLogin,
                            first = loadSize,
                            after = edges?.lastOrNull()?.cursor,
                        )
                    )
            },
            itemsAfter = { response, loadedItemsCount ->
                response.data!!.user.repositories.totalCount - loadedItemsCount
            },
            getItems = { response ->
                if (response.hasErrors()) {
                    Result.failure(Exception("Could not fetch page of repositories: ${response.errors!!.joinToString { it.message }}"))
                } else {
                    Result.success(response.data!!.user.repositories.edges.map { edge -> edge!!.node })
                }
            },
        )
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
