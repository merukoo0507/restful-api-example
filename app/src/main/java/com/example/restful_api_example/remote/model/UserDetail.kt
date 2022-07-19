package com.example.restful_api_example.remote.model


import com.google.gson.annotations.SerializedName

data class UserDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("bio")
    val bio: Any,
    @SerializedName("login")
    val login: String,
    @SerializedName("site_admin")
    val siteAdmin: Boolean,
    @SerializedName("location")
    val location: String,
    @SerializedName("blog")
    val blog: String
)