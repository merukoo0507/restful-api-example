package com.example.restful_api_example.remote

import com.example.restful_api_example.MainApplication.Companion.networkFlipperPlugin
import com.example.restful_api_example.remote.model.SearchUserModel
import com.example.restful_api_example.remote.model.User
import com.example.restful_api_example.remote.model.UserDetail
import com.example.restful_api_example.util.Constants.USER_NUM_PER_PAGE
import com.example.restful_api_example.util.Constants.token
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") q: String,
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = USER_NUM_PER_PAGE,
         @Header("authorization") auth: String = token
    ): SearchUserModel

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int = 0,
        @Query("per_page") limit: Int = USER_NUM_PER_PAGE
//        @Header("authorization") auth: String = token
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
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

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
                .addInterceptor(logging)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin))
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.github.com/")     //直接查訊link可以知道可用的api
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
                    .create(ApiService::class.java)
        }

        fun getApiManager(): ApiService {
            if (!::apiManager.isInitialized) {
                apiManager = create()
            }
            return apiManager
        }
    }
}
