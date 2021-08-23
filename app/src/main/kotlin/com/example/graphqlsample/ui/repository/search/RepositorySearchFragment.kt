package com.example.graphqlsample.ui.repository.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.graphqlsample.R
import com.example.graphqlsample.databinding.RepositorySearchFragmentBinding
import com.example.graphqlsample.ui.repository.adapter.search.SearchRepositoryAdapter
import com.google.android.material.snackbar.Snackbar

class RepositorySearchFragment : Fragment() {

    private val viewModel by viewModels<RepositorySearchViewModel>()
    private lateinit var binding: RepositorySearchFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.repository_search_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SearchRepositoryAdapter()
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
    }
}
