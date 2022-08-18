package com.unikey.android.ui.attendance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unikey.android.database.cloud.CloudStorage
import com.unikey.android.objects.Attendance
import kotlinx.coroutines.launch

class AttendanceViewModel : ViewModel() {

    private val dataStore = CloudStorage()

    private val _attendance: MutableLiveData<Attendance>? = MutableLiveData<Attendance>()
    val attendance: LiveData<Attendance>?
    get() = _attendance

    init {
        getAttendance()
    }

    fun getAttendance() {
        viewModelScope.launch {
            val data = dataStore.getAttendance()
            _attendance?.value = data
            Log.d("ATTT", "getAttendance__2: $data")
        }
    }
}