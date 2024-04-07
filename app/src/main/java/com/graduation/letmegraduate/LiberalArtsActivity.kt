package com.graduation.letmegraduate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.graduation.letmegraduate.databinding.ActivityLiberalartsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiberalArtsActivity: AppCompatActivity() {
    private val selectedRows: MutableList<Map<String, Any>> = mutableListOf()
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLiberalartsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_liberalarts)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar) //toolbar를 액션바로 지정
        val actionbar = supportActionBar
        actionbar!!.setDisplayShowTitleEnabled(false)
        actionbar.setDisplayHomeAsUpEnabled(true) // 액션바에 뒤로가기 버튼 추가

        val service = RetrofitInstance.apiService

        val liberalArtsSubjectList = mutableListOf<Map<String, Any>>()

        service.getSemesterList().enqueue(object : Callback<GetSemesterListResponse> {
            override fun onResponse(
                call: Call<GetSemesterListResponse>,
                response: Response<GetSemesterListResponse>,
            ) {
                val semesterList = response.body()?.semester

                var totalLachoiceCredit = 0 // lachoceCredit: 교양선택 학점

                val adapter = ArrayAdapter(
                    this@LiberalArtsActivity,
                    android.R.layout.simple_list_item_1,
                    semesterList as MutableList<*>
                )
                binding.listSemester.setAdapter(adapter)

                binding.listSemester.onItemClickListener =
                    object : AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?, view: View?, position: Int, id: Long,
                        ) {
                            TODO("Not yet implemented")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                        override fun onItemClick(
                            parent: AdapterView<*>?, view: View?, position: Int, id: Long,
                        ) {
                            val minorCredit = binding.minorCredit.text.toString()
                            if (minorCredit.isNotEmpty()) {
                                totalLachoiceCredit += minorCredit.toInt()
                            }
                            binding.minorCredit.text = null

                            val selectSemester = parent?.getItemAtPosition(position).toString()
                            val data = SelesctedSemester(selectSemester)

                            val tableLayout = binding.tableLayout

                            var cnt = 0

                            service.getLASubjectsList(data).enqueue(object : Callback<JsonObject> {
                                override fun onResponse(
                                    call: Call<JsonObject>,
                                    response: Response<JsonObject>,
                                ) {
                                    if (response.isSuccessful) {
                                        binding.layout.visibility = View.VISIBLE
                                        binding.scrollView.visibility = View.VISIBLE

                                        liberalArtsSubjectList.clear()
                                        val lafoundamentalList = response.body()?.get("foundamental")
                                        val laneedList = response.body()?.get("need")

                                        // JsonElement를 List<Map<String, Any>>로 변환
                                        if (lafoundamentalList!!.isJsonArray) {
                                            val jsonArray = lafoundamentalList.asJsonArray.apply {
                                                addAll(laneedList?.asJsonArray)
                                            }
                                            for (element in jsonArray) {
                                                val mapType =
                                                    object : TypeToken<Map<String, Any>>() {}.type
                                                val jsonObject = Gson().fromJson<Map<String, Any>>(element, mapType)

                                                // 필드의 값이 실수로 들어가 있을 경우 정수로 변환
                                                val dataList = mutableMapOf<String, Any>()
                                                for ((key, value) in jsonObject) {
                                                    val newValue = if ((key == "grade" || key == "credit") && value is Double) {
                                                        value.toInt()
                                                    } else {
                                                        value
                                                    }
                                                    dataList[key] = newValue
                                                }
                                                liberalArtsSubjectList.add(dataList)
                                            }
                                        }

                                        tableLayout.removeAllViews()


                                        for (map in liberalArtsSubjectList) {
                                            // 새로운 TableRow 생성
                                            val tableRow = TableRow(this@LiberalArtsActivity)

                                            val button = CheckBox(this@LiberalArtsActivity)
                                            // 버튼을 TableRow에 추가
                                            tableRow.addView(button)

                                            for ((key, value) in map) {
                                                when (key) {
                                                    "grade", "c_area", "credit", "sub_name" -> {
                                                        // 공백을 추가하는 TextView 생성
                                                        val spaceTextView = TextView(this@LiberalArtsActivity)
                                                        spaceTextView.text = "      "

                                                        val textView =
                                                            TextView(this@LiberalArtsActivity)
                                                        textView.text = value.toString()


                                                        // TableRow에 공백 TextView와 TextView를 번갈아가며 추가
                                                        tableRow.addView(spaceTextView)
                                                        tableRow.addView(textView)
                                                    }
                                                }
                                            }
                                            // 테이블 레이아웃에 TableRow 추가
                                            tableLayout.addView(tableRow)

                                            // 체크박스 클릭 이벤트 설정
                                            button.setOnCheckedChangeListener { _, isChecked ->
                                                if (isChecked) {
                                                    selectedRows.add(map)

                                                    cnt++
                                                    if ( cnt == 1 )
                                                        count += cnt

                                                    val message = "현재 수강한 교양 과목을 선택한 학기: $count/${semesterList.size}"
                                                    binding.textView.text = message
                                                } else {
                                                    selectedRows.remove(map)

                                                    cnt--
                                                    if (cnt == 0)
                                                        count--
                                                }
                                            }
                                        }
                                    } else
                                        Log.e("err", "상태코드: ${response.code()}")

                                }

                                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                    Log.e("fail", "${t.message}")
                                }

                            })

                        }

                    }

                binding.okBtn.setOnClickListener {
                    val minorCredit = binding.minorCredit.text.toString()
                    if (minorCredit.isNotEmpty()) {
                        totalLachoiceCredit += minorCredit.toInt()
                    }

                    val data = SelectedLASubjects(selectedRows, totalLachoiceCredit)

                    if (count >= 7) {
                        service.putSelectedLASubjects(data).enqueue(object : Callback<JsonObject> {
                            override fun onResponse(
                                call: Call<JsonObject>,
                                response: Response<JsonObject>,
                            ) {
                                if (response.isSuccessful) {
                                    val laRequirementSatisfacionStatus = response.body()?.get("state")?.asBoolean
                                    val laCreditRSS = response.body()?.get("checkState")?.asBoolean
                                    val lafSubjectRegistrationStatus = response.body()?.get("sFoundamentalList")?.asBoolean
                                    val lanSubjectRegistrationStatus = response.body()?.get("sNeedList")?.asBoolean
                                    val laCredit = response.body()?.get("s_score")?.asInt
                                    val lafSubjectList = response.body()!!.get("s_fundamental_list")
                                    val lanSubjectList = response.body()!!.get("s_need_list")

                                    if (!laRequirementSatisfacionStatus!!) {
                                        binding.noticeLayout.visibility = View.VISIBLE

                                        val text = buildString {
                                            if(!laCreditRSS!!) {
                                                append(" ○ 최소 이수 학점까지 남은 교양 학점: ${30 - laCredit!!}")
                                                append("\n\n")
                                            }
                                            if (!lafSubjectRegistrationStatus!!){
                                                append(" ○ 미수강한 기초교양 과목: $lafSubjectList")
                                                append("\n\n")
                                            }
                                            if (!lanSubjectRegistrationStatus!!){
                                                append(" ○ 교양필수는 3개 이수영역 이상 이수해야 합니다.\n")
                                                append(" ○ 미수강한 교양필수 과목: $lanSubjectList")
                                            }
                                        }
                                        binding.noticeMassage.text = text
                                    }
                                }
                            }
                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                Log.e("fail", "${t.message}")
                            }
                        })
                    }
                    else {
                        AlertDialog.Builder(this@LiberalArtsActivity)
                            .setMessage("학기별로 수강한 교양 과목을 다 선택해야 합니다.")
                            .setPositiveButton("확인", null)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<GetSemesterListResponse>, t: Throwable) {
                Log.e("fail", "${t.message}")
            }
        })
    }



    override fun onSupportNavigateUp(): Boolean {
        //뒤로가기 버튼 클릭 시 화면 이동
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        return true
    }
}