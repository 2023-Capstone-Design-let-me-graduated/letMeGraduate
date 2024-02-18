package com.graduation.letmegraduate

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("/signup")
    fun emailAuthentication(
        @Query("email") email: String
    ): Call<EmailResponse>
    @POST("/signup")
    fun userSignup(
        @Body signup: Signup
    ): Call<Void>
    @POST("/login")
    fun userLogin(
        @Body login: Login
    ): Call<Void>
}

data class EmailResponse(
    val authenticationCode: String
)
data class Signup(
    val userid: String,
    val password: String,
    val major: String,
    val email: String,
    val semester: List<String>
)

data class Login(
    val userid: String,
    val password: String
)