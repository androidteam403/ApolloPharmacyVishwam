package com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CheckDayWiseAccessResponse : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("SUNDAY")
    @Expose
    var sunday: Boolean? = null

    @SerializedName("MONDAY")
    @Expose
    var monday: Boolean? = null

    @SerializedName("TUESDAY")
    @Expose
    var tuesday: Boolean? = null

    @SerializedName("WEDNESDAY")
    @Expose
    var wednesday: Boolean? = null

    @SerializedName("THURSDAY")
    @Expose
    var thursday: Boolean? = null

    @SerializedName("FRIDAY")
    @Expose
    var friday: Boolean? = null

    @SerializedName("SATURDAY")
    @Expose
    var saturday: Boolean? = null

    @SerializedName("UPLOADEDDATE")
    @Expose
    var lastUploadedDate: String? = null


}