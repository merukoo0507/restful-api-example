package com.example.gitRestSample.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import com.example.gitRestSample.remote.Result.*
import com.example.gitRestSample.remote.model.SearchUserModel
import com.example.gitRestSample.remote.model.User
import com.example.gitRestSample.remote.model.UserDetail

class DataRepository {
    var ioDispatcher = Dispatchers.IO

    suspend fun searchUsers(q: String, page: Int): Result<SearchUserModel> {
        return withContext(ioDispatcher) {
            return@withContext try {
                Success(ApiService.getApiManager().searchUsers(q, page))
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