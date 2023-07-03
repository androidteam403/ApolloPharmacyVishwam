package com.apollopharmacy.vishwam.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginDetails(
    @SerializedName("STATUS")
    @Expose
    val STATUS: Boolean,

    @SerializedName("MESSAGE")
    @Expose
    val MESSAGE: String,

    @SerializedName("EMPID")
    @Expose
    val EMPID: String,

    @SerializedName("EMPNAME")
    @Expose
    val EMPNAME: String,

    @SerializedName("DESIGNATION")
    @Expose
    val DESIGNATION: String,

    @SerializedName("DISCOUNTLIMIT")
    @Expose
    val DISCOUNTLIMIT: String,

    @SerializedName("IsHavingStore")
    @Expose
    val IsHavingStore: Boolean,

    @SerializedName("STOREDETAILS")
    @Expose
    val STOREDETAILS: ArrayList<StoreData>,

    @SerializedName("IS_DISCOUNTAPP")
    @Expose
    val IS_DISCOUNTAPP: Boolean,

    @SerializedName("IS_CMSAPP")
    @Expose
    val IS_CMSAPP: Boolean,

    @SerializedName("IS_ATTANDENCEAPP")
    @Expose
    val IS_ATTANDENCEAPP: Boolean,

    @SerializedName("APPLEVELDESIGNATION")
    @Expose
    var APPLEVELDESIGNATION: String,

    @SerializedName("IS_SUPERADMIN")
    @Expose
    val IS_SUPERADMIN: Boolean,

    @SerializedName("IS_SWACHHAPP")
    @Expose
    val IS_SWACHHAPP: Boolean,

    @SerializedName("IS_QCFAILAPP")
    @Expose
    val IS_QCFAILAPP: Boolean,

    @SerializedName("IS_NEWDRUGAPP")
    @Expose
    val IS_NEWDRUGAPP: Boolean,

    @SerializedName("IS_SENSINGAPP")
    @Expose
    val IS_SENSINGAPP: Boolean,

    ) : Serializable {
    data class StoreData(
        @SerializedName("SITEID")
        @Expose
        val SITEID: String,

        @SerializedName("SITENAME")
        @Expose
        val SITENAME: String,

        @SerializedName("DCNAME")
        @Expose
        val DCNAME: String,

        @SerializedName("STATEID")
        @Expose
        val STATEID: String,

        @SerializedName("DC")
        @Expose
        val DC: String,

        @SerializedName("IsSelectedStore")
        @Expose
        val IsSelectedStore: Boolean,
    )
}


//package com.apollopharmacy.vishwam.data.model
//
//import java.io.Serializable
//
//data class LoginDetails(
//    val STATUS: Boolean,
//    val MESSAGE: String,
//    val EMPID: String,
//    val EMPNAME: String,
//    val DESIGNATION: String,
//    val DISCOUNTLIMIT: String,
//    val IsHavingStore: Boolean,
//    val STOREDETAILS: ArrayList<StoreData>,
//    val IS_DISCOUNTAPP: Boolean,
//    val IS_CMSAPP: Boolean,
//    val IS_ATTANDENCEAPP: Boolean,
//    var APPLEVELDESIGNATION: String,
//    val IS_SUPERADMIN: Boolean,
//    val IS_SWACHHAPP:Boolean,
//    val IS_QCFAILAPP: Boolean,
//    val IS_NEWDRUGAPP: Boolean,
//
//
//    ) : Serializable {
//    data class StoreData(
//        val SITEID: String,
//        val SITENAME: String,
//        val DCNAME: String,
//        val STATEID: String,
//        val DC: String,
//        val IsSelectedStore: Boolean,
//    )
//}
//
