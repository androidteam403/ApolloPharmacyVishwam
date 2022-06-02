package com.apollopharmacy.vishwam.data.model.discount

data class ApprovalOrderRequest(
    val MESSAGE: String? = null,
    val STATUS: Boolean,
    val APPROVEDLIST: ArrayList<APPROVEDLISTItem>,
    val DESIGNATION: String,
    val PENDINGLIST: List<PENDINGLISTItem>,
    val REMARKSLIST: Any? = null,
) {

    data class REMARKSItem(
        val CREATEDDATE: String,
        val REMARKS: String? = null,
        val EMPNAME: String? = null,
    )

    data class ITEMSItem(
        val APPROVED_DISC: Double,
        val PRICE: Double,
        val CATEGORY: String? = null,
        val QTY: Double,
        val ITEMID: String? = null,
        val REQUEST_DISC: Double,
        val MARGIN: Double,
        val ITEMNAME: String? = null,
    )

    data class APPROVEDLISTItem(
        val TRACKINGREFNAME: String? = null,
        val BULKDISCOUNTPER: String? = null,
        val REMARKS: List<REMARKSItem>,
        val TRACKINGREFCODE: String? = null,
        val ITEMS: List<ITEMSItem>,
        val TERMINALID: String? = null,
        val STORENAME: String? = null,
        val STATUS: String? = null,
        val DCCODE: String? = null,
        val POSTEDDATE: String,
        val CUSTNAME: String? = null,
        val STORE: String,
        val USERID: String? = null,
        val INDENTNO: String,
        val ISMARGIN: Int,
        val DCNAME: String?,
        val STATUSLIST: ArrayList<PendingOrder.STATUSItem>,
        val TELNO: String? = null,
    )

    data class PENDINGLISTItem(
        val tRACKINGREFNAME: String? = null,
        val bULKDISCOUNTPER: String? = null,
        val rEMARKS: List<Any?>? = null,
        val tRACKINGREFCODE: String? = null,
        val iTEMS: List<ITEMSItem?>? = null,
        val tERMINALID: String? = null,
        val sTORENAME: String? = null,
        val sTATUS: String? = null,
        val dCCODE: String? = null,
        val pOSTEDDATE: String? = null,
        val cUSTNAME: String? = null,
        val sTORE: String? = null,
        val uSERID: String? = null,
        val iNDENTNO: String? = null,
        val tELNO: String? = null,
    )
}

