package com.unikey.android.objects

data class User(
    val regNo: Long? = null,
    val name: String? = null,
    @field:JvmField
    val isStudent: Boolean? = null,
    val dateOfBirth: String? = null,
    val email: String? = null,
    val phoneNumber: Long? = null,
    val cls: String? = null,
    val sem: Int? = null,
    val profileUrl: String? = null,
    val academicYear: String? = null,
    val dateOfAdmission: String? = null,
    val fatherName: String? = null,
    val motherName: String? = null,
    val address: String? = null,
)
