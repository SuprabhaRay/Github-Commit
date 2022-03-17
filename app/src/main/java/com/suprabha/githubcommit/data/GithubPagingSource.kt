package com.suprabha.githubcommit.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.suprabha.githubcommit.api.CommitsApiResponse
import com.suprabha.githubcommit.api.CommitsApiResponseItem
import com.suprabha.githubcommit.api.GithubApiService
import com.suprabha.githubcommit.data.MainRepository.Companion.NETWORK_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException

private const val GITHUB_STARTING_PAGE_INDEX = 1

class GithubPagingSource(private val service: GithubApiService, private var perPage: Int):
    PagingSource<Int, CommitsApiResponseItem>()
{

    override fun getRefreshKey(state: PagingState<Int, CommitsApiResponseItem>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
        }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommitsApiResponseItem> {

        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX

        perPage= params.loadSize

        return try {

            val response = service.getRepoCommits(params.loadSize, position)
            val nextKey = if (response.isEmpty()) {

                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = response,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )     } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
        }
}