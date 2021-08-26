package com.example.graphqlsample.ui.misc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class MiscFragment : Fragment() {
    private val viewModel by viewModels<MiscViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiModel by viewModel.uiModel.collectAsState()
                MiscLayout(uiModel = uiModel, viewModel::addCommentToIssue, viewModel::handleErrorResult)
            }
        }
    }
}
