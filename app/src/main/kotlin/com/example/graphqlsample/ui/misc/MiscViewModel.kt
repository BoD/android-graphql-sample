package com.example.graphqlsample.ui.misc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graphqlsample.api.apollo.ApolloClientManager.apolloClient
import com.example.graphqlsample.queries.AddCommentToIssueMutation
import com.example.graphqlsample.ui.misc.MiscViewModel.MiscUiModel.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MiscViewModel(application: Application) : AndroidViewModel(application) {

    val uiModel: MutableStateFlow<MiscUiModel> = MutableStateFlow(MiscUiModel(isLoading = false, status = Status.Idle))

    fun addCommentToIssue() = viewModelScope.launch {
        try {
            uiModel.value = uiModel.value.copy(isLoading = true)
            val returnedSubjectId: String = apolloClient.mutation(
                AddCommentToIssueMutation(
                    subjectId = ISSUE_ID_GOOD,
                    body = createCommentBody()
                )
            )
                .execute()
                .dataAssertNoErrors.addComment!!.subject.id

            Timber.i("returnedSubjectId=$returnedSubjectId")
            uiModel.value = MiscUiModel(isLoading = false, status = Status.Success)
        } catch (e: Exception) {
            Timber.w(e, "Could not add comment to issue")
            uiModel.value = MiscUiModel(isLoading = false, status = Status.Error(e.message!!))
        }
    }

    fun handleErrorResult() = viewModelScope.launch {
        try {
            uiModel.value = uiModel.value.copy(isLoading = true)
            val errors = apolloClient.mutation(
                AddCommentToIssueMutation(
                    subjectId = ISSUE_ID_BAD,
                    body = createCommentBody()
                )
            )
                .execute()
                .errors!!
            Timber.i("errors=$errors")
            uiModel.value = MiscUiModel(isLoading = false, status = Status.Error("type: ${errors.first().extensions?.get("type")}, message: ${errors.first().message}"))
        } catch (e: Exception) {
            Timber.w(e, "Could not add comment to issue")
            uiModel.value = MiscUiModel(isLoading = false, status = Status.Error(e.message!!))
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
            object Idle : Status
            object Success : Status
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



