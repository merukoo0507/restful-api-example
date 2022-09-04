package com.example.restful_api_example.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import com.example.restful_api_example.remote.Result.*
import com.example.restful_api_example.remote.model.SearchUserModel
import com.example.restful_api_example.remote.model.User
import com.example.restful_api_example.remote.model.UserDetail

object DataRepository {
    suspend fun getUsers(since: Int): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                Success(ApiService.getApiManager().getUsers(since))
            } catch (e: Exception) {
                Error(e)
            }
        }
    }

    suspend fun getUserRepos(page: Int): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                Success(ApiService.getApiManager().getUserRepos(page))
            } catch (e: Exception) {
                Error(e)
            }
        }
    }

    suspend fun searchUsers(q: String, page: Int): Result<SearchUserModel> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                Success(ApiService.getApiManager().searchUsers(q, page))
            } catch (e: Exception) {
                Error(e)
            }
        }
    }

    suspend fun getUser(name: String): Result<UserDetail> {
        return withContext(Dispatchers.IO) {
            return@withContext  try {
                Success(ApiService.getApiManager().getUser(name))
            } catch (e: Exception) {
                Error(e)
            }
        }
    }
}