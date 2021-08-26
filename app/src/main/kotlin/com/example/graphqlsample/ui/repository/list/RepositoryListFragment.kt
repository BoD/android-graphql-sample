package com.example.graphqlsample.ui.repository.list

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

class RepositoryListFragment : Fragment() {

    private val args: RepositoryListFragmentArgs by navArgs()

    private val viewModel by viewModels<RepositoryListViewModel>() {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return RepositoryListViewModel(
                    context!!.applicationContext as Application,
                    args.userLogin
                ) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RepositoryListLayout(viewModel.pagingDataflow)
            }
        }
    }
}
