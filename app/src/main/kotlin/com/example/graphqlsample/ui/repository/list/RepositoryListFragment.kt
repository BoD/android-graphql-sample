package com.example.graphqlsample.ui.repository.list

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.graphqlsample.R
import com.example.graphqlsample.databinding.RepositoryListFragmentBinding
import com.example.graphqlsample.ui.repository.adapter.simple.PagedSimpleRepositoryAdapter
import com.google.android.material.snackbar.Snackbar

class RepositoryListFragment : Fragment() {

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
    private lateinit var binding: RepositoryListFragmentBinding

    private val args: RepositoryListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.repository_list_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PagedSimpleRepositoryAdapter()
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
