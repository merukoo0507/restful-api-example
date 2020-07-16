package com.example.mvvm_sample.remote

import android.util.Log
import com.example.mvvm_sample.Constants.USER_LINIT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber

interface ApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int = 0,
        @Query("per_page") limit: Int = USER_LINIT
    ): List<User>

    @GET("users/:{username}")
    suspend fun getUser(@Path("username") userName: String): User

    companion object {
        private lateinit var apiManager: ApiService
        private fun create() : ApiService {
            var logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
                override fun log(message: String) {
                    Timber.i(message)
                }
            })
            var okHttpClient = OkHttpClient().newBuilder().addInterceptor(logging).build()
            var retrofit = Retrofit.Builder()
                                .baseUrl("https://api.github.com/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .client(okHttpClient)
                                .build()
            return retrofit.create(ApiService::class.java)
        }

        fun getApiManager(): ApiService {
            if (!::apiManager.isInitialized) {
                apiManager = create()
            }
            return apiManager
        }
    }
}
