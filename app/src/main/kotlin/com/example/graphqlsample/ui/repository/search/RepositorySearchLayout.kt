package com.example.graphqlsample.ui.repository.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.graphqlsample.R
import com.example.graphqlsample.core.ui.FullScreenLoading
import com.example.graphqlsample.ui.repository.search.RepositorySearchViewModel.RepositorySearchItemUiModel
import com.example.graphqlsample.ui.repository.search.RepositorySearchViewModel.RepositorySearchItemUiModel.OwnerType
import com.example.graphqlsample.ui.repository.search.RepositorySearchViewModel.RepositorySearchUiModel

@Composable
fun RepositorySearchLayout(viewModel: RepositorySearchViewModel) {
    val uiModel by viewModel.uiModel.collectAsState()
    RepositorySearchLayoutContent(uiModel)
}

@Composable
private fun RepositorySearchLayoutContent(uiModel: RepositorySearchUiModel) {
    val scaffoldState = rememberScaffoldState()
    if (uiModel is RepositorySearchUiModel.Error) {
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
            uiModel is RepositorySearchUiModel.Loading,
            modifier = Modifier.padding(paddingValues)
        ) { isLoading ->
            if (isLoading) {
                FullScreenLoading()
            } else if (uiModel is RepositorySearchUiModel.Loaded) {
                RepositoryList(uiModel.repositorySearchItemList)
            }
        }
    }
}

@Composable
private fun RepositoryList(repositorySearchItemList: List<RepositorySearchItemUiModel>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(repositorySearchItemList) { repository ->
            Repository(repository)
        }
    }
}

@Composable
private fun Repository(repositorySearchItem: RepositorySearchItemUiModel) {
    Card(elevation = 4.dp) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = repositorySearchItem.name, style = MaterialTheme.typography.subtitle1)
            Spacer(Modifier.size(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = repositorySearchItem.ownerName,
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(Modifier.size(4.dp))
                UserOrOrga(repositorySearchItem.ownerType)
            }
            repositorySearchItem.ownerUserBio?.let { ownerUserBio ->
                Spacer(Modifier.size(4.dp))
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = ownerUserBio,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}

@Composable
private fun UserOrOrga(ownerType: OwnerType) {
    Text(
        modifier = Modifier
            .background(
                shape = RoundedCornerShape(corner = CornerSize(8.dp)),
                color = colorResource(
                    when (ownerType) {
                        OwnerType.USER -> R.color.ownerType_background_user
                        OwnerType.ORGANIZATION -> R.color.ownerType_background_orga
                    }
                )
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        text = stringResource(
            when (ownerType) {
                OwnerType.USER -> R.string.ownerType_user
                OwnerType.ORGANIZATION -> R.string.ownerType_orga
            }
        ),
        color = Color.White,
        fontSize = 10.sp,
        style = TextStyle(fontWeight = FontWeight.Bold)
    )
}


// Previews
@Preview
@Composable
private fun RepositorySearchLayoutPreview() {
    RepositorySearchLayoutContent(
        uiModel = RepositorySearchUiModel.Loaded(
            listOf(
                RepositorySearchItemUiModel(
                    name = "The First Project",
                    ownerType = OwnerType.USER,
                    ownerName = "User001",
                    ownerUserBio = "My super duper bio",
                ),

                RepositorySearchItemUiModel(
                    name = "The Second Project",
                    ownerType = OwnerType.ORGANIZATION,
                    ownerName = "Organization, inc.",
                    ownerUserBio = null,
                ),

                )
        )
    )
}
