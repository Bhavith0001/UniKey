package com.unikey.android.ui.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unikey.android.database.cloud.Authentication
import com.unikey.android.database.cloud.CloudStorage
import com.unikey.android.objects.User
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    /* Instance of auth */
    private val auth = Authentication()

    private val dataStore = CloudStorage()

    /** Make the user sign out of the app */
    fun signOutUser() = auth.signOut()

    fun uploadImage(uri: Uri) {
        dataStore.uploadProfileImg(uri)
    }

    val user: MutableLiveData<User>? = CloudStorage.userInfo        // MutableLiveData()

}