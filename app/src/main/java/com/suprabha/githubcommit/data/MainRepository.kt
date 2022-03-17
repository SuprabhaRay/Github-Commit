package com.suprabha.githubcommit.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.suprabha.githubcommit.api.CommitsApiResponseItem
import com.suprabha.githubcommit.api.GithubApiService
import kotlinx.coroutines.flow.Flow

class MainRepository(private val apiService: GithubApiService) {

     fun getRepoCommits(perPage: Int):
             LiveData<PagingData<CommitsApiResponseItem>> { return Pager(config=
     PagingConfig(pageSize
     = NETWORK_PAGE_SIZE, enablePlaceholders = false), pagingSourceFactory= {
         GithubPagingSource(apiService, perPage)}).liveData}

    suspend fun getSpecificCommit(repoOwner: String, repoName: String, commitReferenceSha: String)=
        apiService.getSpecificCommit(repoOwner, repoName, commitReferenceSha)

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }
}