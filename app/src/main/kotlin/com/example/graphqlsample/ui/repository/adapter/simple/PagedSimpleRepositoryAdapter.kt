package com.example.graphqlsample.ui.repository.adapter.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graphqlsample.R
import com.example.graphqlsample.core.diffutil.BasicItemCallback
import com.example.graphqlsample.databinding.RepositoryPlaceholderListItemBinding
import com.example.graphqlsample.databinding.RepositorySimpleListItemBinding

class PagedSimpleRepositoryAdapter(
    private val callbacks: SimpleRepositoryAdapterCallbacks? = null
) : PagedListAdapter<SimpleRepositoryUiModel, PagedSimpleRepositoryAdapter.ViewHolder>(
    BasicItemCallback()
) {
    companion object {
        private const val TYPE_ACTUAL = 0
        private const val TYPE_PLACEHOLDER = 1
    }

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class ActualViewHolder(val binding: RepositorySimpleListItemBinding) :
            ViewHolder(binding.root)

        class PlaceholderViewHolder(val binding: RepositoryPlaceholderListItemBinding) :
            ViewHolder(binding.root)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) == null) TYPE_PLACEHOLDER else TYPE_ACTUAL
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
            TYPE_PLACEHOLDER -> ViewHolder.PlaceholderViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.repository_placeholder_list_item,
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

            TYPE_PLACEHOLDER -> {
                holder.itemView.setOnClickListener { callbacks?.onSeeMoreClicked() }
            }
        }
    }
}
