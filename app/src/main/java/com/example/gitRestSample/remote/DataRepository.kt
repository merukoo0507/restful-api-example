package com.example.gitRestSample.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import com.example.gitRestSample.remote.Result.*
import com.example.gitRestSample.remote.model.User
import com.example.gitRestSample.remote.model.UserDetail

class DataRepository {
    var ioDispatcher = Dispatchers.IO

    suspend fun getUsers(since: Int): Result<List<User>> {
        return withContext(ioDispatcher) {
            return@withContext try {
                Success(ApiService.getApiManager().getUsers(since))
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