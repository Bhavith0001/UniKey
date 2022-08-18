package com.unikey.android.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.unikey.android.R
import com.unikey.android.databinding.FragmentResultBinding
import com.unikey.android.setUp

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private val viewModel: ResultViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()

        binding.submit.setOnClickListener {
            getResult()
        }
    }

    private fun getResult() {
        val result = viewModel.results
        with(binding){
            studentName.text = getString(R.string.stud_name, result.name)
            regNo.text = getString(R.string.stud_reg_no, result.regNo)
            sem.text = getString(R.string.sem_head, result.sem)
        }
        binding.inputDetails.visibility = View.GONE
        binding.resultsDetails.visibility = View.VISIBLE
    }

    private fun setViews() {
        with(binding) {
            appBar.toolBar.setUp(this@ResultFragment, "Result")

            inputSem.detailTxtInputLayout.apply {
                hint = "SEM"
                placeholderText = "Enter SEM"
            }
        }
    }

}