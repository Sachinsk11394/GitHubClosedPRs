package com.sachin.githubclosedprs

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SearchWebService {
    @GET("users/{userName}/repos")
    suspend fun getRepos(
        @Path("userName") userName: String,
    ): Response<List<Repositories>>

    @GET("repos/{userName}/{repoName}/pulls?state=closed")
    suspend fun getClosedPRs(
        @Path("userName") userName: String,
        @Path("repoName") repoName: String,
    ): Response<List<PullRequest>>
}