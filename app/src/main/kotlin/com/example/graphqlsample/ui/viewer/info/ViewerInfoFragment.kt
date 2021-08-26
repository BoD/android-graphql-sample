package com.example.graphqlsample.ui.viewer.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.graphqlsample.R

class ViewerInfoFragment : Fragment() {
    private val viewModel by viewModels<ViewerInfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return ComposeView(requireContext()).apply {
            setContent {
                val uiModel by viewModel.uiModel.collectAsState()
                ViewerInfoLayout(uiModel = uiModel, ::onSeeMoreClicked)
            }
        }
    }

    private fun onSeeMoreClicked() {
        findNavController().navigate(
            ViewerInfoFragmentDirections.actionViewerInfoFragmentToRepositoryListFragment(
                (viewModel.uiModel.value as ViewerInfoViewModel.ViewerInfoUiModel.Loaded).login
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.viewer_info, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_misc -> {
                findNavController().navigate(R.id.action_viewerInfoFragment_to_miscFragment)
                true
            }

            R.id.menu_search -> {
                findNavController().navigate(R.id.action_viewerInfoFragment_to_repositorySearchFragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
