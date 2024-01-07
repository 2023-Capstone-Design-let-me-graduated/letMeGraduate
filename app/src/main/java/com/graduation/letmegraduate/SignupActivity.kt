package com.graduation.letmegraduate

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        val items = resources.getStringArray(R.array.semester)
        val checked = Array(10){false}
        //회원가입 버튼 선택 시 재학학기 선택 팝업 띄우기
        binding.signupBtn.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("재학학기를 선택해주세요")
                .setMultiChoiceItems(items,null) {_, which, isChecked ->
                    // 선택된 항목을 배열에 저장
                    checked[which] = isChecked
                }
                .setPositiveButton("취소", null)
                .setNegativeButton("확인") {_, _ ->
                    val sb = StringBuffer()
                    for (i in checked.indices) {
                        if (checked[i]) {
                            // 확인 버튼을 누르면 선택된 항목(재학학기)을 스트링 배열에 추가함
                            sb.append("${items[i]}, ")
                        }
                    }
                    Toast.makeText(applicationContext, "$sb", Toast.LENGTH_SHORT).show()
                }
                .setCancelable(false)
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        //(액션바의)뒤로가기 버튼 클릭 시 로그인 화면으로 이동
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        return true
    }
}
