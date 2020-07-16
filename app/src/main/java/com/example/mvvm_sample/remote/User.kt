package com.example.mvvm_sample.remote

import com.google.gson.annotations.SerializedName


class User {
    @SerializedName("id")
    var userId: Int = 0

    @SerializedName("login")
    var login: String? = null
}