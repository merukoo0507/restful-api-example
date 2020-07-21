package com.example.mvvm_sample

import com.example.mvvm_sample.remote.ApiService
import com.example.mvvm_sample.remote.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import com.example.mvvm_sample.remote.Result
import com.example.mvvm_sample.remote.Result.*
import retrofit2.http.HeaderMap

class DataRepository {
    var ioDispatcher = Dispatchers.IO

    suspend fun getUsers(page: Int): Result<List<User>> {
        return withContext(ioDispatcher) {
            return@withContext try {
                Success(ApiService.getApiManager().getUsers(page))
            } catch (e: Exception) {
                Error(e)
            }
        }
    }

    suspend fun getUserRepos(page: Int, perPage: Int): Result<List<User>> {
        var header = hashMapOf<String, String>()
        header["Link:"] = "<https://api.github.com/user/repos?page=$page&per_page=$perPage>; rel=\"next\""

        return withContext(ioDispatcher) {
            return@withContext try {
                Success(ApiService.getApiManager().getUserRepos(page, perPage, header))
            } catch (e: Exception) {
                Error(e)
            }
        }
    }

    suspend fun getUser(name: String): Result<User> {
        return withContext(ioDispatcher) {
            return@withContext  try {
                Success(ApiService.getApiManager().getUser(name))
            } catch (e: Exception) {
                Error(e)
            }
        }
    }
}