package com.example.graphqlsample.ui.viewer.info

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.graphqlsample.R
import com.example.graphqlsample.core.ui.FullScreenLoading
import com.example.graphqlsample.ui.repository.item.RepositoryItem
import com.example.graphqlsample.ui.repository.item.RepositoryItemUiModel
import com.example.graphqlsample.ui.repository.item.SeeMoreRepositoryItemUiModel
import com.example.graphqlsample.ui.repository.item.SimpleRepositoryItemUiModel
import com.example.graphqlsample.ui.viewer.info.ViewerInfoViewModel.ViewerInfoUiModel
import com.example.graphqlsample.ui.viewer.info.ViewerInfoViewModel.ViewerInfoUiModel.*

@Composable
fun ViewerInfoLayout(
    viewModel: ViewerInfoViewModel,
    onSeeMoreClick: (String) -> Unit
) {
    val uiModel by viewModel.uiModel.collectAsState()
    ViewerInfoLayoutContent(
        uiModel = uiModel,
        onSeeMoreClick = {
            val login = (uiModel as Loaded).login
            onSeeMoreClick(login)
        }
    )
}

@Composable
private fun ViewerInfoLayoutContent(uiModel: ViewerInfoUiModel, onSeeMoreClick: () -> Unit) {
    val scaffoldState = rememberScaffoldState()
    if (uiModel is Error) {
        val message = stringResource(R.string.error_generic)
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message,
                duration = SnackbarDuration.Indefinite
            )
        }
    }
    Scaffold(scaffoldState = scaffoldState) { paddingValues ->
        Crossfade(
            uiModel is Loading,
            modifier = Modifier.padding(paddingValues)
        ) { isLoading ->
            if (isLoading) {
                FullScreenLoading()
            } else if (uiModel is Loaded) {
                Loaded(uiModel, onSeeMoreClick)
            }
        }
    }
}

@Composable
private fun Loaded(uiModel: Loaded, onSeeMoreClick: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        UserInfo(uiModel)
        RepositoryList(uiModel.repositoryItemList, onSeeMoreClick)
    }
}

@Composable
private fun UserInfo(uiModel: Loaded) {
    Card(Modifier.fillMaxWidth(), elevation = 4.dp) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = uiModel.login, style = typography.h5)
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                uiModel.name?.let { Text(text = it) }
                Spacer(Modifier.height(4.dp))
                Text(text = uiModel.email)
            }
        }
    }
}

@Composable
private fun RepositoryList(
    repositoryItemList: List<RepositoryItemUiModel>,
    onSeeMoreClick: () -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
        items(repositoryItemList) { repository ->
            when (repository) {
                is SimpleRepositoryItemUiModel -> RepositoryItem(repository)
                SeeMoreRepositoryItemUiModel -> MoreItem(onSeeMoreClick)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MoreItem(onClick: () -> Unit) {
    ListItem(
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.see_more),
                style = typography.button,
                textAlign = TextAlign.End
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}


// Previews

@Preview
@Composable
private fun LoadedViewerInfoLayoutPreview() {
    ViewerInfoLayoutContent(
        Loaded(
            "JohnDoe42", "John Doe", "john.doe@example.com", listOf(
                SimpleRepositoryItemUiModel(
                    "The first repository",
                    "This repository is very interesting!",
                    "4"
                ),
                SimpleRepositoryItemUiModel(
                    "The second repository",
                    "I will not buy this record, it is scratched",
                    "1"
                ),
                SeeMoreRepositoryItemUiModel,
            )
        )
    ) {}
}

@Preview
@Composable
private fun ErrorViewerInfoLayoutPreview() {
    ViewerInfoLayoutContent(Error) {}
}
