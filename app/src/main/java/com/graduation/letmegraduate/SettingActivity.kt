package com.graduation.letmegraduate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.graduation.letmegraduate.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySettingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_setting)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar) //toolbar를 액션바로 설정
        val actionbar = getSupportActionBar()
        actionbar!!.setDisplayShowTitleEnabled(false)
        actionbar.setDisplayHomeAsUpEnabled(true) // 액션바에 뒤로가기 버튼 추가

        binding.withdrawalBtn.setOnClickListener {
            // 탈퇴 버튼 클릭 시 탈퇴 여부 묻는 팝업 띄우기
            AlertDialog.Builder(this)
                .setTitle("탈퇴하시겠습니까?")
                .setIcon(R.drawable.ic_warning)
                .setMessage(R.string.withdrawal_message)
                .setPositiveButton("취소", null)
                .setNegativeButton("탈퇴", null)
                .setCancelable(false)
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        //(액션바의)뒤로가기 버튼 클릭 시 메인 화면으로 이동
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        return true
    }
}