package com.graduation.letmegraduate

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.graduation.letmegraduate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar) //toolbar를 액션바로 설정
        val actionbar = supportActionBar
        actionbar!!.setDisplayShowTitleEnabled(false)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        // 스와이프로 화면 전환할 수 없게 하기
        viewPager.isUserInputEnabled = false

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        // TabLayout과 ViewPager를 연동
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.ic_home)
                    tab.text = "홈"
                }
                1 -> {
                    tab.setIcon(R.drawable.ic_create)
                    tab.text = "학점"
                }

                2 -> {
                    tab.setIcon(R.drawable.ic_alphabet_e)
                    tab.text = "영어졸업인증"
                }
            }
        }.attach()

        // 메인 화면을 처음 띄웠을 때 첫번째 tab의 아이콘 색 설정
        tabLayout.getTabAt(0)?.icon?.setTint(getColor(R.color.black))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // 선택된 탭의 아이콘 색 변경
                tab.icon?.setTint(getColor(R.color.black))
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // 선택이 해제된 탭의 아이콘 색을 원래 색으로 변경
                tab.icon?.setTint(Color.parseColor("#96999C"))
            }

            override fun onTabReselected(tab: TabLayout.Tab) { // 재선택된 탭의 경우 추가적인 동작 수행 가능
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.setting -> {
                // 액션바에서 설정 아이콘 클릭 시 설정 화면으로 이동
                intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}