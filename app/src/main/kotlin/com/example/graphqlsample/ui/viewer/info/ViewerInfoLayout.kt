package com.example.graphqlsample.ui.viewer.info

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.graphqlsample.R
import com.example.graphqlsample.ui.repository.adapter.simple.RepositoryUiModel
import com.example.graphqlsample.ui.repository.adapter.simple.SeeMoreRepositoryUiModel
import com.example.graphqlsample.ui.repository.adapter.simple.SimpleRepositoryUiModel
import com.example.graphqlsample.ui.viewer.info.ViewerInfoViewModel.ViewerInfoUiModel
import com.example.graphqlsample.ui.viewer.info.ViewerInfoViewModel.ViewerInfoUiModel.Error
import com.example.graphqlsample.ui.viewer.info.ViewerInfoViewModel.ViewerInfoUiModel.Loaded
import com.example.graphqlsample.ui.viewer.info.ViewerInfoViewModel.ViewerInfoUiModel.Loading


@Composable
fun ViewerInfoLayout(uiModel: ViewerInfoUiModel, onSeeMoreClick: () -> Unit) {
    MaterialTheme {
        val scaffoldState = rememberScaffoldState()
        if (uiModel is Error) {
            val message = stringResource(R.string.error_generic)
            LaunchedEffect(scaffoldState.snackbarHostState) {
                scaffoldState.snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Indefinite)
            }
        }
        Scaffold(scaffoldState = scaffoldState) {
            Crossfade(uiModel is Loading) { isLoading ->
                if (isLoading) {
                    Loading()
                } else if (uiModel is Loaded) {
                    Loaded(uiModel, onSeeMoreClick)
                }
            }
        }
    }
}

@Composable
private fun Loading() {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Loaded(uiModel: Loaded, onSeeMoreClick: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        UserInfo(uiModel)
        RepositoryList(uiModel.repositoryList, onSeeMoreClick)
    }
}

@Composable
fun UserInfo(uiModel: Loaded) {
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
fun RepositoryList(repositoryList: List<RepositoryUiModel>, onSeeMoreClick: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(repositoryList) { repository ->
            when (repository) {
                is SimpleRepositoryUiModel -> RepositoryItem(repository)
                SeeMoreRepositoryUiModel -> MoreItem(onSeeMoreClick)
            }
        }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RepositoryItem(repository: SimpleRepositoryUiModel) {
    ListItem(
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(1F), text = repository.name)
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = repository.stars, style = typography.body2)
                    Icon(painter = painterResource(R.drawable.ic_star_black_16dp), contentDescription = null)
                }
            }
        },
        secondaryText = {
            Text(repository.description)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MoreItem(onClick: () -> Unit) {
    ListItem(
        text = { Text(modifier = Modifier.fillMaxWidth(), text = stringResource(R.string.see_more), style = typography.button, textAlign = TextAlign.End) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}


// Previews

@Preview
@Composable
fun LoadedViewerInfoLayoutPreview() {
    ViewerInfoLayout(
        Loaded(
            "JohnDoe42", "John Doe", "john.doe@example.com", listOf(
                SimpleRepositoryUiModel("The first repository", "This repository is very interesting!", "4"),
                SimpleRepositoryUiModel("The second repository", "I will not buy this record, it is scratched", "1"),
                SeeMoreRepositoryUiModel,
            )
        )
    ) {}
}

@Preview
@Composable
fun ErrorViewerInfoLayoutPreview() {
    ViewerInfoLayout(Error) {}
}