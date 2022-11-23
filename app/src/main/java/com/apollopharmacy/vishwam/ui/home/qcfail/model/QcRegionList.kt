package com.apollopharmacy.vishwam.ui.home.qcfail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QcRegionList : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("STORELIST")
    @Expose
    var storelist: List<Store>? = null
    fun withStatus(status: Boolean?): QcRegionList {
        this.status = status
        return this
    }

    fun withMessage(message: String?): QcRegionList {
        this.message = message
        return this
    }

    fun withStorelist(storelist: List<Store>?): QcRegionList {
        this.storelist = storelist
        return this
    }

     class Store {
        @SerializedName("SITEID")
        @Expose
        var siteid: String? = null

        @SerializedName("SITENAME")
        @Expose
        var sitename: String? = null
         var isClick: Boolean =false

         fun setisClick(pos: Boolean) {
             isClick = pos
         }

        fun withSiteid(siteid: String?): Store {
            this.siteid = siteid
            return this
        }

        fun withSitename(sitename: String?): Store {
            this.sitename = sitename
            return this
        }
    }
}