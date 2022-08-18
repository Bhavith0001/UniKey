package com.unikey.android.ui.certificatecouses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unikey.android.databinding.CourseItemBinding
import com.unikey.android.objects.Course

class CourseListAdapter : ListAdapter<Course, CourseViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val item = CourseItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CourseViewHolder(item)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Course>(){
            override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem.courseName == newItem.courseName
            }

            override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem == newItem
            }

        }
    }
}

class CourseViewHolder(private val binding: CourseItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(course: Course){
        binding.apply {
            courseName.text = course.courseName
            courseDescription.text = course.description
            fees.text = "Rs.${course.fees}"
        }
    }
}