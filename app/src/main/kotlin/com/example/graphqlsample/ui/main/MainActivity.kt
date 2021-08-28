package com.example.graphqlsample.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.graphqlsample.ui.misc.MiscLayout
import com.example.graphqlsample.ui.repository.list.RepositoryListLayout
import com.example.graphqlsample.ui.repository.search.RepositorySearchLayout
import com.example.graphqlsample.ui.viewer.info.ViewerInfoLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MainLayout(
                onMenuSearchClick = { navController.navigate(Destinations.REPOSITORY_SEARCH.name) },
                onMenuMiscClick = { navController.navigate(Destinations.MISC.name) },
            ) {
                MainNavHost(navController)
            }
        }
    }

    @Composable
    private fun MainNavHost(navController: NavHostController) {
        NavHost(navController, startDestination = Destinations.VIEWER_INFO.name) {
            composable(Destinations.VIEWER_INFO.name) {
                ViewerInfoLayout(
                    onSeeMoreClick = { login ->
                        navController.navigate("${Destinations.REPOSITORY_LIST.name}/$login")
                    }
                )
            }
            composable(Destinations.MISC.name) { MiscLayout() }
            composable(Destinations.REPOSITORY_SEARCH.name) { RepositorySearchLayout() }
            composable("${Destinations.REPOSITORY_LIST.name}/{userLogin}") { backStackEntry ->
                RepositoryListLayout(userLogin = backStackEntry.arguments!!.getString("userLogin")!!)
            }
        }
    }
}

enum class Destinations {
    VIEWER_INFO,
    MISC,
    REPOSITORY_SEARCH,
    REPOSITORY_LIST,
}
