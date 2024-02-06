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

    @SerializedName("IMAGEURL")
    @Expose
    var imageurl: Any? = null

    @SerializedName("CategoryDetails")
    @Expose
    var categoryDetails: Any? = null

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

        @SerializedName("CATEGORYNAME")
        @Expose
        var categoryname: String? = null

        @SerializedName("CATEGORYID")
        @Expose
        var categoryid: Int? = null

        var byteArray=ByteArray(0)
        var reviewimageurl: String? = ""
        var isRackSelected: Boolean = false
//        var categoryIdQr: Int? = 0
//        fun setcategoryIdQr(url: Int) {
//            categoryIdQr = url
//        }
//        var categoryQr: String? = ""
//        fun setcategoryQr(url: String) {
//            categoryQr = url
//        }

        fun setreviewimageurl(url: String) {
            reviewimageurl = url
        }

        var matchingPercentage: String? = ""

        fun setmatchingPercentage(pos: String) {
            matchingPercentage = pos
        }

    }
}