package com.suprabha.githubcommit.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {

    @GET("/repos/square/retrofit/commits")
    suspend fun getRepoCommits(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): CommitsApiResponse

    @GET("/repos/{owner}/{repo}/commits/{ref}")
    suspend fun getSpecificCommit(
        @Path("owner") repoOwner: String,
        @Path("repo") repoName: String,
        @Path("ref") commitReferenceSha: String
    ): SpecificCommitResponse

    companion object{

        private const val BASE_URL= "https://api.github.com"

        fun create(): GithubApiService{

            return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory
                .create()).build().create(GithubApiService::class.java)
        }
    }
}