package com.sachin.githubclosedprs

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Repositories(
    @Json(name = "name")
    val name: String,

    @Json(name = "owner")
    val owner: User,
)

@JsonClass(generateAdapter = true)
data class PullRequest(
    @Json(name = "id")
    val id: Int,

    @Json(name = "title")
    val title: String,

    @Json(name = "created_at")
    val createdAt: String,

    @Json(name = "closed_at")
    val closedAt: String,

    @Json(name = "user")
    val user: User,
)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "login")
    val userName: String,

    @Json(name = "avatar_url")
    val imageUrl: String,
)
