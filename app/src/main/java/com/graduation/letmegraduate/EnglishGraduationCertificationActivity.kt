package com.graduation.letmegraduate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.graduation.letmegraduate.databinding.ActivityEnglishGraduationCertificationBinding

class EnglishGraduationCertificationActivity : Fragment() {
    private lateinit var binding: ActivityEnglishGraduationCertificationBinding
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

        val kind = binding.certificateKind
        // dropdown menu에 표시할 항목들을 가져옴
        val item : Array<String> = resources.getStringArray(R.array.certificate_kind)
        // ArrayAdapter를 사용하여 dropdown menu에 항목들을 연결
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, item)
        kind.setAdapter(adapter)
    }
}