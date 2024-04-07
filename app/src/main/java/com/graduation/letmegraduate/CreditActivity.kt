package com.graduation.letmegraduate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.graduation.letmegraduate.databinding.ActivityCreditBinding

class CreditActivity : Fragment(){
    private lateinit var binding: ActivityCreditBinding
    private lateinit var intent: Intent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityCreditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.majorBtn.setOnClickListener {
            // 화면 이동
            intent = Intent(activity, MajorActivity::class.java)
            startActivity(intent)
        }

        binding.liberalArisBtn.setOnClickListener {
            // 화면 이동
            intent = Intent(activity, LiberalArtsActivity::class.java)
            startActivity(intent)
        }

        binding.generalElectiveBtn.setOnClickListener {
            // 화면 이동
            intent = Intent(activity, GeneralElectiveActivity::class.java)
            startActivity(intent)
        }
    }
}
