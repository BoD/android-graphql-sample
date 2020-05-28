package com.example.graphqlsample.ui.misc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.graphqlsample.api.apollo.ApolloClientManager.apolloClient
import com.example.graphqlsample.core.apollo.suspendMutate
import com.example.graphqlsample.queries.AddCommentToIssueMutation
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MiscViewModel(application: Application) : AndroidViewModel(application) {
    val loading = MutableLiveData<Boolean>()
    val status = MutableLiveData<Status>()

    fun addCommentToIssue() = viewModelScope.launch {
        try {
            loading.value = true
            val returnedSubjectId = apolloClient.suspendMutate(
                AddCommentToIssueMutation(
                    subjectId = ISSUE_ID_GOOD,
                    body = createCommentBody()
                )
            )
                .data?.addComment?.subject?.id

            Timber.i("returnedSubjectId=$returnedSubjectId")
            status.value = Status.Success
        } catch (e: Exception) {
            Timber.w(e, "Could not add comment to issue")
            status.value = Status.Error(e.message!!)
        } finally {
            loading.value = false
        }
    }

    fun handleErrorResult() = viewModelScope.launch {
        try {
            loading.value = true
            val errors = apolloClient.suspendMutate(
                AddCommentToIssueMutation(
                    subjectId = ISSUE_ID_BAD,
                    body = createCommentBody()
                )
            ).errors!!
            Timber.i("errors=$errors")
            status.value =
                Status.Error("${errors.first().customAttributes["type"]} ${errors.first().message}")
        } catch (e: Exception) {
            Timber.w(e, "Could not add comment to issue")
            status.value = Status.Error(e.message!!)
        } finally {
            loading.value = false
        }
    }

    private fun createCommentBody(): String {
        return "Hello, World!  This test comment was created on ${Date()}.  Have a nice day."
    }

    companion object {
        // Id of this issue: https://github.com/octocat/Hello-World/issues/349
        private const val ISSUE_ID_GOOD = "MDU6SXNzdWUyMzEzOTE1NTE="

        // Id of a non existing issue (test error handling)
        private const val ISSUE_ID_BAD = "XXX"
    }
}

sealed class Status {
    object Success : Status()
    data class Error(val message: String) : Status()
}
