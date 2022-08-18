package com.unikey.android.ui.studymaterials.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.unikey.android.databinding.ChapterItemBinding
import com.unikey.android.objects.StudyMaterialsPdf

class StudyMaterialsLisAdapter(
    private val checkDownloadStatus: (String) -> LiveData<DownloadStatus>,
    private val downloadPdf: (StudyMaterialsPdf) -> LiveData<DownloadStatus>,
    private val openPdf: (String) -> Unit,
) : ListAdapter<StudyMaterialsPdf, MaterialsViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialsViewHolder {
        val item = ChapterItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MaterialsViewHolder(item)
    }

    override fun onBindViewHolder(holder: MaterialsViewHolder, position: Int) {
        holder.bindData(
            data = getItem(position),
            checkDownloadStatus = checkDownloadStatus,
            downloadPdf = downloadPdf,
            openPdf = openPdf
        )
    }

    companion object{
        val DiffCallBack = object : DiffUtil.ItemCallback<StudyMaterialsPdf>(){
            override fun areItemsTheSame(
                oldItem: StudyMaterialsPdf,
                newItem: StudyMaterialsPdf
            ): Boolean {
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(
                oldItem: StudyMaterialsPdf,
                newItem: StudyMaterialsPdf
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}


