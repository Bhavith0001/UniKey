package com.unikey.android.objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationObj(
    val title: String? = null,
    val description: String? = null,
    val content: String? = null,
    val time: Long? = null,
) : Parcelable
