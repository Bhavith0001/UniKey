package com.unikey.android.application

import android.app.Application
import com.google.android.material.color.DynamicColors

class UniKeyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}