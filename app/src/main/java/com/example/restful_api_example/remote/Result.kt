package com.example.restful_api_example.remote

import timber.log.Timber
import java.lang.Exception

sealed class Result<out R> {
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}