package com.apollopharmacy.vishwam.data.model.discount

import com.google.gson.annotations.SerializedName

data class BillOrderResponse(

	@field:SerializedName("MESSAGE")
	val mESSAGE: String? = null,

	@field:SerializedName("STATUS")
	val sTATUS: Boolean,

	@field:SerializedName("BILLDEATILS")
	val bILLDEATILS: ArrayList<BILLDEATILSItem>
)

data class ITEMSItem(

	@field:SerializedName("BILLEDQTY")
	val bILLEDQTY: Int? = null,

	@field:SerializedName("REQUESTEDQTY")
	val rEQUESTEDQTY: Int,

	@field:SerializedName("BILLEDDISCPERCENTAGE")
	val bILLEDDISCPERCENTAGE: Double? = null,

	@field:SerializedName("ITEMCODE")
	val iTEMCODE: String? = null,

	@field:SerializedName("BATCHNO")
	val bATCHNO: String? = null,

	@field:SerializedName("ITEMNAME")
	val iTEMNAME: String? = null,

	@field:SerializedName("APPROVEDDSICPERCENTAGE")
	val aPPROVEDDSICPERCENTAGE: Double? = null
)

data class BILLDEATILSItem(

	@field:SerializedName("APPROVEDVALUE")
	val aPPROVEDVALUE: Double? = null,

	@field:SerializedName("BILLEDDATE")
	val bILLEDDATE: String,

	@field:SerializedName("APPROVEDDATE")
	val aPPROVEDDATE: String,

	@field:SerializedName("STOREID")
	val sTOREID: String? = null,

	@field:SerializedName("FINALBILLEDVALUE")
	val fINALBILLEDVALUE: Double? = null,

	@field:SerializedName("INDENTNO")
	val iNDENTNO: String,

	@field:SerializedName("ITEMS")
	val iTEMS: List<ITEMSItem>
)
