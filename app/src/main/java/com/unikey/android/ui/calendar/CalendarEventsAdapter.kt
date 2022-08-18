package com.unikey.android.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unikey.android.databinding.CalendarEventItemBinding
import com.unikey.android.objects.Event

class CalendarEventsAdapter() : ListAdapter<Event, EventsViewHolder>(
    DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val view = CalendarEventItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventsViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<Event>(){
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }

        }
    }
}


class EventsViewHolder(val binding: CalendarEventItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(data: Event){
        binding.title.text = data.title
        binding.description.text = data.description
        binding.date.text = data.date.toString()+" "+data.monthYear
    }
}