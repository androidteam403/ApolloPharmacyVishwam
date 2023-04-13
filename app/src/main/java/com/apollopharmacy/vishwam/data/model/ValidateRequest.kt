package com.apollopharmacy.vishwam.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateRequest(

	@SerializedName("DEVICEID") @Expose val DEVICEID: String,
	@SerializedName("KEY") @Expose val KEY: String
)

