package com.unikey.android.ui.jobalerts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unikey.android.databinding.CourseItemBinding
import com.unikey.android.databinding.JobAlertItemBinding
import com.unikey.android.objects.JobAlert
import com.unikey.android.ui.certificatecouses.CourseViewHolder

class JobsListAdapter : ListAdapter<JobAlert, JobAlertViewHolder>(DiffCallBack) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobAlertViewHolder {
        val item = JobAlertItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JobAlertViewHolder(item)
    }

    override fun onBindViewHolder(holder: JobAlertViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<JobAlert>(){
            override fun areItemsTheSame(oldItem: JobAlert, newItem: JobAlert): Boolean {
                return oldItem.jobDescription == newItem.jobDescription
            }

            override fun areContentsTheSame(oldItem: JobAlert, newItem: JobAlert): Boolean {
                return newItem == oldItem
            }

        }
    }
}

class JobAlertViewHolder(private val binding: JobAlertItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(jobAlert: JobAlert){
        binding.apply {
            companyName.text = jobAlert.companyName
            jobRole.text = jobAlert.jobRole
            jobDescription.text = jobAlert.jobDescription
            applyLink.text = jobAlert.link
            salaryPackage.text = jobAlert.salaryPackage
        }
    }
}