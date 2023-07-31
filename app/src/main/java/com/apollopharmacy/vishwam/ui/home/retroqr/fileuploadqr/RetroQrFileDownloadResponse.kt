package com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RetroQrFileDownloadResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("referenceurl")
    @Expose
    var referenceurl: String? = null
}