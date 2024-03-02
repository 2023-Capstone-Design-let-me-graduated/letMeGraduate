package com.graduation.letmegraduate

import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .cookieJar(JavaNetCookieJar(CookieManager()))
        .build()

    // Retrofit 객체 생성
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://port-0-letmegraduated-server-17xco2nlswrug2s.sel5.cloudtype.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}