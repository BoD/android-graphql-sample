package com.example.graphqlsample.ui.viewer.info

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.graphqlsample.R
import com.example.graphqlsample.api.apollo.ApolloClientManager.apolloClient
import com.example.graphqlsample.core.apollo.suspendQuery
import com.example.graphqlsample.queries.ViewerInfoQuery
import com.example.graphqlsample.ui.repository.adapter.simple.RepositoryUiModel
import com.example.graphqlsample.ui.repository.adapter.simple.SeeMoreRepositoryUiModel
import com.example.graphqlsample.ui.repository.adapter.simple.SimpleRepositoryUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

class ViewerInfoViewModel(application: Application) : AndroidViewModel(application) {
    val loading = MutableLiveData(true)
    val isError = MutableLiveData<Boolean>()

    val login = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val repositoryList = MutableLiveData<List<RepositoryUiModel>>()

    init {
        viewModelScope.launch {
            try {
                val viewerInfo: ViewerInfoQuery.Data = apolloClient
                    .suspendQuery(ViewerInfoQuery())
                    .data!!

                login.value = viewerInfo.viewer.login
                name.value = viewerInfo.viewer.name
                email.value = viewerInfo.viewer.email

                val repositoryUiModelList = mutableListOf<RepositoryUiModel>()
                repositoryUiModelList += viewerInfo.viewer.repositories.nodes!!.map { note ->
                    SimpleRepositoryUiModel(
                        note!!.name,
                        note.description
                            ?: getApplication<Application>().getString(R.string.repository_noDescription),
                        note.stargazers.totalCount.toString()
                    )
                }
                if (viewerInfo.viewer.repositories.totalCount > 10) {
                    repositoryUiModelList += SeeMoreRepositoryUiModel
                }

                repositoryList.value = repositoryUiModelList
                isError.value = false
            } catch (e: Exception) {
                Timber.w(e, "Could not fetch user info")
                isError.value = true
            } finally {
                loading.value = false
            }
        }
    }
}
