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
    private lateinit var id: String
    private lateinit var pw: String
    private var authenticationStatus = false // authentication_status : 이메일 인증여부 확인을 위한 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        // Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("https://port-0-letmegraduated-server-17xco2nlspr2wdq.sel5.cloudtype.app/")
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
            id = binding.signupId.text.toString()
            pw = binding.signupPw.text.toString()

            if(!TextUtils.isEmpty(id) && !TextUtils.isEmpty(pw) && authenticationStatus) {
                //재학학기 선택 팝업 띄우기
                AlertDialog.Builder(this)
                    .setTitle("재학학기를 선택해주세요")
                    .setMultiChoiceItems(items,null) {_, which, isChecked ->
                        // 항목의 선택 여부를 배열에 저장
                        checked[which] = isChecked
                    }
                    .setPositiveButton("확인") {_, _ ->
                        if (checked.count { it } >= 8) {
                            val list: MutableList<String> = mutableListOf()
                            for (i in checked.indices) {
                                if (checked[i]) {
                                    // 선택된 항목(재학학기)을 배열에 추가함
                                    val str = items[i].substring(0,4) + "_" + items[i].substring(6,7)
                                    list.add(str)
                                }
                            }

                            val userData = Signup(id, pw, binding.schoolRecord.text.toString(), email, list)
                            apiService.userSignup(userData).enqueue(object: Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                                    if (response.isSuccessful) { // 서버와 응답에 성공한 경우
                                        Toast.makeText(applicationContext, "회원가입에 성공했습니다",
                                            Toast.LENGTH_SHORT).show()
                                        // 로그인 화면으로 이동
                                        intent = Intent(this@SignupActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                    }
                                    else
                                        Toast.makeText(applicationContext, "오류가 발생했습니다.\n" +
                                                "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    // 서버와 응답에 실패한 경우
                                    AlertDialog.Builder(this@SignupActivity)
                                        .setMessage("회원가입에 실패했습니다.\n다시 시도해주세요.")
                                        .setPositiveButton("확인", null)
                                        .show()

                                }
                            })
                        }
                        else
                            Toast.makeText(applicationContext, "재학학기를 8개 선택해야 합니다.\n" +
                                    "회원가입이 아직 완료되지 않았습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("취소", null)
                    .setCancelable(false)
                    .show()
            }
            else {
                if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pw))
                    AlertDialog.Builder(this)
                        .setMessage("학번 또는 비밀번호를 입력하지 않았습니다.\n학번 또는 비밀번호를 입력해주세요.")
                        .setPositiveButton("확인", null)
                        .show()

                else if(authenticationStatus)
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
