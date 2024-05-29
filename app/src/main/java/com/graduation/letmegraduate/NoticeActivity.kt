package com.graduation.letmegraduate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.graduation.letmegraduate.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityNoticeBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_notice)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar) //toolbar를 액션바로 설정
        val actionbar = supportActionBar
        actionbar!!.setDisplayShowTitleEnabled(false)
        actionbar.setDisplayHomeAsUpEnabled(true) // 액션바에 뒤로가기 버튼 추가

        binding.recyclerView.apply {
            adapter = RecyclerViewAdapter()
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        //(액션바의)뒤로가기 버튼 클릭 시 설정 화면으로 이동
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
        return true
    }
}
