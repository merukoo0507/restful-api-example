package com.example.mvvm_sample.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import com.example.mvvm_sample.remote.Result.*
import com.example.mvvm_sample.remote.model.User
import com.example.mvvm_sample.remote.model.UserDetail

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

    suspend fun getUserRepos(page: Int): Result<List<User>> {

        return withContext(ioDispatcher) {
            return@withContext try {
                Success(ApiService.getApiManager().getUserRepos(page))
            } catch (e: Exception) {
                Error(e)
            }
        }
    }

    suspend fun getUser(name: String): Result<UserDetail> {
        return withContext(ioDispatcher) {
            return@withContext  try {
                Success(ApiService.getApiManager().getUser(name))
            } catch (e: Exception) {
                Error(e)
            }
        }
    }
}