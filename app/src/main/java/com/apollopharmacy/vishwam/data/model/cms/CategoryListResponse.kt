package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName

data class CategoryListResponse(

	@field:SerializedName("categoryList")
	val categoryList: ArrayList<CategoryListItem>,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) {

    data class CategoryListItem(

		@field:SerializedName("departmentName")
		val departmentName: String? = null,

		@field:SerializedName("subcategortName")
		val subcategortName: String,

		@field:SerializedName("categoryName")
		val categoryName: String
	)
}
