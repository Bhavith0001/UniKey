package com.unikey.android.objects


data class Attendance(
    val attendanceList: List<Subject>?  = null
)

data class Subject(
    val subCode: String? = null,
    val name: String? = null,
    val attendance: String? = null,
)