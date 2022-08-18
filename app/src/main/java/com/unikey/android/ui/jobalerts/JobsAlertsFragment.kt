package com.unikey.android.ui.jobalerts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialSharedAxis
import com.unikey.android.R
import com.unikey.android.databinding.FragmentJobsAletrsBinding
import com.unikey.android.databinding.JobAlertItemBinding
import com.unikey.android.setTargetSharedAxisAnim
import com.unikey.android.setUp

class JobsAlertsFragment : Fragment() {

    private lateinit var binding: FragmentJobsAletrsBinding
    private val viewModel: JobAlertViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTargetSharedAxisAnim(MaterialSharedAxis.X)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJobsAletrsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolBar.setUp(this, "Job alerts")

        val adapter = JobsListAdapter()
        binding.jobAlertsListView.adapter = adapter

        viewModel.jobsList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

}