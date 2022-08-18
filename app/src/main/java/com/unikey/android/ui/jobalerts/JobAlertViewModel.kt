package com.unikey.android.ui.jobalerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unikey.android.database.cloud.CloudStorage
import kotlinx.coroutines.launch

class JobAlertViewModel : ViewModel() {
    private val dataStore = CloudStorage()
    val jobsList = dataStore.jobsList

    init {
        dataStore.getJobsList()
    }
}