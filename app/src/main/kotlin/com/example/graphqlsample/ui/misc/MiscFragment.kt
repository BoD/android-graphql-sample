package com.example.graphqlsample.ui.misc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.graphqlsample.R
import com.example.graphqlsample.databinding.MiscFragmentBinding
import com.google.android.material.snackbar.Snackbar

class MiscFragment : Fragment() {
    private val viewModel by viewModels<MiscViewModel>()
    private lateinit var binding: MiscFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.misc_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                Status.Success -> Snackbar.make(
                    view,
                    getString(R.string.success),
                    Snackbar.LENGTH_LONG
                ).show()

                is Status.Error -> Snackbar.make(
                    view,
                    getString(R.string.error_withInfo, it.message),
                    Snackbar.LENGTH_INDEFINITE
                ).show()

                else -> Unit
            }
        }
    }
}
