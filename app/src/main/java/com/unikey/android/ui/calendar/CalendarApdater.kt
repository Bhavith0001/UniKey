package com.unikey.android.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unikey.android.CURRENT_DATE
import com.unikey.android.R
import com.unikey.android.databinding.CalendarItemBinding
import com.unikey.android.objects.EventDate

class CalendarAdapter(val onClick: (date: String) -> Unit) : ListAdapter<EventDate, CalendarAdapter.CalenderItemViewHolder>(DiffCallback) {

    inner class CalenderItemViewHolder(val binding: CalendarItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(eventDate: EventDate){
            if (eventDate.isTodays == true) {
                binding.dateItem.setTextColor(CURRENT_DATE)
            }
            if (eventDate.containEvent == true){
                binding.dateItem.setBackgroundResource(R.color.light_green)
            }
            binding.dateItem.text = eventDate.date

            binding.root.setOnClickListener { onClick(eventDate.date!!) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderItemViewHolder {
        val view = CalendarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams  = view.root.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalenderItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalenderItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DiffCallback  = object : DiffUtil.ItemCallback<EventDate>(){
            override fun areItemsTheSame(oldItem: EventDate, newItem: EventDate): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: EventDate, newItem: EventDate): Boolean {
                return oldItem == newItem
            }

        }
    }
}