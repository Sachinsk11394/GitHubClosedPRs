package com.sachin.githubclosedprs

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class SearchActivityRepository(private val webService: SearchWebService,
                                    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {
    suspend fun getRepos(userName: String): NetworkResponse<List<Repositories>> {
        return withContext(ioDispatcher){
            val response = webService.getRepos(userName)
            if (response.isSuccessful) {
                return@withContext NetworkResponse.success(response.body()!!)
            } else {
                return@withContext NetworkResponse.failure<List<Repositories>>(Throwable("Something went wrong, Please try again later."))
            }
        }
    }

    suspend fun getClosedPRs(userName: String, repoName: String): NetworkResponse<List<PullRequest>> {
        return withContext(ioDispatcher){
            val response = webService.getClosedPRs(userName, repoName)
            if (response.isSuccessful) {
                return@withContext NetworkResponse.success(response.body()!!)
            } else {
                return@withContext NetworkResponse.failure<List<PullRequest>>(Throwable("Something went wrong, Please try again later."))
            }
        }
    }
}