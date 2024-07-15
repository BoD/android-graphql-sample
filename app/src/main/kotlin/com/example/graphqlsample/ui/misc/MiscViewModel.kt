package com.example.graphqlsample.ui.misc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.example.graphqlsample.graphql.AddCommentToIssueMutation
import com.example.graphqlsample.ui.misc.MiscViewModel.MiscUiModel.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MiscViewModel @Inject constructor(
    application: Application,
    private val apolloClient: ApolloClient,
) : AndroidViewModel(application) {

    val uiModel: MutableStateFlow<MiscUiModel> = MutableStateFlow(MiscUiModel(isLoading = false, status = Status.Idle))

    fun addCommentToIssue() = viewModelScope.launch {
        uiModel.value = uiModel.value.copy(isLoading = true)
        val apolloResponse = apolloClient.mutation(
            AddCommentToIssueMutation(
                subjectId = ISSUE_ID_GOOD,
                body = createCommentBody()
            )
        ).execute()
        val data = apolloResponse.data
        if (data != null) {
            val returnedSubjectId: String = data.addComment!!.subject.id
            Timber.i("returnedSubjectId=$returnedSubjectId")
            uiModel.value = MiscUiModel(isLoading = false, status = Status.Success)
        } else if (apolloResponse.exception != null) {
            Timber.w(apolloResponse.exception, "Could not add comment to issue")
            uiModel.value = MiscUiModel(isLoading = false, status = Status.Error(apolloResponse.exception!!.message!!))
        }
    }

    fun handleErrorResult() = viewModelScope.launch {
        uiModel.value = uiModel.value.copy(isLoading = true)
        val apolloResponse = apolloClient.mutation(
            AddCommentToIssueMutation(
                subjectId = ISSUE_ID_BAD,
                body = createCommentBody()
            )
        ).execute()
        val errors = apolloResponse.errors
        if (errors != null) {
            Timber.i("errors=$errors")
            uiModel.value = MiscUiModel(
                isLoading = false,
                status = Status.Error("type: ${errors.first().extensions?.get("type")}, message: ${errors.first().message}")
            )
        } else if (apolloResponse.exception != null) {
            Timber.w(apolloResponse.exception, "Could not add comment to issue")
            uiModel.value = MiscUiModel(isLoading = false, status = Status.Error(apolloResponse.exception!!.message!!))
        }
    }

    private fun createCommentBody(): String {
        return "Hello, World!  This test comment was created on ${Date()}.  Have a nice day."
    }

    data class MiscUiModel(
        val isLoading: Boolean,
        val status: Status,
    ) {
        sealed interface Status {
            data object Idle : Status
            data object Success : Status
            data class Error(val message: String) : Status
        }
    }

    companion object {
        // Id of this issue: https://github.com/octocat/Hello-World/issues/349
        private const val ISSUE_ID_GOOD = "MDU6SXNzdWUyMzEzOTE1NTE="

        // Id of a non existing issue (test error handling)
        private const val ISSUE_ID_BAD = "XXX"
    }

}

