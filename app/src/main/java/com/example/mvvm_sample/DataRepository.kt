package com.example.mvvm_sample

import com.example.mvvm_sample.remote.ApiService
import com.example.mvvm_sample.remote.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import com.example.mvvm_sample.remote.Result
import com.example.mvvm_sample.remote.Result.*

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