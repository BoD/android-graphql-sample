package com.example.graphqlsample.ui

import com.example.graphqlsample.ui.misc.MiscViewModel
import com.example.graphqlsample.ui.repository.list.RepositoryListViewModel
import com.example.graphqlsample.ui.repository.search.RepositorySearchViewModel
import com.example.graphqlsample.ui.viewer.info.ViewerInfoViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val uiModule = module {
  viewModel<MiscViewModel>()
  viewModel<RepositoryListViewModel>()
  viewModel<RepositorySearchViewModel>()
  viewModel<ViewerInfoViewModel>()
}
