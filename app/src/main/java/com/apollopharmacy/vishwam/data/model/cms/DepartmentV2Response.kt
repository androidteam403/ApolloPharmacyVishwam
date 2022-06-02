package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DepartmentV2Response(

    @field:SerializedName("departmentList")
    val departmentList: ArrayList<DepartmentListItem>,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Serializable {

    data class DepartmentListItem(

        @field:SerializedName("departmentName")
        var departmentName: String? = null,

        @field:SerializedName("departmentid")
        val departmentid: Int? = null,

        @field:SerializedName("departmentkey")
        val departmentKey: String? = null,

        @field:SerializedName("categories")
        val categories: ArrayList<CategoriesItem>
    ) : Serializable

    data class SubcategoryItem(
        @field:SerializedName("subCategoryName")
        val subCategoryName: String? = null,

        val uid: String? = null,

    ) : Serializable

    data class CategoriesItem(

        @field:SerializedName("subcategory")
        val subcategory: ArrayList<SubcategoryItem>,

        @field:SerializedName("categoryName")
        val categoryName: String? = null,

       // @field:SerializedName("categoryName")
        val uid: String? = null,
    ) : Serializable
}