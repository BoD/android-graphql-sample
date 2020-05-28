package com.example.graphqlsample.ui.repository.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graphqlsample.R
import com.example.graphqlsample.core.diffutil.BasicItemCallback
import com.example.graphqlsample.databinding.RepositorySearchListItemBinding

class SearchRepositoryAdapter :
    ListAdapter<SearchRepositoryUiModel, SearchRepositoryAdapter.ViewHolder>(BasicItemCallback()) {
    class ViewHolder(val binding: RepositorySearchListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.repository_search_list_item,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.repository = getItem(position)
        holder.binding.executePendingBindings()
    }
}
