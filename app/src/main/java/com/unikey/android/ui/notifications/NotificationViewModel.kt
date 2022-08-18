package com.unikey.android.ui.notifications

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unikey.android.database.cloud.CloudStorage
import com.unikey.android.objects.NotificationObj
import kotlinx.coroutines.launch
import java.util.ArrayList

class NotificationViewModel : ViewModel() {

    private val dataStore = CloudStorage()

    var categoryList = dataStore.clsNamesList //arrayOf("BCA", "BBA", "BCOM", "BSC", "MCA", "BA","BCA", "BBA", "BCOM", "BSC", "MCA", "BA")
    val checkedList = mutableListOf<Boolean>()
    val selectedList = mutableListOf<Int>()

    val notificationList = dataStore.notificationList

    val btnDoneEnabled = MutableLiveData(selectedList.isNotEmpty())


    init {
        dataStore.getNotifications()
    }

    fun setBtnDoneEnabled(){
        btnDoneEnabled.value = selectedList.isNotEmpty()
    }

    /** Makes the FAB visible if the user is not student.  */
    fun fabEnableOrDisable(): Int {
        return if (CloudStorage.userInfo?.value?.isStudent!!)
            View.GONE
        else
            View.VISIBLE
    }


    fun getClsNamesList(){
        dataStore.getClsNames()
        Log.d("GGG", "getClsNamesList : Called")
    }

    fun sendNotification(
        title: String,
        description: String,
        content: String
    ){
        val time = System.currentTimeMillis()
        val notificationObj = NotificationObj(
            title = title,
            description = description,
            content = content,
            time = time
        )

        viewModelScope.launch {
            val resList: MutableList<String> = mutableListOf()
            for (i in selectedList){
                resList.add(categoryList.value!![i])
            }
            Log.d("SSS", "sendNotification: $time \n $notificationObj \n $resList ")
            dataStore.sendNotification(resList, notificationObj)
        }
    }


}