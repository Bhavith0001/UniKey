package com.unikey.android.ui.timetable

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.unikey.android.databinding.TimeTableItemBinding
import com.unikey.android.objects.TimeTable
import java.util.*

class TimeTableViewHolder(private val binding: TimeTableItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(timeTable: TimeTable){
        secondSubVisibility(timeTable.isMultiSub!!)

        binding.apply {
            dateText.text = "Date : ${timeTable.date}"
            time1.text = timeTable.exam?.get(0)?.time
            subName1.text = timeTable.exam?.get(0)?.subName

            time2.text = timeTable.exam?.get(1)?.time
            subName2.text = timeTable.exam?.get(1)?.subName
        }
    }

    private fun secondSubVisibility(flag: Boolean){
        if (flag)
            binding.secondSub.visibility = View.VISIBLE
        else
            binding.secondSub.visibility = View.GONE
    }
}