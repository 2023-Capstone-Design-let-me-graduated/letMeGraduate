package com.graduation.letmegraduate

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/signup")
    fun emailAuthentication(
        @Query("email") email: String
    ): Call<EmailResponse>
}