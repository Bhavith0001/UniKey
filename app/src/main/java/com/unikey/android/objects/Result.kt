package com.unikey.android.objects

data class Result(
    val regNo: Long? = null,
    val name: String? = null,
    val sem: Int? = null,
    val resultsList: List<SubjectResult>? = null
)

data class SubjectResult(
    val subCode: String? = null,
    val subName: String? = null,
    val type: String? = null,
    val max: Int? = null,
    val marks: Int? = null,
    val passFail: String? = null,
)
