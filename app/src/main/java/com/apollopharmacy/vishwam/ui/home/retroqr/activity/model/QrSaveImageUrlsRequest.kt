package com.apollopharmacy.vishwam.ui.home.retroqr.activity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QrSaveImageUrlsRequest : Serializable {
    @SerializedName("STOREID")
    @Expose
    var storeid: String? = null

    @SerializedName("USERID")
    @Expose
    var userid: String? = null

    @SerializedName("storeDetails")
    @Expose
    var storeDetails: List<StoreDetail>? = null
    fun withStoreid(storeid: String?): QrSaveImageUrlsRequest {
        this.storeid = storeid
        return this
    }

    fun withUserid(userid: String?): QrSaveImageUrlsRequest {
        this.userid = userid
        return this
    }

    fun withStoreDetails(storeDetails: List<StoreDetail>?): QrSaveImageUrlsRequest {
        this.storeDetails = storeDetails
        return this
    }

    inner class StoreDetail : Serializable {
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