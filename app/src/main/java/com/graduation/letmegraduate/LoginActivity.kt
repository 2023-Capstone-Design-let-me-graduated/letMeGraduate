package com.graduation.letmegraduate

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.graduation.letmegraduate.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity: AppCompatActivity() {
    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)

        // Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("https://port-0-letmegraduated-server-17xco2nlspr2wdq.sel5.cloudtype.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)


        val data = Login(binding.loginId.text.toString(), binding.loginPw.text.toString())
        // 로그인 버튼 클릭시
        binding.loginBtn.setOnClickListener {
            if (!TextUtils.isEmpty(binding.loginId.text.toString()) && !TextUtils.isEmpty(binding.loginPw.text.toString())) {
                apiService.userLogin(data)
                    .enqueue(object: Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                        if (response.isSuccessful) { // 서버와 응답에 성공한 경우
                            // 메인화면으로 이동
                            intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                        else if (response.code() == 404) {
                            AlertDialog.Builder(this@LoginActivity)
                                .setMessage("회원 정보가 없습니다.\n" +
                                        "학번 또는 비밀번호를 잘못 입력하였는지 확인해주세요.\n" +
                                        "혹은 회원가입을 하지 않았을 경우 회원가입을 진행해주세요.")
                                .setPositiveButton("확인", null)
                                .show()
                        }
                        else
                            Toast.makeText(applicationContext,
                                "오류가 발생했습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // 서버와 응답에 실패한 경우
                        AlertDialog.Builder(this@LoginActivity)
                            .setMessage("로그인에 실패했습니다.\n다시 시도해주세요")
                            .setPositiveButton("확인", null)
                            .show()
                    }
                })
            }
            else {
                Toast.makeText(applicationContext,
                    "학번 또는 비밀번호를 입력하지 않았습니다.\n학번 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 버튼 클릭시 회원가입 화면으로 이동
        binding.signupBtn.setOnClickListener {
            intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

    }
}