package com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RetroQrFileDownloadRequest {
    @SerializedName("RefURL")
    @Expose
    var RefURL: String? = null

}