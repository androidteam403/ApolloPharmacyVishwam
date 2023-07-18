package com.apollopharmacy.vishwam.ui.home.retroqr.activity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StoreWiseRackDetails : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("storeDetails")
    @Expose
    var storeDetails: List<StoreDetail>? = null
    fun withStatus(status: Boolean?): StoreWiseRackDetails {
        this.status = status
        return this
    }

    fun withMessage(message: String?): StoreWiseRackDetails {
        this.message = message
        return this
    }

    fun withStoreDetails(storeDetails: List<StoreDetail>?): StoreWiseRackDetails {
        this.storeDetails = storeDetails
        return this
    }

    public class StoreDetail : Serializable {
        @SerializedName("RACKNO")
        @Expose
        var rackno: String? = null

        @SerializedName("QRCODE")
        @Expose
        var qrcode: String? = null

        @SerializedName("IMAGEURL")
        @Expose
        var imageurl: String? = null
        fun withRackno(rackno: String?): StoreDetail {
            this.rackno = rackno
            return this
        }

        fun withQrcode(qrcode: String?): StoreDetail {
            this.qrcode = qrcode
            return this
        }

        fun withImageurl(imageurl: String?): StoreDetail {
            this.imageurl = imageurl
            return this
        }
    }
}