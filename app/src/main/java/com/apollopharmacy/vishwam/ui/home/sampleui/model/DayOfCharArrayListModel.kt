package com.apollopharmacy.vishwam.ui.home.sampleui.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DayOfCharArrayListModel : Serializable {
    @SerializedName("Weekname")
    @Expose
    var weekname: String? = null

    @SerializedName("Weekposition")
    @Expose
    var weekposition: Int? = null
}