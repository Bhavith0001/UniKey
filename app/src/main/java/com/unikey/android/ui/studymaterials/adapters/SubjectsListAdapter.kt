package com.unikey.android.ui.studymaterials.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unikey.android.databinding.SubjectsItemBinding
import com.unikey.android.ui.studymaterials.ui.SubjectsFragmentDirections

class SubjectsListAdapter(
    val navController: NavController,
    val callBack: (String)-> Unit
    ) : ListAdapter<String, SubjectsListAdapter.SubjectViewHolder>(
    DiffCallBack
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val item = SubjectsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SubjectViewHolder(item)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SubjectViewHolder(private val binding : SubjectsItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(subName: String){
            binding.subjectName.text = subName
            binding.root.setOnClickListener {
                callBack(subName)
                navController.navigate(
                    SubjectsFragmentDirections.actionSubjectsFragmentToStudyMaterialsFragment(
                        subName
                    )
                )
            }
        }
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.length == newItem.length
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }
}