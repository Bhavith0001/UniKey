package com.unikey.android.ui.certificatecouses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unikey.android.database.cloud.CloudStorage
import kotlinx.coroutines.launch

class CoursesViewModel : ViewModel() {
    private val dataStore = CloudStorage()

    val coursesList = dataStore.coursesList

    init {
        dataStore.getCoursesList()
    }

}