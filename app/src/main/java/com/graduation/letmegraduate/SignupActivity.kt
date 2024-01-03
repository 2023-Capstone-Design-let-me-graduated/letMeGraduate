package com.graduation.letmegraduate

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.graduation.letmegraduate.databinding.ActivitySignupBinding


class SignupActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySignupBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_signup)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar) //toolbar를 액션바로 지정
        val actionbar = getSupportActionBar()
        actionbar!!.setDisplayShowTitleEnabled(false)
        actionbar.setDisplayHomeAsUpEnabled(true) // 액션바에 뒤로가기 버튼 추가

        val school_record = binding.schoolRecord
        // dropdown menu에 표시할 항목들을 가져옴
        val item : Array<String> = resources.getStringArray(R.array.dropdown_item)
        // ArrayAdapter를 사용하여 dropdown menu에 항목들을 연결
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,item)
        school_record.setAdapter(adapter)
    }

    override fun onSupportNavigateUp(): Boolean {
        //(액션바의)뒤로가기 버튼 클릭 시 로그인 화면으로 이동
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        return true
    }
}
