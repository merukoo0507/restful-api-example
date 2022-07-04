package com.example.gitRestSample.remote

import com.example.gitRestSample.remote.model.User
import com.example.gitRestSample.remote.model.UserDetail
import com.example.gitRestSample.util.Constants.token
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("search/users")
    suspend fun searchUsers(
        @Query("since") since: Int = 0,
        @Header("authorization") auth: String = token
    ): List<User>

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int = 0,
        @Header("authorization") auth: String = token
    ): List<User>


    @GET("user/repos")
    suspend fun getUserRepos(
        @Query("page") page: Int = 0
    ): List<User>

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") userName: String
        //,@Header("authorization") auth: String = token
    ): UserDetail

    companion object {
        private const val CONNECT_TIME_OUT = 30L
        private const val READ_TIME_OUT = 30L
        private const val WRITE_TIME_OUT = 30L
        private lateinit var apiManager: ApiService
        private fun create() : ApiService {
            val interceptor = Interceptor { chain ->
                val request = chain
                    ?.request()
                    ?.newBuilder()
                    ?.addHeader("Content-Type", "application/json")
                    ?.addHeader("User-Accept", "application/vnd.github.v3+json")
                    ?.build()
                chain?.proceed(request)
            }

            var okHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(FlipperOkhttpInterceptor(NetworkFlipperPlugin()))
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .build()

            var retrofit = Retrofit.Builder()
                    .client(okHttpClient)
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
