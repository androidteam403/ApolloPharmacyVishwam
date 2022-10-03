package com.apollopharmacy.vishwam.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class EmployeeDetailsResponse : Serializable {
    @SerializedName("message")
    @Expose
    var message: Any? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("zcServerDateTime")
    @Expose
    var zcServerDateTime: String? = null

    @SerializedName("zcServerIp")
    @Expose
    var zcServerIp: String? = null

    @SerializedName("zcServerHost")
    @Expose
    var zcServerHost: String? = null

    inner class Data : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("upload_swach")
        @Expose
        var uploadSwach: UploadSwach? = null

        @SerializedName("swacch_default_site")
        @Expose
        var swacchDefaultSite: SwacchDefaultSite? = null


        inner class UploadSwach : Serializable {
            @SerializedName("uid")
            @Expose
            var uid: String? = null

            @SerializedName("name")
            @Expose
            var name: String? = null

            @SerializedName("other")
            @Expose
            var other: Other? = null

            @SerializedName("icon")
            @Expose
            var icon: Any? = null

            inner class Other : Serializable {
                @SerializedName("color")
                @Expose
                var color: Any? = null


            }
        }

        inner class SwacchDefaultSite : Serializable {
            @SerializedName("uid")
            @Expose
            var uid: String? = null

            @SerializedName("site")
            @Expose
            var site: String? = null
        }
    }
}
