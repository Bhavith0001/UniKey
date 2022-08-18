package com.unikey.android.ui.result

import androidx.lifecycle.ViewModel
import com.unikey.android.objects.Result

class ResultViewModel : ViewModel() {
    val results = Result(
        name = "Bhavith K",
        regNo = 192292L,
        sem = 5
    )
}