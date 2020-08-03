package com.example.mvvm_sample.remote.model


import com.google.gson.annotations.SerializedName

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