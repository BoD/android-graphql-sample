package com.example.graphqlsample.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.graphqlsample.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    onMenuSearchClick: () -> Unit,
    onMenuMiscClick: () -> Unit,
    content: @Composable () -> Unit
) {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    actions = {
                        var menuExpanded by remember { mutableStateOf(false) }
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, null)
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    menuExpanded = false
                                    onMenuSearchClick()
                                },
                                text = {
                                    Text(stringResource(R.string.main_menu_search))
                                }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    menuExpanded = false
                                    onMenuMiscClick()
                                },
                                text = {
                                    Text(stringResource(R.string.main_menu_misc))
                                }
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                content()
            }
        }
    }
}
