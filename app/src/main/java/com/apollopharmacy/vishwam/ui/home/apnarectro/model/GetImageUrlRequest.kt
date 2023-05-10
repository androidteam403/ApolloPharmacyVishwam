package com.apollopharmacy.vishwam.ui.home.apnarectro.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetImageUrlRequest : Serializable {
    @SerializedName("RETRO_ID")
    @Expose
    var retroId: String? = null

    @SerializedName("STOREID")
    @Expose
    var storeid: String? = null
    fun withRetroId(retroId: String?): GetImageUrlRequest {
        this.retroId = retroId
        return this
    }

    fun withStoreid(storeid: String?): GetImageUrlRequest {
        this.storeid = storeid
        return this
    }
}