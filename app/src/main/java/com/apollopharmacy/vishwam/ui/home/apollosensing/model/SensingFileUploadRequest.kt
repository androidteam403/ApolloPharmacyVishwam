package com.apollopharmacy.vishwam.ui.home.apollosensing.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File

class SensingFileUploadRequest {
    @SerializedName("Filename")
    @Expose
    var Filename: File? = null

}