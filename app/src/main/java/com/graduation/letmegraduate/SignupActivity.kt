package com.graduation.letmegraduate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.graduation.letmegraduate.databinding.ActivityLoginBinding

class SignupActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val actionbar = supportActionBar
        
        actionbar!!.setTitle(R.string.signup) // 액션바 제목을 '회원가입'으로 바꾸기
        actionbar!!.setDisplayHomeAsUpEnabled(true) // 액션바에 뒤로가기 버튼 추가
    }

    override fun onSupportNavigateUp(): Boolean {
        //(액션바의)뒤로가기 버튼 클릭 시 로그인 화면으로 이동
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        return true
    }
}