package com.example.restful_api_example.remote.model


import com.google.gson.annotations.SerializedName

data class SearchUserModel(
    @SerializedName("items")
    val items: List<User>
)

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("site_admin")
    val siteAdmin: Boolean
)