package com.suprabha.githubcommit.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.suprabha.githubcommit.api.CommitsApiResponseItem
import com.suprabha.githubcommit.data.MainRepository
import com.suprabha.githubcommit.tools.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapLatest
import java.lang.Exception

class CommitsViewModel(private val mainRepository: MainRepository): ViewModel() {

    private val commitsList= MutableLiveData<PagingData<CommitsApiResponseItem>>()

    fun getCommitsList(perPage: Int): LiveData<PagingData<CommitsApiResponseItem>> { val
        response= mainRepository.getRepoCommits(perPage).cachedIn(viewModelScope)

        commitsList.value= response.value

    return response}

    fun getSpecificCommit(repoOwner: String, repoName: String, commitReferenceSha: String)=
        liveData(Dispatchers.IO){

            emit(Resource.loading(null))

            try {

                emit(
                    Resource.success(
                        mainRepository.getSpecificCommit(
                            repoOwner, repoName,
                            commitReferenceSha
                        )
                    )
                )
            } catch (e: Exception){

                emit(Resource.error(null, e.message?: "Error occurred!"))
            }
        }

    }