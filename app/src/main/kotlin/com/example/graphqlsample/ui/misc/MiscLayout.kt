package com.example.graphqlsample.ui.misc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.graphqlsample.R
import com.example.graphqlsample.core.ui.FullScreenLoading

@Composable
fun MiscLayout() {
  val viewModel: MiscViewModel = hiltViewModel()
  val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()
  MiscLayoutContent(uiModel, viewModel::addCommentToIssue, viewModel::handleErrorResult)
}

@Composable
private fun MiscLayoutContent(
  uiModel: MiscViewModel.MiscUiModel,
  addCommentToIssue: () -> Unit,
  handleErrorResult: () -> Unit,
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val snackbarMessage = when (uiModel.status) {
    MiscViewModel.MiscUiModel.Status.Success -> stringResource(R.string.success)
    is MiscViewModel.MiscUiModel.Status.Error -> stringResource(
      R.string.error_withInfo,
      uiModel.status.message,
    )

    MiscViewModel.MiscUiModel.Status.Idle -> ""
  }
  LaunchedEffect(uiModel) {
    if (!uiModel.isLoading && (uiModel.status == MiscViewModel.MiscUiModel.Status.Success ||
        uiModel.status is MiscViewModel.MiscUiModel.Status.Error)
    ) {
      snackbarHostState.showSnackbar(snackbarMessage, duration = SnackbarDuration.Indefinite)
    } else {
      snackbarHostState.currentSnackbarData?.dismiss()
    }
  }

  Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
    Box(
      Modifier
        .padding(paddingValues)
        .fillMaxSize(),
    ) {
      if (uiModel.isLoading) FullScreenLoading()

      Column(
        Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Button(onClick = addCommentToIssue) {
          Text(text = stringResource(R.string.misc_addCommentToIssue))
        }
        Spacer(Modifier.size(8.dp))
        Button(onClick = handleErrorResult) {
          Text(text = stringResource(R.string.misc_handleErrorResult))
        }
      }
    }
  }
}


// Previews

@Preview
@Composable
private fun MiscLayoutContentPreview() {
  MiscLayoutContent(
    uiModel = MiscViewModel.MiscUiModel(
      isLoading = false,
      status = MiscViewModel.MiscUiModel.Status.Idle,
    ),
    {}, {},
  )
}
