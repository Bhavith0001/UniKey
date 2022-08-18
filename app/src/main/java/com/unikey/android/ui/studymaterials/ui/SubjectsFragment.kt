package com.unikey.android.ui.studymaterials.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.unikey.android.databinding.FragmentSubjectsBinding
import com.unikey.android.setHostSharedAxisAnim
import com.unikey.android.setTargetSharedAxisAnim
import com.unikey.android.setUp
import com.unikey.android.ui.studymaterials.StudyMaterialsViewModel
import com.unikey.android.ui.studymaterials.adapters.SubjectsListAdapter

class SubjectsFragment : Fragment() {

    private lateinit var binding: FragmentSubjectsBinding

    private val viewModel: StudyMaterialsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTargetSharedAxisAnim(MaterialSharedAxis.X)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubjectsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolBar.setUp(this, "Subjects")

        val adapter = SubjectsListAdapter(findNavController()) {
            viewModel.getLocalFiles(it)
            setHostSharedAxisAnim(MaterialSharedAxis.X)
        }
        binding.subjectsRecyclerView.adapter = adapter

        viewModel.subjectsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

    }

}