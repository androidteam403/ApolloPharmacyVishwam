package com.apollopharmacy.vishwam.util.fileuploadchamps

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FileDownloadResponseChamps {
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