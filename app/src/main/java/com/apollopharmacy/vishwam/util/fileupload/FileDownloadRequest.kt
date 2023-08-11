package com.apollopharmacy.vishwam.util.fileupload

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FileDownloadRequest {
    @SerializedName("RefURL")
    @Expose
    var RefURL: String? = null

}