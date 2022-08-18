package com.unikey.android.ui.attendance

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialContainerTransform
import com.unikey.android.ANIM_DURATION
import com.unikey.android.R
import com.unikey.android.databinding.FragmentAttendanceBinding
import com.unikey.android.setUp

class AttendanceFragment : Fragment() {

    private val viewModel : AttendanceViewModel by viewModels()

    private lateinit var binding: FragmentAttendanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.main_activity_fragment_container
            duration = ANIM_DURATION
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AttendanceListAdapter()
        binding.attendanceRecyclerView.adapter = adapter

        viewModel.attendance?.observe(viewLifecycleOwner){
            adapter.submitList(it.attendanceList)
        }

        binding.run {
            appBar.toolBar.setUp(this@AttendanceFragment, "Attendance")
        }
    }
}