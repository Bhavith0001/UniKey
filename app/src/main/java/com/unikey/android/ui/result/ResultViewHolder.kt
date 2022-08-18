package com.unikey.android.ui.result

import androidx.recyclerview.widget.RecyclerView
import com.unikey.android.STUD_RESULT_FAIL
import com.unikey.android.databinding.ResultSubItemBinding
import com.unikey.android.objects.SubjectResult

class ResultViewHolder(private val binding: ResultSubItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(subjectResult: SubjectResult){
        binding.apply {
            subCodeValue.text = subjectResult.subCode
            subNameValue.text = subjectResult.subName
            typeValue.text = subjectResult.type
            maxValue.text = subjectResult.max.toString()
            marksValue.text = subjectResult.marks.toString()
            passFailValue.text = subjectResult.passFail
            if (subjectResult.passFail == "F")
                root.setBackgroundColor(STUD_RESULT_FAIL)
        }
    }
}