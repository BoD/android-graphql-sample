package com.example.graphqlsample.ui.viewer.info

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.example.graphqlsample.R
import com.example.graphqlsample.queries.ViewerInfoQuery
import com.example.graphqlsample.ui.repository.item.RepositoryItemUiModel
import com.example.graphqlsample.ui.repository.item.SeeMoreRepositoryItemUiModel
import com.example.graphqlsample.ui.repository.item.SimpleRepositoryItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewerInfoViewModel @Inject constructor(
    application: Application,
    apolloClient: ApolloClient,
) : AndroidViewModel(application) {
    val uiModel: MutableStateFlow<ViewerInfoUiModel> = MutableStateFlow(ViewerInfoUiModel.Loading)

    init {
        viewModelScope.launch {
            val response = apolloClient
                .query(ViewerInfoQuery())
                .execute()
            uiModel.value = when {
                response.exception != null -> {
                    Timber.w(response.exception, "Could not fetch user info")
                    ViewerInfoUiModel.Error
                }

                response.hasErrors() -> {
                    Timber.w("Could not fetch user info: ${response.errors!!.joinToString { it.message }}")
                    ViewerInfoUiModel.Error
                }

                else -> {
                    val viewerInfo: ViewerInfoQuery.Data = response.data!!

                    val repositoryUiModelList = mutableListOf<RepositoryItemUiModel>()
                    repositoryUiModelList += viewerInfo.viewer.repositories.nodes.map { note ->
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

                    ViewerInfoUiModel.Loaded(
                        login = viewerInfo.viewer.login,
                        name = viewerInfo.viewer.name,
                        email = viewerInfo.viewer.email,
                        repositoryItemList = repositoryUiModelList,
                    )
                }
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
