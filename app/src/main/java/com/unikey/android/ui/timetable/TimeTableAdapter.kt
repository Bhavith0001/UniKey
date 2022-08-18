package com.unikey.android.ui.timetable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.unikey.android.databinding.TimeTableItemBinding
import com.unikey.android.objects.TimeTable

class TimeTableAdapter : ListAdapter<TimeTable, TimeTableViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTableViewHolder {
        val item = TimeTableItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimeTableViewHolder(item)
    }

    override fun onBindViewHolder(holder: TimeTableViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<TimeTable>() {
            override fun areItemsTheSame(oldItem: TimeTable, newItem: TimeTable): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: TimeTable, newItem: TimeTable): Boolean {
                return oldItem == newItem
            }

        }
    }
}