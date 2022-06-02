package com.apollopharmacy.vishwam.data.model.discount

import java.io.Serializable

data class PendingOrder(
    val MESSAGE: String? = null,
    val STATUS: Boolean,
    val APPROVEDLIST: List<APPROVEDLISTItem?>? = null,
    val DESIGNATION: String? = null,
    val PENDINGLIST: ArrayList<PENDINGLISTItem>,
    val REMARKSLIST: Any? = null
) : Serializable {

    data class STATUSItem(
        val STATUS: String,
        var EMPID: String,
        val EMPNAME: String?
    ) : Serializable

    data class REMARKSItem(
        val CREATEDDATE: String,
        var REMARKS: String,
        val EMPNAME: String
    ) : Serializable

    data class ITEMSItem(
        var APPROVED_DISC: Double? = null,
        val PRICE: Double,
        val CATEGORY: String? = null,
        val QTY: Int,
        var ITEMID: String,
        var REQUEST_DISC: Double? = null,
        val MARGIN: Double,
        val ITEMNAME: String? = null,
        var INDENTNO: String,
        var ORIGINAL_DISC: Double? = null
    ) : Serializable

    data class APPROVEDLISTItem(
        val TRACKINGREFNAME: String? = null,
        val BULKDISCOUNTPER: String? = null,
        val rEMARKS: List<REMARKSItem>,
        val TRACKINGREFCODE: String? = null,
        val ITEMS: List<ITEMSItem?>? = null,
        val TERMINALID: String? = null,
        val STORENAME: Any? = null,
        val STATUS: String? = null,
        val DCCODE: Any? = null,
        val POSTEDDATE: String? = null,
        val CUSTNAME: String? = null,
        val STORE: String? = null,
        val USERID: String? = null,
        val INDENTNO: String? = null,
        val ISMARGIN: Int,
        val TELNO: String? = null,
        val REMARK: String? = null
    ) : Serializable

    data class PENDINGLISTItem(
        val TRACKINGREFNAME: String,
        val BULKDISCOUNTPER: String,
        val REMARKS: List<REMARKSItem>,
        val TRACKINGREFCODE: String,
        val ITEMS: List<ITEMSItem>,
        val TERMINALID: String,
        val STORENAME: String,
        val STATUS: String,
        val DCCODE: String,
        val POSTEDDATE: String,
        val CUSTNAME: String,
        val STORE: String,
        val USERID: String,
        val INDENTNO: String,
        val ISMARGIN: Int,
        val TELNO: String,
        var REMARK: String? = null,
        val DCNAME: String?,
        val STATUSLIST: ArrayList<STATUSItem>,
        var isItemChecked:Boolean = false
    ) : Serializable
}
