package com.unikey.android.ui.certificatecouses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialSharedAxis
import com.unikey.android.R
import com.unikey.android.databinding.CourseItemBinding
import com.unikey.android.databinding.FragmentCoursesBinding
import com.unikey.android.objects.Course
import com.unikey.android.setTargetSharedAxisAnim
import com.unikey.android.setUp

class CoursesFragment : Fragment() {

    private lateinit var binding: FragmentCoursesBinding
    private val viewModel: CoursesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTargetSharedAxisAnim(MaterialSharedAxis.X)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolBar.setUp(this, "Certificate courses")

        val adapter = CourseListAdapter()
        binding.coursesListView.adapter = adapter

        viewModel.coursesList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

}