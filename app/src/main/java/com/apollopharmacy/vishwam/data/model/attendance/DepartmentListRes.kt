package com.apollopharmacy.vishwam.data.model.attendance

import com.google.gson.annotations.SerializedName

data class DepartmentListRes(
    @field:SerializedName("status")
    val status: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("DEPARTMENTLIST")
    val DEPARTMENTLIST: ArrayList<DepartmentItem>
) {
    data class DepartmentItem(

        @field:SerializedName("ID")
        val ID: String,

        @field:SerializedName("DEPARTMENT")
        val DEPARTMENT: String
    )
}