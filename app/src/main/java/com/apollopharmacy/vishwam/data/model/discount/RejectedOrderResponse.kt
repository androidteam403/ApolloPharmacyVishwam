package com.apollopharmacy.vishwam.data.model.discount

data class RejectedOrderResponse(
    val PENDINGLIST: List<PENDINGLISTItem>,
    val APPROVEDLIST: List<APPROVEDLISTItem>,
    val REJECTEDLIST: ArrayList<REJECTEDLISTItem>,
    val DESIGNATION: String?,
    val REMARKSLIST: Any,
    val STATUS: Boolean,
    val MESSAGE: String,
) {

    data class APPROVEDLISTItem(
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
        val TELNO: String,
        val REMARK: String,
    )

    data class REJECTEDLISTItem(
        val TRACKINGREFCODE: String? = null,
        val TRACKINGREFNAME: String? = null,
        val CUSTNAME: String? = null,
        val TELNO: String? = null,
        val STORE: String,
        val TERMINALID: String? = null,
        val USERID: String? = null,
        val BULKDISCOUNTPER: String,
        val POSTEDDATE: String,
        val INDENTNO: String,
        val ISMARGIN: Int,
        val STATUS: String? = null,
        val STORENAME: String? = null,
        val DCCODE: String? = null,
        val DCNAME: String?= null,
        val REMARK: String? = null,
        val ITEMS: List<ITEMSItem>,
        val REMARKS: List<REMARKSItem>,
        val STATUSLIST: ArrayList<PendingOrder.STATUSItem>,
    )

    data class PENDINGLISTItem(
        val TRACKINGREFNAME: String,
        val BULKDISCOUNTPER: String,
        val REMARKS: List<Any>,
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
        val TELNO: String,
        val REMARK: String,
    )

    data class REMARKSItem(
        val CREATEDDATE: String,
        val REMARKS: String,
        val EMPNAME: String,
    )

    data class ITEMSItem(
        val ITEMID:  String? = null,
        val ITEMNAME: String? = null,
        val REQUESTDISC: Double,
        val MARGIN: Double,
        val APPROVEDDISC: Double,
        val CATEGORY: String? = null,
        val PRICE: Double,
        val QTY: Double,
        val ORIGINAL_DISC: Double,
    )
}

