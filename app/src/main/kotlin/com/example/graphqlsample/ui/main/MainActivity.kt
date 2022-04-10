package com.example.graphqlsample.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.graphqlsample.ui.misc.MiscLayout
import com.example.graphqlsample.ui.navigation.NavigationArguments
import com.example.graphqlsample.ui.navigation.NavigationDestinations
import com.example.graphqlsample.ui.repository.list.RepositoryListLayout
import com.example.graphqlsample.ui.repository.search.RepositorySearchLayout
import com.example.graphqlsample.ui.viewer.info.ViewerInfoLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MainLayout(
                onMenuSearchClick = { navController.navigate(NavigationDestinations.REPOSITORY_SEARCH.name) },
                onMenuMiscClick = { navController.navigate(NavigationDestinations.MISC.name) },
            ) {
                MainNavHost(navController)
            }
        }
    }

    @Composable
    private fun MainNavHost(navController: NavHostController) {
        NavHost(navController, startDestination = NavigationDestinations.VIEWER_INFO.name) {
            composable(route = NavigationDestinations.VIEWER_INFO.name) { navBackStackEntry ->
                ViewerInfoLayout(
                    viewModel = hiltViewModel(navBackStackEntry),
                    onSeeMoreClick = { login ->
                        navController.navigate(route = "${NavigationDestinations.REPOSITORY_LIST.name}/$login")
                    }
                )
            }
            composable(route = NavigationDestinations.MISC.name) { navBackStackEntry ->
                MiscLayout(viewModel = hiltViewModel(navBackStackEntry))
            }
            composable(route = NavigationDestinations.REPOSITORY_SEARCH.name) { navBackStackEntry ->
                RepositorySearchLayout(viewModel = hiltViewModel(navBackStackEntry))
            }
            composable(route = "${NavigationDestinations.REPOSITORY_LIST.name}/{${NavigationArguments.USER_LOGIN}}") { navBackStackEntry ->
                RepositoryListLayout(viewModel = hiltViewModel(navBackStackEntry))
            }
        }
    }
}
