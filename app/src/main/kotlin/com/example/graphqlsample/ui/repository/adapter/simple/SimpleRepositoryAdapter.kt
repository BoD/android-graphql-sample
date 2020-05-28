package com.example.graphqlsample.ui.repository.adapter.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graphqlsample.R
import com.example.graphqlsample.core.diffutil.BasicItemCallback
import com.example.graphqlsample.databinding.RepositorySeeMoreListItemBinding
import com.example.graphqlsample.databinding.RepositorySimpleListItemBinding

class SimpleRepositoryAdapter(
    private val callbacks: SimpleRepositoryAdapterCallbacks? = null
) : ListAdapter<RepositoryUiModel, SimpleRepositoryAdapter.ViewHolder>(BasicItemCallback()) {
    companion object {
        private const val TYPE_ACTUAL = 0
        private const val TYPE_SEE_MORE = 1
    }

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class ActualViewHolder(val binding: RepositorySimpleListItemBinding) :
            ViewHolder(binding.root)

        class SeeMoreViewHolder(val binding: RepositorySeeMoreListItemBinding) :
            ViewHolder(binding.root)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SimpleRepositoryUiModel -> TYPE_ACTUAL
            is SeeMoreRepositoryUiModel -> TYPE_SEE_MORE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_ACTUAL -> ViewHolder.ActualViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.repository_simple_list_item,
                    parent,
                    false
                )
            )
            TYPE_SEE_MORE -> ViewHolder.SeeMoreViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.repository_see_more_list_item,
                    parent,
                    false
                )
            )
            else -> throw AssertionError()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_ACTUAL -> {
                val item = getItem(position)
                holder as ViewHolder.ActualViewHolder
                holder.binding.repository = item as SimpleRepositoryUiModel
                holder.binding.executePendingBindings()
            }

            TYPE_SEE_MORE -> {
                holder.itemView.setOnClickListener { callbacks?.onSeeMoreClicked() }
            }
        }
    }
}
