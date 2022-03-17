package com.suprabha.githubcommit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.suprabha.githubcommit.api.GithubApiService
import com.suprabha.githubcommit.data.MainRepository
import java.lang.IllegalArgumentException

class ViewModelFactory(private val apiService: GithubApiService): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CommitsViewModel::class.java))

            return CommitsViewModel(MainRepository(apiService)) as T

        throw IllegalArgumentException("Unknown class name")
    }
}