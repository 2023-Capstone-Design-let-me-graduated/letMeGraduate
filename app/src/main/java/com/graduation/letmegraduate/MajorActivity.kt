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
import com.graduation.letmegraduate.databinding.ActivityMajorBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MajorActivity: AppCompatActivity() {
    private val selectedRows: MutableList<Map<String, Any>> = mutableListOf()
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: ActivityMajorBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_major)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar) //toolbar를 액션바로 지정
        val actionbar = supportActionBar
        actionbar!!.setDisplayShowTitleEnabled(false)
        actionbar.setDisplayHomeAsUpEnabled(true) // 액션바에 뒤로가기 버튼 추가

        val service = RetrofitInstance.apiService

        val majorSubjectList = mutableListOf<Map<String, Any>>()

        service.getSemesterList().enqueue(object : Callback<GetSemesterListResponse> {
            override fun onResponse(
                call: Call<GetSemesterListResponse>, response: Response<GetSemesterListResponse>,
            ) {
                val semesterList = response.body()?.semester

                val adapter = ArrayAdapter(
                    this@MajorActivity, android.R.layout.simple_list_item_1, semesterList as MutableList<*>
                )
                binding.listSemester.setAdapter(adapter)

                binding.listSemester.onItemClickListener =
                    object : AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long,
                        ) {
                            TODO("Not yet implemented")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                        override fun onItemClick(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long,
                        ) {
                            binding.scrollView.post {
                                binding.scrollView.scrollTo(0, 0)
                            }

                            val selectSemester = parent?.getItemAtPosition(position).toString()

                            val data = SelesctedSemester(selectSemester)

                            var cnt = 0

                            service.get(data).enqueue(object: Callback<JsonObject> {
                                override fun onResponse(call: Call<JsonObject>,
                                                        response: Response<JsonObject>,
                                ) {
                                    if (response.isSuccessful) {
                                        binding.layout.visibility = View.VISIBLE
                                        binding.subjectListView.visibility = View.VISIBLE

                                        majorSubjectList.clear()

                                        val majorfoundamentalList = response.body()?.get("foundamental")
                                        val majorneedList = response.body()?.get("need")
                                        val majorchoiceList = response.body()?.get("choice")

                                        // JsonElement를 List<Map<String, Any>>로 변환
                                        if (majorfoundamentalList!!.isJsonArray) {
                                            val jsonArray = majorfoundamentalList.asJsonArray.apply {
                                                addAll(majorneedList?.asJsonArray)
                                                addAll(majorchoiceList?.asJsonArray)
                                            }
                                            for (element in jsonArray) {
                                                val mapType = object : TypeToken<Map<String, Any>>() {}.type
                                                val jsonObject = Gson().fromJson<Map<String, Any>>(element, mapType)

                                                // 필드의 값이 실수로 들어가 있을 경우 정수로 변환
                                                val dataList = mutableMapOf<String, Any>()
                                                for ((key, value) in jsonObject) {
                                                    val newValue =
                                                        if ((key == "grade" || key == "credit") && value is Double) {
                                                            value.toInt()
                                                        } else {
                                                            value
                                                        }
                                                    dataList[key] = newValue
                                                }
                                                majorSubjectList.add(dataList)
                                            }
                                        }

                                        val tableLayout = binding.tableLayout
                                        tableLayout.removeAllViews() // TableRow를 다 제거함(초기화)

                                        for (map in majorSubjectList) {
                                            // 새로운 TableRow 생성
                                            val tableRow = TableRow(this@MajorActivity)

                                            val button = CheckBox(this@MajorActivity)
                                            // 버튼을 TableRow에 추가
                                            tableRow.addView(button)

                                            var tmp = ""
                                            for ((key, value) in map) {
                                                when (key) {
                                                    "grade", "c_major", "credit" -> {

                                                        // 공백을 추가하는 TextView 생성
                                                        val spaceTextView = TextView(this@MajorActivity)
                                                        spaceTextView.text = "     "

                                                        val textView = TextView(this@MajorActivity)
                                                        textView.text = value.toString()


                                                        // TableRow에 공백 TextView와 TextView를 번갈아가며 추가
                                                        tableRow.addView(spaceTextView)
                                                        tableRow.addView(textView)
                                                    }
                                                    "sub_name" -> {
                                                        tmp = value.toString()
                                                    }
                                                }
                                            }
                                            // key가 "sub_name"인 값(value)이 tableRow에서 마지막에 오도록 함
                                            if (!tmp.isEmpty()) {
                                                // 공백을 추가하는 TextView 생성
                                                val spaceTextView = TextView(this@MajorActivity)
                                                spaceTextView.text = "     "

                                                val textView = TextView(this@MajorActivity)
                                                textView.text = tmp


                                                // TableRow에 공백 TextView와 TextView를 번갈아가며 추가
                                                tableRow.addView(spaceTextView)
                                                tableRow.addView(textView)
                                            }

                                            // tableLayout에 TableRow 추가
                                            tableLayout.addView(tableRow)

                                            // 체크박스 클릭 이벤트 설정
                                            button.setOnCheckedChangeListener { _, isChecked ->
                                                if (isChecked) {
                                                    selectedRows.add(map)
                                                    Log.d("Tets", map.toString())

                                                    cnt++
                                                    if ( cnt == 1 )
                                                        count += cnt

                                                    val message = "현재 수강한 전공 과목을 선택한 학기: $count/${semesterList.size}"
                                                    binding.textView.text = message
                                                } else {
                                                    selectedRows.remove(map)
                                                }
                                            }
                                        }
                                    }
                                    else
                                        Log.e("err", "상태코드: ${response.code()}")

                                }

                                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                    Log.e("fail","${t.message}")
                                }
                            })

                        }

                    }

                binding.okBtn.setOnClickListener {
                    val data = SelectedMajorSubjects(selectedRows)
                    if (count >= 8) {
                        service.majorput(data).enqueue(object: Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>,
                                                    response: Response<JsonObject>,
                            ) {
                                if (response.isSuccessful){
                                    val mRequirementSatisfacionStatus = response.body()?.get("state")?.asBoolean
                                    val mCreditRSS = response.body()?.get("checkState")?.asBoolean
                                    val csSubjectRegistrationStatus = response.body()?.get("capstoneState")?.asBoolean
                                    val majorCredit = response.body()!!.get("m_score")?.asInt
                                    val mnRequirementSatisfacionStatus = response.body()?.get("m_need_checkState")?.asBoolean
                                    val mnCredit = response.body()!!.get("m_need_score")?.asInt
                                    val mnSubjectList = response.body()!!.get("m_need_list")

                                    if (!mRequirementSatisfacionStatus!!) { //전공 졸업요건을 충족하지 못했을 경우
                                        binding.noticeLayout.visibility = View.VISIBLE

                                        val text = buildString {
                                            if (!csSubjectRegistrationStatus!!){
                                                append(" ○ 캡스톤디지인(1), 캡스톤디자인(2) 과목은 필수로 수강해야 합니다.")
                                                append("\n\n")
                                            }
                                            if (!mCreditRSS!!){
                                                append(" ○ 최소 이수 학점까지 남은 전공 학점: ${72 - majorCredit!!}")
                                                append("\n\n")

                                            }
                                            if (!mnRequirementSatisfacionStatus!!){
                                                append(" ○ 최소 이수 학점까지 남은 전공필수 학점: ${19 - mnCredit!!}\n")
                                                append(" ○ 미수강한 전공 필수 과목: $mnSubjectList")
                                            }
                                        }
                                        binding.noticeMassage.text = text
                                    }
                                }
                                else
                                    Log.e("err", "상태코드: ${response.code()}")
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                Log.e("fail","${t.message}")
                            }
                        })
                    }
                    else {
                        AlertDialog.Builder(this@MajorActivity)
                            .setMessage("학기별 수강한 전공 과목을 다 선택해야 합니다.")
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