package com.boilerplate.data.network

import com.google.gson.JsonObject
import com.boilerplate.BuildConfig
import com.boilerplate.data.network.responses.AuthResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface MyApi {



    @Headers("Accept:application/json")
    @POST("register")
    suspend fun userSignup(
        @Body request: JsonObject
    ): Response<AuthResponse>


    @POST("connection")
    suspend fun userLogin(
        @Body request: JsonObject
    ): Response<AuthResponse>

    @POST("register/check")
    suspend fun userLoginUsingSocial(
        @Body request: JsonObject
    ): Response<AuthResponse>

    @POST("register/social")
    suspend fun userSignupUsingSocial(
        @Body request: JsonObject
    ): Response<AuthResponse>


    companion object{
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : MyApi {

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(networkConnectionInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}



