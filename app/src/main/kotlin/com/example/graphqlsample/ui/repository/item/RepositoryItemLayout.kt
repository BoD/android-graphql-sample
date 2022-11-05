package com.example.graphqlsample.ui.repository.item

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.graphqlsample.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryItem(repository: SimpleRepositoryItemUiModel) {
    ListItem(
        headlineText = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(1F), text = repository.name)
                Text(text = repository.stars, style = MaterialTheme.typography.bodyMedium)
                Icon(
                    painter = painterResource(R.drawable.ic_star_black_16dp),
                    contentDescription = null
                )
            }
        },
        supportingText = {
            Text(repository.description)
        }
    )
}
