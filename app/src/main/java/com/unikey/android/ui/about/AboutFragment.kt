package com.unikey.android.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialSharedAxis
import com.unikey.android.R
import com.unikey.android.databinding.FragmentAboutBinding
import com.unikey.android.setTargetSharedAxisAnim
import com.unikey.android.setUp

class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding
    private val viewModel: AboutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTargetSharedAxisAnim(MaterialSharedAxis.X)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.toolBar.setUp(this, " About")
        viewModel.aboutContent.observe(viewLifecycleOwner){
            binding.collegeName.text = it.collegeName
            binding.aboutContent.text = it.aboutContent
        }
    }
}