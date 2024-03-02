package com.graduation.letmegraduate

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.graduation.letmegraduate.databinding.ActivityGeneralelectiveBinding
import retrofit2.Call
import retrofit2.Callback

class GeneralElectiveActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityGeneralelectiveBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_generalelective)

        val service = RetrofitInstance.apiService

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar) //toolbar를 액션바로 지정
        val actionbar = supportActionBar
        actionbar!!.setDisplayShowTitleEnabled(false)
        actionbar.setDisplayHomeAsUpEnabled(true) // 액션바에 뒤로가기 버튼 추가

        binding.okBtn.setOnClickListener {
            val generalElectiveCredit = binding.generalelectiveCredit.text.toString()

            if (!TextUtils.isEmpty(generalElectiveCredit)) {
                val data = GeneralElectivieCredit(generalElectiveCredit.toInt())

                service.putCredit(data).enqueue(object: Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                        if (!response.isSuccessful) {
                            // HTTP 상태코드 404,503 등 오류가 발생한 경우
                            Log.e("err", "상태코드: ${response.code()}")
                            Toast.makeText(this@GeneralElectiveActivity,
                                "오류가 발생했습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // 서버와 응답에 실패한 경우
                        Log.e("fail","${t.message}")
                        Toast.makeText(this@GeneralElectiveActivity,
                            "오류가 발생했습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
            else {
                AlertDialog.Builder(this)
                    .setMessage("취득한 총 학점을 입력하지 않았습니다.\n취득 학점을 입력해주세요.")
                    .setPositiveButton("확인", null)
                    .show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        //뒤로가기 버튼 클릭 시 화면 이동
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        return true
    }
}