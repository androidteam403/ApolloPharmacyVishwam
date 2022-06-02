package com.apollopharmacy.vishwam.data.model.discount

data class BulkAcceptOrRejectDiscountOrder(
    val ORDERS: ArrayList<ORDER>,
    val TYPE: String
) {
    data class ITEMSItem(
        val aPPROVEDDISC: Int,
        val pRICE: Int,
        val cATEGORY: String,
        val qTY: Int,
        val iNDENTNO: String,
        val iTEMID: String,
        val rEQUESTDISC: Int,
        val iTEMNAME: String
    )

    data class ORDER(
        var TRACKINGREFNAME: String,
        var BULKDISCOUNTPER: String,
        var TRACKINGREFCODE: String,
        var ITEMS: List<PendingOrder.ITEMSItem>,
        var TERMINALID: String,
        var STORENAME: String? = null,
        var STATUS: String,
        var DCCODE: String? = null,
        var POSTEDDATE: String,
        var CUSTNAME: String,
        var STORE: String,
        var USERID: String,
        var INDENTNO: String,
        var TELNO: String
    )
}

