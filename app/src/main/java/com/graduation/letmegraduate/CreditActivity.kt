package com.graduation.letmegraduate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.graduation.letmegraduate.databinding.ActivityCreditBinding

class CreditActivity : Fragment() {
    private lateinit var binding: ActivityCreditBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityCreditBinding.inflate(inflater, container, false)
        return binding.root
    }
}