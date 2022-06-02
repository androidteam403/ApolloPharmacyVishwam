package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SiteDto(

	@field:SerializedName("Status")
	val status: Boolean,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("StoreList")
	val storeList: List<StoreListItem>
)

data class StoreListItem(

	@field:SerializedName("SITENAME")
	val sITENAME: String? = null,
	@field:SerializedName("STATEID")
	val sTATEID: String? = null,

	@field:SerializedName("DCNAME")
	val DcName: String? = null,

	@field:SerializedName("SITEID")
	val sITEID: String? = null,

	@field:SerializedName("DC")
	val dcId: String? = null
) : Serializable
