package com.suprabha.githubcommit.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.suprabha.githubcommit.api.CommitsApiResponse
import com.suprabha.githubcommit.api.GithubApiService
import com.suprabha.githubcommit.databinding.FragmentListBinding
import com.suprabha.githubcommit.tools.Resource
import com.suprabha.githubcommit.tools.Status
import com.suprabha.githubcommit.viewmodel.CommitsViewModel
import com.suprabha.githubcommit.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private lateinit var viewModelCommits: CommitsViewModel
    private val listAdapter by lazy { CommitsListAdapter() }
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelCommits = ViewModelProvider(
            this,
            ViewModelFactory(GithubApiService.create())
        ).get(CommitsViewModel::class.java)

        val layoutManager = LinearLayoutManager(context)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.context,
                (layoutManager).orientation
            )
        )

        binding.recyclerView.adapter = listAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModelCommits.getCommitsList(30).observe(viewLifecycleOwner, {
                listAdapter.submitData(lifecycle, it)}) }
        listAdapter.addLoadStateListener { loadStates ->

            val isListEmpty= loadStates.refresh is LoadState.NotLoading && listAdapter.itemCount== 0

        binding.errorMessage.isVisible= isListEmpty
        binding.recyclerView.isVisible= loadStates.source.refresh is LoadState.NotLoading
        binding.loadingProgressBar.isVisible= loadStates.append is
                LoadState.Loading|| loadStates.source.refresh is LoadState.Loading

            val errorState= when{

                loadStates.append is LoadState.Error-> loadStates.append as LoadState.Error
                loadStates.prepend is LoadState.Error-> loadStates.prepend as LoadState.Error
                loadStates.refresh is LoadState.Error-> loadStates.refresh as LoadState.Error

                else-> null
            }
            errorState?.let { Toast.makeText(context, "error: ${it.error.message.toString()}",
                Toast.LENGTH_SHORT).show() }
        }
    }
}