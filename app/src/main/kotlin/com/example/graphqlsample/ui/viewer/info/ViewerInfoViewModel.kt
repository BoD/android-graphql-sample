package com.example.graphqlsample.ui.viewer.info

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graphqlsample.R
import com.example.graphqlsample.api.apollo.ApolloClientManager.apolloClient
import com.example.graphqlsample.core.apollo.suspendQuery
import com.example.graphqlsample.queries.ViewerInfoQuery
import com.example.graphqlsample.ui.repository.item.RepositoryUiModel
import com.example.graphqlsample.ui.repository.item.SeeMoreRepositoryUiModel
import com.example.graphqlsample.ui.repository.item.SimpleRepositoryUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ViewerInfoViewModel(application: Application) : AndroidViewModel(application) {

    val uiModel: MutableStateFlow<ViewerInfoUiModel> = MutableStateFlow(ViewerInfoUiModel.Loading)

    init {
        viewModelScope.launch {
            try {
                val viewerInfo: ViewerInfoQuery.Data = apolloClient
                    .suspendQuery(ViewerInfoQuery())
                    .data!!

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

                uiModel.value = ViewerInfoUiModel.Loaded(
                    login = viewerInfo.viewer.login,
                    name = viewerInfo.viewer.name,
                    email = viewerInfo.viewer.email,
                    repositoryList = repositoryUiModelList,
                )
            } catch (e: Exception) {
                Timber.w(e, "Could not fetch user info")
                uiModel.value = ViewerInfoUiModel.Error
            }
        }
    }

    sealed interface ViewerInfoUiModel {
        object Loading : ViewerInfoUiModel
        object Error : ViewerInfoUiModel
        data class Loaded(
            val login: String,
            val name: String?,
            val email: String,
            val repositoryList: List<RepositoryUiModel>
        ) : ViewerInfoUiModel
    }
}
