package com.example.graphqlsample.ui.viewer.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.graphqlsample.R
import com.example.graphqlsample.databinding.ViewerInfoFragmentBinding
import com.example.graphqlsample.ui.repository.adapter.simple.SimpleRepositoryAdapter
import com.example.graphqlsample.ui.repository.adapter.simple.SimpleRepositoryAdapterCallbacks
import com.google.android.material.snackbar.Snackbar

class ViewerInfoFragment : Fragment(), SimpleRepositoryAdapterCallbacks {
    private val viewModel by viewModels<ViewerInfoViewModel>()
    private lateinit var binding: ViewerInfoFragmentBinding
    private val adapter = SimpleRepositoryAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.viewer_info_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rclRepositories.adapter = adapter
        viewModel.uiModel.observe(viewLifecycleOwner) { uiModel ->
            when (uiModel) {
                ViewerInfoViewModel.ViewerInfoUiModel.Loading -> binding.isLoading = true
                ViewerInfoViewModel.ViewerInfoUiModel.Error -> {
                    binding.isLoading = false
                    showError()
                }
                is ViewerInfoViewModel.ViewerInfoUiModel.Loaded -> {
                    binding.isLoading = false
                    binding.uiModel = uiModel
                    adapter.submitList(uiModel.repositoryList)
                }
            }
        }

        setHasOptionsMenu(true)
    }

    private fun showError() {
        Snackbar.make(binding.root, getString(R.string.error_generic), Snackbar.LENGTH_INDEFINITE).show()
    }

    override fun onSeeMoreClicked() {
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
