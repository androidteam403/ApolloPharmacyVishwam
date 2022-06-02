package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName

data class ArticleCodeResponse(

	@field:SerializedName("data")
	val data: ArrayList<DataItem>,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) {

    data class DataItem(

		@field:SerializedName("artCodeName")
		val artCodeName: String? = null,

		@field:SerializedName("artcode")
		val artcode: String? = null
	)
}

