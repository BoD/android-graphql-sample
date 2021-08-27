package com.example.graphqlsample.ui.repository.item

import androidx.compose.foundation.layout.Row
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.graphqlsample.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RepositoryItem(repository: SimpleRepositoryItemUiModel) {
    ListItem(
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(1F), text = repository.name)
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = repository.stars, style = MaterialTheme.typography.body2)
                    Icon(painter = painterResource(R.drawable.ic_star_black_16dp), contentDescription = null)
                }
            }
        },
        secondaryText = {
            Text(repository.description)
        }
    )
}
