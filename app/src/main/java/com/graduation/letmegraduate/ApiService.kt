package com.graduation.letmegraduate

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

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
    @GET("/major/semester")
    fun getSemesterList(): Call<GetSemesterListResponse>
    @POST("/major/semester")
    fun get(
        @Body selesctedSemester: SelesctedSemester
    ): Call<JsonObject>
    @PUT("/major")
    fun majorput(
        @Body selectedMajorSubjects: SelectedMajorSubjects
    ): Call<JsonObject>
    @PUT("/normal")
    fun putCredit(
        @Body generalElectivieCredit: GeneralElectivieCredit
    ): Call<Void>
    @POST("/exam")
    fun exam(
        @Body exam: Exam
    ): Call<ExamResponse>
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

data class GetSemesterListResponse(
    val semester: List<*>
)

data class SelesctedSemester(
    val selectedSemester: String
)

data class SelectedMajorSubjects(
    val list: List<*>
)

data class GeneralElectivieCredit(
    val score: Int
)

data class Exam(
    val testType: String,
    val score: String
)

data class ExamResponse(
    val check: Boolean,
    val condition: List<*>
)





