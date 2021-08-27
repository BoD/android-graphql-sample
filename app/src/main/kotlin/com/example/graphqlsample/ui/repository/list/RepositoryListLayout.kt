package com.example.graphqlsample.ui.repository.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.graphqlsample.R
import com.example.graphqlsample.core.ui.FullScreenLoading
import com.example.graphqlsample.ui.repository.item.RepositoryItem
import com.example.graphqlsample.ui.repository.item.SimpleRepositoryItemUiModel
import kotlinx.coroutines.flow.Flow

@Composable
fun RepositoryListLayout(repositoryList: Flow<PagingData<SimpleRepositoryItemUiModel>>) {
    MaterialTheme {
        val lazyRepositoryItems: LazyPagingItems<SimpleRepositoryItemUiModel> = repositoryList.collectAsLazyPagingItems()
        val state = lazyRepositoryItems.loadState
        val isError = state.refresh is LoadState.Error || state.append is LoadState.Error

        val scaffoldState = rememberScaffoldState()
        if (isError) {
            val message = stringResource(R.string.error_generic)
            LaunchedEffect(scaffoldState.snackbarHostState) {
                scaffoldState.snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Indefinite)
            }
        }
        Scaffold(scaffoldState = scaffoldState) {
            Crossfade(state.refresh is LoadState.Loading) { isLoading ->
                if (isLoading) {
                    FullScreenLoading()
                } else if (state.refresh is LoadState.NotLoading) {
                    Loaded(lazyRepositoryItems)
                }
            }
        }
    }
}

@Composable
private fun Loaded(lazyRepositoryItems: LazyPagingItems<SimpleRepositoryItemUiModel>) {
    LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
        items(lazyRepositoryItems) { repository ->
            when (repository) {
                is SimpleRepositoryItemUiModel -> RepositoryItem(repository)
                null -> PlaceholderRepositoryItem()
            }
        }
    }
}

@Composable
private fun PlaceholderRepositoryItem() {
    Column(
        Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.weight(1F)) {
                Shim(Modifier.size(128.dp, 19.dp))
            }
            Shim(Modifier.size(24.dp, 15.dp))
        }
        Spacer(Modifier.size(4.dp))
        Shim(Modifier.size(192.dp, 15.dp))
    }
}

@Composable
private fun Shim(modifier: Modifier) {
    val color = MaterialTheme.colors.onBackground.copy(alpha = .1F)
    Canvas(modifier) {
        drawRoundRect(color, size = size, cornerRadius = CornerRadius(size.height))
    }
}


// Previews

@Preview
@Composable
private fun EmptyRepositoryItemPreview() {
    PlaceholderRepositoryItem()
}
