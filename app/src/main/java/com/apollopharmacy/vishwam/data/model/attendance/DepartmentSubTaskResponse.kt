package com.apollopharmacy.vishwam.data.model.attendance

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DepartmentSubTaskResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("TASKLIST")
    @Expose
    var tasklist: Any? = null

    @SerializedName("SUBTASKLIST")
    @Expose
    var subtasklist: List<Subtask>? = null

    public class Subtask : Serializable {
        @SerializedName("ID")
        @Expose
        var id: String? = null

        @SerializedName("SUBTASK")
        @Expose
        var subtask: String? = null
    }
}