package com.graduation.letmegraduate

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.graduation.letmegraduate.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class SignupActivity: AppCompatActivity() {
    private lateinit var code: String
    private lateinit var email: String
    private var authenticationStatus = false // authentication_status : 이메일 인증여부 확인을 위한 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySignupBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_signup)

        // Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar) //toolbar를 액션바로 지정
        val actionbar = supportActionBar
        actionbar!!.setDisplayShowTitleEnabled(false)
        actionbar.setDisplayHomeAsUpEnabled(true) // 액션바에 뒤로가기 버튼 추가

        val schoolRecord = binding.schoolRecord
        // dropdown menu에 표시할 항목들을 가져옴
        val item : Array<String> = resources.getStringArray(R.array.dropdown_item)
        // ArrayAdapter를 사용하여 dropdown menu에 항목들을 연결
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,item)
        schoolRecord.setAdapter(adapter)

        // (이메일) 인증하기 버튼 클릭 시
        binding.authenticationBtn.setOnClickListener {
            if (!TextUtils.isEmpty(binding.email.text.toString())) {
                email = binding.email.text.toString() + binding.domainName.text.toString()
                apiService.emailAuthentication(email).enqueue(object: Callback<EmailResponse> {
                    override fun onResponse(call: Call<EmailResponse>, response: retrofit2.Response<EmailResponse>) {
                        if (response.isSuccessful) { // 서버와 응답에 성공한 경우
                            AlertDialog.Builder(this@SignupActivity)
                                .setMessage("입력하신 이메일로 인증코드를 전송하였습니다.\n인증코드를 입력해주세요")
                                .setPositiveButton("확인", null)
                                .show()

                            binding.confrimAuthentication.visibility = View.VISIBLE // 인증코드 입력란 보이게 하기
                            code = response.body()?.authenticationCode.toString()
                        }
                        binding.authenticationBtn.text = "인증코드 재전송"
                    }

                    override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                        // 서버와 응답에 실패한 경우
                        when(t) {
                            is IOException -> {
                                AlertDialog.Builder(this@SignupActivity)
                                    .setMessage("네트워크 연결이 불안정합니다.\n네트워크 연결 상태를 확인 후 다시 시도해주세요")
                                    .setPositiveButton("확인", null)
                                    .show()
                            }
                            is HttpException -> { // 404 error 등 오류가 발생한 경우
                                AlertDialog.Builder(this@SignupActivity)
                                    .setMessage("오류가 발생했습니다. 다시 시도해주세요.")
                                    .setPositiveButton("확인", null)
                                    .show()

                                binding.authenticationBtn.text = "인증코드 재전송"
                            }
                        }
                    }
                })
            }

            else {
                // 이메일을 입력하지 않은 경우
                Toast.makeText(applicationContext, "이메일을 입력한 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show()
            }

        }

        // (인증코드) 확인 버튼 클릭 시
        binding.confrimAuthentication.setOnClickListener {
            if (binding.inputCode.text.toString() == code) {
                // 사용자가 입력한 코드랑 인증코드가 일치하는 경우
                authenticationStatus = true
                Toast.makeText(applicationContext, "이메일이 인증되었습니다.",Toast.LENGTH_SHORT).show()
            }
            else {
                AlertDialog.Builder(this)
                    .setMessage("인증코드가 일치하지 않습니다. 다시 시도해주세요.")
                    .setPositiveButton("확인", null)
                    .show()
            }
        }

        val items = resources.getStringArray(R.array.semester)
        val checked = Array(10){false}
        // 회원가입 버튼 클릭 시
        binding.signupBtn.setOnClickListener {
            if(authenticationStatus) {
                //이메일 인증을 완료했을 경우 재학학기 선택 팝업 띄우기
                AlertDialog.Builder(this)
                    .setTitle("재학학기를 선택해주세요")
                    .setMultiChoiceItems(items,null) {_, which, isChecked ->
                        // 선택된 항목을 배열에 저장
                        checked[which] = isChecked
                    }
                    .setPositiveButton("확인") {_, _ ->
                        val sb = StringBuffer()
                        for (i in checked.indices) {
                            if (checked[i]) {
                                // 확인 버튼을 누르면 선택된 항목(재학학기)을 스트링 배열에 추가함
                                sb.append("${items[i]}, ")
                            }
                        }
                    }
                    .setNegativeButton("취소", null)
                    .setCancelable(false)
                    .show()
            }
            else {
                AlertDialog.Builder(this)
                    .setMessage("이메일이 인증되지 않았습니다.\n인증을 완료해주세요.")
                    .setPositiveButton("확인", null)
                    .show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        //(액션바의)뒤로가기 버튼 클릭 시 로그인 화면으로 이동
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        return true
    }
}
