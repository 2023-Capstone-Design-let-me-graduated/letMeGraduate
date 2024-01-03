package com.graduation.letmegraduate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.graduation.letmegraduate.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {
    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


       val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)

        // 회원가입 버튼 클릭시 회원가입 화면으로 이동
        binding.signupBtn.setOnClickListener {
            intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼 클릭시 메인화면으로 이동
        binding.loginBtn.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}