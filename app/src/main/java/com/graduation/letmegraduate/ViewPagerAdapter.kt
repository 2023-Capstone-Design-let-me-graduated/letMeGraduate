package com.graduation.letmegraduate

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 3

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeActivity() // 홈 탭 클릭 시 홈 화면을 보여줌
            1 -> CreditActivity() // 학점 탭 클릭 시 학점 화면을 보여줌
            else -> EnglishGraduationCertificationActivity() // 영어졸업인증 탭 클릭 시 영어졸업인증 화면을 보여줌
        }
    }
}