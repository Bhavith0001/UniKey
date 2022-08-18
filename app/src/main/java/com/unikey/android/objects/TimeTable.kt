package com.unikey.android.objects

data class ExamTimeTableList(
    val timeTableList: List<TimeTable>? = null
)

data class TimeTable(
    val timeTableName: String? = null,
    val date: String? = null,
    val exam: List<Exam>? = null,
    @field:JvmField
    val isMultiSub: Boolean? = null
)

data class Exam(
    val time: String? = null,
    val subName: String? = null
)
