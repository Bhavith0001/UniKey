package com.unikey.android.ui.timetable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unikey.android.database.cloud.CloudStorage
import com.unikey.android.fakeTimeTable
import com.unikey.android.fakeTimeTableList
import com.unikey.android.objects.ExamTimeTableList
import com.unikey.android.objects.TimeTable
import kotlinx.coroutines.launch

class TimeTableViewModel : ViewModel() {
    private val dataStore = CloudStorage()

    init {
        test()
    }

    fun test(){
        viewModelScope.launch {
            dataStore.putData()
        }
    }
}