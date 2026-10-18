package com.example.graphqlsample.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.graphqlsample.ui.misc.MiscLayout
import com.example.graphqlsample.ui.navigation.Destination
import com.example.graphqlsample.ui.repository.list.RepositoryListLayout
import com.example.graphqlsample.ui.repository.search.RepositorySearchLayout
import com.example.graphqlsample.ui.viewer.info.ViewerInfoLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val backStack = rememberNavBackStack(Destination.ViewerInfo)

      MainLayout(
        onMenuSearchClick = { backStack.add(Destination.RepositorySearch) },
        onMenuMiscClick = { backStack.add(Destination.Misc) },
      ) {
        MainNavHost(backStack)
      }
    }
  }

  @Composable
  private fun MainNavHost(backStack: NavBackStack<NavKey>) {
    NavDisplay(
      backStack = backStack,
      onBack = { backStack.removeLastOrNull() },
      entryDecorators = listOf(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator(),
      ),
      entryProvider = entryProvider {
        entry<Destination.ViewerInfo> {
          ViewerInfoLayout(
            onSeeMoreClick = { login ->
              backStack.add(Destination.RepositoryList(login))
            },
          )
        }
        entry<Destination.Misc> {
          MiscLayout()
        }
        entry<Destination.RepositorySearch> {
          RepositorySearchLayout()
        }
        entry<Destination.RepositoryList> { key ->
          RepositoryListLayout(key)
        }
      },
    )
  }
}
