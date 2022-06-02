package com.apollopharmacy.vishwam.data.model.discount

data class AcceptOrRejectDiscountOrder(
	val ORDERS: ORDER,
	val REMARKS: String? = null,
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
		val TRACKINGREFNAME: String,
		val BULKDISCOUNTPER: String,
		val TRACKINGREFCODE: String,
		val ITEMS: List<PendingOrder.ITEMSItem>,
		val TERMINALID: String,
		val STORENAME: String? = null,
		val STATUS: String,
		val DCCODE: String? = null,
		val POSTEDDATE: String,
		val CUSTNAME: String,
		val STORE: String,
		val USERID: String,
		val INDENTNO: String,
		val TELNO: String
	)
}

