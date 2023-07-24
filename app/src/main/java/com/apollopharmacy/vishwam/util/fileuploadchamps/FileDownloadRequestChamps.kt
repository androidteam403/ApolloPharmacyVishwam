package com.apollopharmacy.vishwam.util.fileuploadchamps

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FileDownloadRequestChamps {
    @SerializedName("RefURL")
    @Expose
    var RefURL: String? = null
}