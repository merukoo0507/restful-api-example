package com.example.mvvm_sample.remote

import com.example.mvvm_sample.remote.model.User
import com.example.mvvm_sample.remote.model.UserDetail
import com.example.mvvm_sample.util.Constants.USER_LINIT
import com.example.mvvm_sample.util.Constants.token
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import timber.log.Timber

interface ApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int = 0,
        @Query("per_page") limit: Int = USER_LINIT
    ): List<User>


    @GET("user/repos")
    suspend fun getUserRepos(
        @Query("page") page: Int = 0,
        @Query("per_page") limit: Int = USER_LINIT
    ): List<User>

    @GET("users/{username}")
    suspend fun getUser(@Path("username") userName: String): UserDetail

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
                    ?.addHeader("Authorization", "Bearer ${token}")
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
