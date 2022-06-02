package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RequestNewComplaintRegistration(

    @field:SerializedName("employee_id")
    val employee_id: String? = null,

    @field:SerializedName("date_of_occurrence")
    val date_of_occurrence: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("platform")
    val platform: Platform,

    @field:SerializedName("category")
    val category: Category,

    @field:SerializedName("department")
    val department: Department,

    @field:SerializedName("site")
    val site: Site,

    @field:SerializedName("reason")
    val reason: Reason,

    @field:SerializedName("subcategory")
    val subcategory: Subcategory,

    @field:SerializedName("problem_images")
    val problem_images: ProblemImages,
) : Serializable {
    data class Platform(
        @field:SerializedName("uid")
        val uid: String ,
    ) : Serializable

    data class Category(
        @field:SerializedName("uid")
        val uid: String? = null,
    ) : Serializable

    data class Department(
        @field:SerializedName("uid")
        val uid: String? = null
    ) : Serializable

    data class  Site(
        @field:SerializedName("site")
        val uid: String? = null
    ):Serializable

    data class  Reason(
        @field:SerializedName("uid")
        val uid: String? = null
    ):Serializable

    data class  Subcategory(
        @field:SerializedName("uid")
        val uid: String? = null
    ):Serializable

    data class  ProblemImages(
        @field:SerializedName("images")
        val images: ArrayList<Image>
    ):Serializable

    data class Image(
        @field:SerializedName("url")
        val url: String? = null
    ):Serializable

}



