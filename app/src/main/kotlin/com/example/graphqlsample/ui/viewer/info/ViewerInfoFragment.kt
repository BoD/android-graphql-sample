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

class ViewerInfoFragment : Fragment(),
    SimpleRepositoryAdapterCallbacks {
    private val viewModel by viewModels<ViewerInfoViewModel>()
    private lateinit var binding: ViewerInfoFragmentBinding
    private lateinit var adapter: SimpleRepositoryAdapter

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
        adapter = SimpleRepositoryAdapter(this)
        binding.rclRepositories.adapter = adapter
        viewModel.repositoryList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (isError) Snackbar.make(
                view,
                getString(R.string.error_generic),
                Snackbar.LENGTH_INDEFINITE
            ).show()
        }

        setHasOptionsMenu(true)
    }

    override fun onSeeMoreClicked() {
        findNavController().navigate(
            ViewerInfoFragmentDirections.actionViewerInfoFragmentToRepositoryListFragment(
                viewModel.login.value!!
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
