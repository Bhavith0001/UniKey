package com.unikey.android.ui.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unikey.android.database.cloud.CloudStorage
import kotlinx.coroutines.launch

class AboutViewModel : ViewModel() {
    private val dataStore = CloudStorage()

    val aboutContent = dataStore.about

    init {
        dataStore.getAboutContent()
    }
}