package com.graduation.letmegraduate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.graduation.letmegraduate.databinding.ActivitySettingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySettingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_setting)

        val service = RetrofitInstance.apiService

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar) //toolbar를 액션바로 설정
        val actionbar = supportActionBar
        actionbar!!.setDisplayShowTitleEnabled(false)
        actionbar.setDisplayHomeAsUpEnabled(true) // 액션바에 뒤로가기 버튼 추가

        // 탈퇴 버튼 클릭 시
        binding.withdrawalBtn.setOnClickListener {
            // 탈퇴 여부 묻는 팝업 띄우기
            AlertDialog.Builder(this)
                .setTitle("탈퇴하시겠습니까?")
                .setIcon(R.drawable.ic_warning)
                .setMessage(R.string.withdrawal_message)
                .setPositiveButton("탈퇴") { _, _ ->
                    service.userDelete().enqueue(object: Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) { // 서버와 응답에 성공한 경우
                                Toast.makeText(applicationContext, "회원 탈퇴가 완료되었습니다",
                                    Toast.LENGTH_SHORT).show()
                                // 로그인 화면으로 이동
                                intent = Intent(this@SettingActivity, LoginActivity::class.java)
                                startActivity(intent)
                            }
                            else {
                                Log.e("err","상태코드: ${response.code()}")
                                Toast.makeText(applicationContext,
                                    "오류가 발생했습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            // 서버와 응답에 실패한 경우
                            Log.e("fail","${t.message}")
                            Toast.makeText(applicationContext,
                                "오류가 발생했습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                .setNegativeButton("취소", null)
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