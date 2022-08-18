package com.unikey.android.ui.timetable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.unikey.android.R
import com.unikey.android.databinding.FragmentTimeTableBinding
import com.unikey.android.fakeTimeTable
import com.unikey.android.setUp

class TimeTableFragment : Fragment() {

    private lateinit var binding: FragmentTimeTableBinding
    private val viewModel: TimeTableViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTimeTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolBar.setUp(this, "Time Table")

        val adapter = TimeTableAdapter()
        binding.timeTableRecyclerView.adapter = adapter

//        viewModel.timeTableList.observe(viewLifecycleOwner){
//            adapter.submitList(it.timeTableList)
//            binding.timeTableName.text = it.timeTableList?.get(0)?.timeTableName
//        }
    }

}