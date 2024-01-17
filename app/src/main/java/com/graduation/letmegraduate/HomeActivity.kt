package com.graduation.letmegraduate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.graduation.letmegraduate.databinding.ActivityHomeBinding

class HomeActivity : Fragment() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}