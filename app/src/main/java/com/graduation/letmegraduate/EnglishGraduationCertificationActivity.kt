package com.graduation.letmegraduate

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.graduation.letmegraduate.databinding.ActivityEnglishGraduationCertificationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnglishGraduationCertificationActivity : Fragment() {
    private lateinit var binding: ActivityEnglishGraduationCertificationBinding
    private lateinit var standardScore: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityEnglishGraduationCertificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val service = RetrofitInstance.apiService

        val kind = binding.certificateKind
        // dropdown menu에 표시할 항목들을 가져옴
        val item : Array<String> = resources.getStringArray(R.array.certificate_kind)
        // ArrayAdapter를 사용하여 dropdown menu에 항목들을 연결
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, item)
        kind.setAdapter(adapter)

        // 확인 버튼 클릭 시
        binding.okBtn.setOnClickListener {
            val certificateKind = kind.text.toString()
            var userScore = binding.certificateScore.text.toString()

            // 영어자격증 종류가 OPIC일 때, 사용자가 점수를 소문자로 입력했다면 대문자로 변환함
            if (certificateKind == "OPIC" && userScore == userScore.lowercase())
                userScore = userScore.uppercase()

            val data = Exam(certificateKind, userScore)

            if (certificateKind != "================" && !TextUtils.isEmpty(userScore)) {
                service.exam(data).enqueue(object : Callback<ExamResponse> {
                    override fun onResponse(call: Call<ExamResponse>, response: Response<ExamResponse>) {
                        if (response.isSuccessful) { // 서버와 응답에 성공한 경우
                            val satisfactionStatus = response.body()?.check!!
                            if (satisfactionStatus)
                                AlertDialog.Builder(requireActivity())
                                    .setMessage("영어졸업인증 조건에 충족되었습니다.\n 학교 포탈에서 " +
                                            "영어졸업인증 신청해주세요.\n\n" +
                                            "포탈 신청절차 : 통합정보시스템 > 학사행정 > 졸업 > 영어인증관리 > 영어졸업인증신청")
                                    .setPositiveButton("확인", null)
                                    .show()
                            else {
                                standardScore = if (certificateKind == "OPIC") {
                                    response.body()?.condition?.get(0).toString()
                                } else {
                                    response.body()?.condition.toString().substring(1, 4)
                                }

                                AlertDialog.Builder(requireActivity())
                                    .setMessage("성적이 영어졸업인증 자격기준 점수보다 부족하여 조건에 충족되지 않았습니다.\n" +
                                            "\n${certificateKind}은 $standardScore 이상 " +
                                            "받어야 영어졸업인증 조건에 충족됩니다.")
                                    .setPositiveButton("확인", null)
                                    .show()
                            }
                        }
                        else {
                            Log.e("err","상태코드: ${response.code()}")
                            Toast.makeText(requireActivity(), "오류가 발생했습니다.\n다시 시도해주세요.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ExamResponse>, t: Throwable) {
                        Log.e("fail","${t.message}")
                        // 서버와 응답에 실패한 경우
                        AlertDialog.Builder(requireActivity())
                            .setMessage("오류가 발생했습니다.\n다시 시도해주세요.")
                            .setPositiveButton("확인", null)
                            .show()
                    }
                })
            }
            else {
                if (certificateKind == "================")
                    Toast.makeText(requireActivity(), "영어자격증 종류를 선택해주세요.",
                        Toast.LENGTH_SHORT).show()
                else
                    AlertDialog.Builder(requireActivity())
                        .setMessage("영어자격증 성적을 입력하지 않았습니다.\n성적을 입력해주세요.")
                        .setPositiveButton("확인", null)
                        .show()
            }
        }
    }
}