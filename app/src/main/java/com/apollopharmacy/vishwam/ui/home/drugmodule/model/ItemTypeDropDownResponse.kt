package com.apollopharmacy.vishwam.ui.home.drugmodule.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ItemTypeDropDownResponse(
    @field:SerializedName("data") val data: Data? = null,
    @field:SerializedName("success") val success: Boolean,
) : Serializable {
    data class Data(
        @field:SerializedName("listData") val listData: ListData,
    ) : Serializable

    data class ListData(
        @field:SerializedName("rows") val rows: ArrayList<Rows>,
    ) : Serializable

    data class Rows(
        @field:SerializedName("name") val name: String,
        @field:SerializedName("uid") val uid: String,
    ) : Serializable
}
