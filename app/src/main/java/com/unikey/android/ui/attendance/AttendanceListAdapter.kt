package com.unikey.android.ui.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unikey.android.databinding.AttendanceItemBinding
import com.unikey.android.objects.Subject

class AttendanceListAdapter : ListAdapter<Subject, ViewHolder>(DiffCallBack)  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = AttendanceItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<Subject>(){
            override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
                return oldItem.subCode == newItem.subCode
            }

            override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
                return oldItem == newItem
            }

        }
    }

}

class ViewHolder(private val binding: AttendanceItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(subject: Subject){
        binding.subjectName.text = subject.name
        binding.percentage.text = subject.attendance+"%"
    }
}