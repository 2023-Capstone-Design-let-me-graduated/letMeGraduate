package com.graduation.letmegraduate

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/signup/email")
    fun emailAuthentication(
        @Body email: Email
    ): Call<ResponseBody>
    @POST("/signup")
    fun userSignup(
        @Body signup: Signup
    ): Call<Void>
    @POST("/login")
    fun userLogin(
        @Body login: Login
    ): Call<Void>
}

data class Email(
    val email: String
)
data class Signup(
    val userid: String,
    val password: String,
    val major: String,
    val email: String,
    val semester: List<String>
)
data class Login(
    val password: String,
    val userid: String
)