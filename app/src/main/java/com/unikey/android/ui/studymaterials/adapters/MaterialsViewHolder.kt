package com.unikey.android.ui.studymaterials.adapters

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.unikey.android.R
import com.unikey.android.databinding.ChapterItemBinding
import com.unikey.android.objects.StudyMaterialsPdf

class MaterialsViewHolder(
    private val binding: ChapterItemBinding
) : RecyclerView.ViewHolder(binding.root){

    lateinit var  downloadPdf: (StudyMaterialsPdf) -> LiveData<DownloadStatus>
    lateinit var openPdf: (String) -> Unit

    private var downLoadListener: MutableLiveData<DownloadStatus> = MutableLiveData()

    fun bindData(
        data: StudyMaterialsPdf,
        checkDownloadStatus: (String) -> LiveData<DownloadStatus>,
        downloadPdf: (StudyMaterialsPdf) -> LiveData<DownloadStatus>,
        openPdf: (String) -> Unit,
    ) {
        this.downloadPdf = downloadPdf
        this.openPdf = openPdf

        val chapterName = data.chapterName
        binding.chapterName.text = chapterName
        binding.downloadBtn.visibility = View.GONE
        checkDownloadStatus(chapterName!!).observeForever {
            setDButtonStatus(it, data)
        }
    }

    private fun setDButtonStatus(status: DownloadStatus, pdf: StudyMaterialsPdf) {
        if (status == DownloadStatus.DOWNLOADED) {
            binding.downloadBtn.apply {
                isEnabled = true
                isClickable = false
                isFocusable = false
                setImageResource(R.drawable.offline_pin_24)
                visibility = View.VISIBLE
                setOnCardClick(pdf.chapterName!!)
            }
            return
        }

        if (status == DownloadStatus.NOT_DOWNLOADED) {
            binding.downloadBtn.apply {
                isEnabled = true
                setImageResource(R.drawable.download_24px)
                visibility = View.VISIBLE
                setOnDownloadBtnClick(pdf)
            }
            return
        }
    }

    private fun setOnDownloadBtnClick(pdf: StudyMaterialsPdf) {
        binding.root.isClickable = false
        binding.downloadBtn.setOnClickListener {
            downLoadListener = downloadPdf(pdf) as MutableLiveData<DownloadStatus>
                downLoadListener.observeForever {
                    setProgressBar(it, pdf)
                    setDButtonStatus(it, pdf)
            }
        }
    }

    private fun setProgressBar(status: DownloadStatus, url: StudyMaterialsPdf) {
        when(status){
            DownloadStatus.DOWNLOADING -> binding.apply {
                downloadBtn.visibility = View.GONE
                downloadingProgress.visibility = View.VISIBLE
            }
            else -> {
                binding.downloadingProgress.visibility = View.GONE
                setDButtonStatus(status,url)
            }
        }
    }

    private fun setOnCardClick(chapterName: String) {
        binding.root.setOnClickListener {
            openPdf(chapterName)
        }
    }

}

enum class DownloadStatus{
    DOWNLOADING , NONE, DOWNLOADED, NOT_DOWNLOADED, FAILED
}
