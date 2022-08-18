package com.unikey.android.ui.homescreen

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unikey.android.database.cloud.Authentication
import com.unikey.android.database.cloud.CloudStorage
import com.unikey.android.objects.User
import kotlinx.coroutines.launch

class HomeScreenViewModel() : ViewModel() {

    /* Instance of auth */
    private val auth = Authentication()

    // Instance of Cloud Storage
    private val dataStore = CloudStorage()

    val user: MutableLiveData<User>? = CloudStorage.userInfo        // MutableLiveData()

    val averageAttendance = dataStore.averageAttendance
    val pendingFees = dataStore.pendingFees


    /** Return true if the user is authenticated
     * else returns false*/
    fun isAuthenticated() = auth.isAuthenticated()


    init {
        dataStore.printLog()
    }

    fun getAttendanceAndFees(){
        dataStore.getAverageAttendance()
        dataStore.getPendingFees()
    }


}
