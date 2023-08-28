package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable

class ScannerResponse : Serializable {
    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null

    var imageUrl1: File? = null
}