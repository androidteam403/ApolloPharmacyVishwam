package com.apollopharmacy.vishwam.data.model

import java.io.Serializable

data class LoginDetails(
    val STATUS: Boolean,
    val MESSAGE: String,
    val EMPID: String,
    val EMPNAME: String,
    val DESIGNATION: String,
    val DISCOUNTLIMIT: String,
    val IsHavingStore: Boolean,
    val STOREDETAILS: ArrayList<StoreData>,
    val IS_DISCOUNTAPP: Boolean,
    val IS_CMSAPP: Boolean,
    val IS_ATTANDENCEAPP: Boolean,
    var APPLEVELDESIGNATION: String,
    val IS_SUPERADMIN: Boolean,
) : Serializable {
    data class StoreData(
        val SITEID: String,
        val SITENAME: String,
        val DCNAME: String,
        val STATEID: String,
        val DC: String,
        val IsSelectedStore: Boolean,
    )
}

