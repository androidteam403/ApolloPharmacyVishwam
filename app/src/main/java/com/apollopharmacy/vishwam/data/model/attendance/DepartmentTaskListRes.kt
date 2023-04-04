package com.apollopharmacy.vishwam.data.model.attendance

import com.google.gson.annotations.SerializedName
import java.util.*

data class DepartmentTaskListRes(
    @field:SerializedName("status")
    val status: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("TASKLIST")
    val TASKLIST: ArrayList<DepartmentTaskItem>
) {
    data class DepartmentTaskItem(

        @field:SerializedName("ID")
        val ID: String,

        @field:SerializedName("TASK")
        val TASK: String
    )
}