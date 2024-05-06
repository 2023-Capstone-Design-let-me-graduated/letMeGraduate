package com.graduation.letmegraduate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.graduation.letmegraduate.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : Fragment() {
    private lateinit var binding: ActivityHomeBinding
    private val service = RetrofitInstance.apiService
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.applicationBtn.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setMessage("학교 포탈에서 영어졸업인증 신청하셨습니까?")
                .setPositiveButton("예") { _, _ ->
                    val data = ApplicationStatus(true)
                    service.putApplicationStatus(data).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                sendRequestToServer()
                            }
                            else{
                                Log.e("err", "상태코드: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("fail", "${t.message}")
                        }
                    })
                }
                .setNegativeButton("아니오") { _, _ ->
                    AlertDialog.Builder(requireActivity())
                        .setMessage("신청을 하셔야 졸업 요건에 충족됩니다.\n" +
                                "학교 포탈에서 영어졸업인증 신청해주세요.\n\n\""+
                                "신청절차 :학교 포탈 > 통합정보 > 학사행정 > 졸업 > 영어인증관리 > 영어졸업인증신청\"")
                        .setPositiveButton("확인",null)
                        .show()
                }
                .show()
        }
    }

    override fun onResume() {
        super.onResume()

        sendRequestToServer()
    }

    fun sendRequestToServer() {
        service.getData().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful) {
                    val graduationStatus = response.body()?.get("state")!!.asBoolean
                    val totalCredit = response.body()?.get("score")!!.asInt // 총 학점
                    val laberalartsCredit = response.body()?.get("s_score")!!.asInt // 교양 학점
                    val majorCredit = response.body()?.get("m_score")!!.asInt // 전공 학점
                    val majorneedCredit = response.body()?.get("m_need_score")!!.asInt // 전공필수 학점
                    val scoreSatisfacionStatus = response.body()?.get("eng_comparison")!!.asBoolean
                    val applicationStatus = response.body()?.get("engcheck")!!.asBoolean

                    var minimumCompletionCredit: Int
                    val mCCSatisfacion = "0"

                    binding.graduationstatus.text = if (graduationStatus) "졸업 가능" else "졸업 불가능"

                    binding.totalcredit.text = totalCredit.toString()
                    minimumCompletionCredit = binding.graduationCredit.text.toString().toInt()
                    binding.remainingcredit.text =
                        if (minimumCompletionCredit - totalCredit > 0)
                                (minimumCompletionCredit - totalCredit).toString()
                        else mCCSatisfacion

                    binding.leberalartisCredit.text = laberalartsCredit.toString()
                    minimumCompletionCredit = binding.minimumLACompletionCredit.text.toString().toInt()
                    binding.remainingLACredit.text =
                        if (minimumCompletionCredit - laberalartsCredit > 0)
                                (minimumCompletionCredit - laberalartsCredit).toString()
                        else mCCSatisfacion

                    binding.majorCredit.text = majorCredit.toString()
                    minimumCompletionCredit = binding.minimumMCompletionCredit.text.toString().toInt()
                    binding.remainingMajorCredit.text =
                        if (minimumCompletionCredit - majorCredit > 0)
                                (minimumCompletionCredit - majorCredit).toString()
                        else mCCSatisfacion

                    binding.majorneedCredit.text = majorneedCredit.toString()
                    minimumCompletionCredit = binding.minimumMNCompletionCredit.text.toString().toInt()
                    binding.remainingMNCredit.text =
                        if (minimumCompletionCredit - majorneedCredit > 0)
                                (minimumCompletionCredit - majorneedCredit).toString()
                        else mCCSatisfacion

                    binding.scoreSatisfacionStatus.text = if (scoreSatisfacionStatus) "충족" else "미충족"
                    binding.applicationStatus.text = if (applicationStatus) "신청함" else "신청 안함"
                }

                else{
                    Log.e("err", "상태코드: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("fail", "${t.message}")
            }
        })
    }

}