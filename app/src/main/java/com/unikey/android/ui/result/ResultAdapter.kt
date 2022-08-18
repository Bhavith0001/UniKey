package com.unikey.android.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.unikey.android.databinding.ResultSubItemBinding
import com.unikey.android.objects.SubjectResult

class ResultAdapter : ListAdapter<SubjectResult, ResultViewHolder>(DiffCallBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val item = ResultSubItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ResultViewHolder(item)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<SubjectResult>(){
            override fun areItemsTheSame(oldItem: SubjectResult, newItem: SubjectResult): Boolean {
                return oldItem.subCode == newItem.subCode
            }

            override fun areContentsTheSame(
                oldItem: SubjectResult,
                newItem: SubjectResult
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}