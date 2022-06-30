package com.example.gitRestSample.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import com.example.gitRestSample.remote.Result.*
import com.example.gitRestSample.remote.model.User
import com.example.gitRestSample.remote.model.UserDetail

class DataRepository {
    suspend fun searchUsers(q: String, page: Int): Result<List<User>> {
        return withContext(ioDispatcher) {
            return@withContext try {
                if (q.isNullOrEmpty()) {
                    Success(ApiService.getApiManager().getUsers(page))
                } else {
                    Success(ApiService.getApiManager().searchUsers(q, page).items)
                }
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

    companion object {
        val ioDispatcher = Dispatchers.IO
    }
}