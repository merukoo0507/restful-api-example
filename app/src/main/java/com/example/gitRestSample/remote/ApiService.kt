package com.example.gitRestSample.remote

import com.example.gitRestSample.remote.model.User
import com.example.gitRestSample.remote.model.UserDetail
import com.example.gitRestSample.util.Constants.USER_LINIT
import com.example.gitRestSample.util.Constants.token
import com.example.gitRestSample.util.FlipperHelper
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
//import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("since") page: Int = 0,
        @Query("per_page") limit: Int = USER_LINIT
    ): List<User>


    @GET("user/repos")
    suspend fun getUserRepos(
        @Query("page") page: Int = 0,
        @Query("per_page") limit: Int = USER_LINIT
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
                    ?.addHeader("User-Accept", "application/vnd.github.v3+json")
                    ?.build()
                chain?.proceed(request)
            }

            var okHttpClient = OkHttpClient().newBuilder()
//                .addInterceptor(StethoInterceptor())
                .addInterceptor(interceptor)
                .addNetworkInterceptor(FlipperOkhttpInterceptor(FlipperHelper.networkPlugin))
                .addNetworkInterceptor(logging)
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
