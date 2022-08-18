package com.unikey.android.ui

import androidx.lifecycle.ViewModel
import com.unikey.android.database.cloud.Authentication

class MainViewModel : ViewModel() {

    /* Instance of auth */
    private val auth = Authentication()

    /** Return true if the user is authenticated
     * else returns false*/
    fun isAuthenticated() = auth.isAuthenticated()
}