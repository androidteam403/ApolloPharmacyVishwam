package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SiteDto(

    @field:SerializedName("success")
    val status: Boolean,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val siteData: SiteData? = null,

//	@field:SerializedName("StoreList")
//	val storeList: List<StoreListItem>
) : Serializable
    data class SiteData(
        @field:SerializedName("listData")
        val listData: ListData? = null
    ) : Serializable

    data class ListData(
        @field:SerializedName("rows")
        val rows: List<StoreListItem>
    ) : Serializable


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
        val dcId: String? = null,

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("store_name")
        val store_name: String? = null,

        var isSelected: Boolean? = false,

        @field:SerializedName("site")
        val site: String? = null,


        @field:SerializedName("dc_code")
        val dc_code: DcCode? = null

    ) : Serializable

    data class DcCode(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("code")
        val code: String? = null,
    ) : Serializable

