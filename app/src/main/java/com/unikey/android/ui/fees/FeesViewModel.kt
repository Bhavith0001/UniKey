package com.unikey.android.ui.fees

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unikey.android.database.cloud.CloudStorage
import com.unikey.android.objects.Fees
import kotlinx.coroutines.launch

class FeesViewModel : ViewModel() {

    private val dataStore =  CloudStorage()

    private val _fees: MutableLiveData<Fees> =  MutableLiveData()
    val fees: LiveData<Fees>
    get() = _fees

    private fun getFees(){
        viewModelScope.launch {
            val fees = dataStore.getFeesDetails()
            _fees.value = fees
            Log.d("FFF", "getFees: $fees")
        }
    }

    init {
        getFees()
    }

}