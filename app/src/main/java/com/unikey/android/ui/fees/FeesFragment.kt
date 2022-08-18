package com.unikey.android.ui.fees

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialContainerTransform
import com.unikey.android.ANIM_DURATION
import com.unikey.android.R
import com.unikey.android.databinding.FragmentFeesBinding
import com.unikey.android.setUp

class FeesFragment : Fragment() {

    private lateinit var binding: FragmentFeesBinding

    private val viewModel: FeesViewModel by viewModels()


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
        binding = FragmentFeesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fees.observe(viewLifecycleOwner){
            binding.total.value.text = "Rs."+it.total.toString()
            binding.paid.value.text = "Rs."+it.paid.toString()
            binding.pending.value.text = "Rs."+(it.total?.minus(it.paid!!)).toString()
        }

        binding.run {

            total.label.text = "Total"
            paid.label.text = "Paid"
            pending.label.text = "Pending"

            appBar.toolBar.setUp(this@FeesFragment, "Fees")

        }
    }

}