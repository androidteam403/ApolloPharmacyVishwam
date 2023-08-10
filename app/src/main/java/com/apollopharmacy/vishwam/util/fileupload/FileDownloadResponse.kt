package com.apollopharmacy.vishwam.util.fileupload

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FileDownloadResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("referenceurl")
    @Expose
    var referenceurl: String? = null

    var decryptedUrl: String? = null

}