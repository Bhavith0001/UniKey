package com.unikey.android.ui.studymaterials

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unikey.android.database.cloud.CloudStorage
import com.unikey.android.objects.StudyMaterialsPdf
import com.unikey.android.ui.studymaterials.adapters.DownloadStatus
import com.unikey.android.ui.studymaterials.ui.UploadStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class StudyMaterialsViewModel : ViewModel(){
    private val dataStore = CloudStorage()

    val subjectsList = dataStore.subjectsList

    val chaptersList = dataStore.chaptersList

    init {
        dataStore.getSubjectsList()
    }

    fun getChaptersList(subName: String){
        viewModelScope.launch {
            dataStore.getMaterialsList(subName)
        }
    }

    fun downloadPdf(context: Context, pdf: StudyMaterialsPdf, subjectName: String): LiveData<DownloadStatus>{
        val status: MutableLiveData<DownloadStatus> = MutableLiveData<DownloadStatus>(DownloadStatus.DOWNLOADING)
        Log.d("UUUU", "DownloadPdf: Downloaded")
        viewModelScope.launch {
            dataStore.downloadPdf(context,pdf, subjectName) as MutableLiveData<DownloadStatus>
            delay(3000)
            status.value = DownloadStatus.DOWNLOADED
        }
        Log.d("AAAAA", "downloadPdf: ${status.value}")
        return status
    }

    fun checkDownloadStatus(url: String): LiveData<DownloadStatus>{
        val status= MutableLiveData(DownloadStatus.NONE)
        viewModelScope.launch {
            status.value = dataStore.checkPresenceOfPdf(url)
            delay(2000)
        }
        return status
    }

    fun uploadPdfFile(clsName: String, subName: String,pdfName: String, uri: Uri?):LiveData<UploadStatus>{
        val status: MutableLiveData<UploadStatus> = MutableLiveData(UploadStatus.UPLOADING)
        viewModelScope.launch {
            Log.d("VVVV", "onActivityResult: $clsName $subName $uri")
            dataStore.uploadPdf(
                clsName = clsName,
                subjectName = subName,
                pdfName = pdfName,
                pdfUri = uri!!
            ).observeForever {
                status.value = it
            }
        }
        return status
    }

    fun getLocalFiles(subjectName: String){
        viewModelScope.launch {
            dataStore.getLocalFiles(subjectName)
        }
    }
}
