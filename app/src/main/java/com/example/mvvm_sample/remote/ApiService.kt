package com.example.mvvm_sample.remote

import android.util.Log
import com.example.mvvm_sample.Constants.USER_LINIT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import timber.log.Timber

interface ApiService {
    @GET("user/repos")
    suspend fun getUsers(
        @Query("page") page: Int = 0,
        @Query("per_page") limit: Int = USER_LINIT
    ): List<User>


    @GET("user/repos")
    suspend fun getUserRepos(
        @Query("page") page: Int = 0,
        @Query("per_page") limit: Int = USER_LINIT,
        @HeaderMap headerMap: Map<String, String>
    ): List<User>

    @GET("users/:{username}")
    suspend fun getUser(@Path("username") userName: String): User

    companion object {
        private lateinit var apiManager: ApiService
        private fun create() : ApiService {
            val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
                override fun log(message: String) {
                    Timber.i(message)
                }
            })
            val interceptor = Interceptor { chain ->
                val request = chain
                    ?.request()
                    ?.newBuilder()
                    ?.addHeader("Content-Type", "application/json")
                    ?.addHeader("Accept:", "application/vnd.github.v3+json")
                    ?.build()
                chain?.proceed(request)
            }
            var okHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(logging)
                .build()
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
