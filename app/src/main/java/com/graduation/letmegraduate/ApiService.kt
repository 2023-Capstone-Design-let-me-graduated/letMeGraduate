package com.graduation.letmegraduate

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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
    @FormUrlEncoded
    @POST("/login")
    fun userLogin(
        @Field("password") password: String,
        @Field("username") username: String
    ): Call<Void>
    @GET("/logout")
    fun userLogout(): Call<Void>
    @DELETE("/user")
    fun userDelete(): Call<Void>
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


