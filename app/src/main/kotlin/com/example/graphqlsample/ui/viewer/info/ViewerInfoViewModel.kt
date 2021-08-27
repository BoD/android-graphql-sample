package com.example.graphqlsample.ui.viewer.info

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graphqlsample.R
import com.example.graphqlsample.api.apollo.ApolloClientManager.apolloClient
import com.example.graphqlsample.core.apollo.suspendQuery
import com.example.graphqlsample.queries.ViewerInfoQuery
import com.example.graphqlsample.ui.repository.item.RepositoryItemUiModel
import com.example.graphqlsample.ui.repository.item.SeeMoreRepositoryItemUiModel
import com.example.graphqlsample.ui.repository.item.SimpleRepositoryItemUiModel
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

                val repositoryUiModelList = mutableListOf<RepositoryItemUiModel>()
                repositoryUiModelList += viewerInfo.viewer.repositories.nodes!!.map { note ->
                    SimpleRepositoryItemUiModel(
                        note!!.name,
                        note.description
                            ?: getApplication<Application>().getString(R.string.repository_noDescription),
                        note.stargazers.totalCount.toString()
                    )
                }
                if (viewerInfo.viewer.repositories.totalCount > 10) {
                    repositoryUiModelList += SeeMoreRepositoryItemUiModel
                }

                uiModel.value = ViewerInfoUiModel.Loaded(
                    login = viewerInfo.viewer.login,
                    name = viewerInfo.viewer.name,
                    email = viewerInfo.viewer.email,
                    repositoryItemList = repositoryUiModelList,
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
            val repositoryItemList: List<RepositoryItemUiModel>,
        ) : ViewerInfoUiModel
    }
}
