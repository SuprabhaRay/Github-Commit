package com.suprabha.githubcommit.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.suprabha.githubcommit.api.CommitsApiResponseItem
import com.suprabha.githubcommit.databinding.CommitItemBinding
import java.lang.StringBuilder

class CommitsListAdapter: PagingDataAdapter<CommitsApiResponseItem,
        CommitsListAdapter.CommitsViewHolder>(DiffUtilCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommitsViewHolder=
        CommitsViewHolder(CommitItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: CommitsViewHolder, position: Int) {

        getItem(position)?.let { holder.bind(it) }
    }
    class CommitsViewHolder(private val binding: CommitItemBinding):
        RecyclerView.ViewHolder(binding.root) {

            fun bind(commit: CommitsApiResponseItem){

                binding.textViewAuthor.text= commit.commit.author.name

                val inputSha= commit.sha.substring(0, 8)

                binding.textViewSha.text= "$inputSha..."
                binding.textViewMessage.text= commit.commit.message
                binding.layoutCommitItem.setOnClickListener {

                    val action= ListFragmentDirections.listToDetails(commit.sha)

                    itemView.findNavController().navigate(action) }
            }
    }
    object DiffUtilCallback: DiffUtil.ItemCallback<CommitsApiResponseItem>(){

        override fun areItemsTheSame(
            oldItem: CommitsApiResponseItem,
            newItem: CommitsApiResponseItem
        ): Boolean {

            return  oldItem.commit.author.name == newItem.commit.author.name||
                    oldItem.sha == newItem.sha|| oldItem.commit.message== newItem.commit.message
        }

        override fun areContentsTheSame(
            oldItem: CommitsApiResponseItem,
            newItem: CommitsApiResponseItem
        ): Boolean {

            return oldItem== newItem
        }
    }
}