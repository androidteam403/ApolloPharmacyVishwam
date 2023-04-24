package com.apollopharmacy.vishwam.data.model.cms

import com.apollopharmacy.vishwam.dialog.model.Row
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

    @field:SerializedName("ticket_it")
    val ticket_it: Ticket_it?
) : Serializable {
    data class Platform(
        @field:SerializedName("uid")
        val uid: String ,
    ) : Serializable

    data class Category(
        @field:SerializedName("uid")
        val uid: String? = null,
        @field:SerializedName("code")
        val code: String? = null

    ) : Serializable

    data class Department(
        @field:SerializedName("uid")
        val uid: String? = null,
        @field:SerializedName("code")
        val code: String? = null
    ) : Serializable

    data class  Site(
        @field:SerializedName("site")
        val uid: String? = null
    ):Serializable

    data class  Reason(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("reason_sla")
        val reason_sla: ArrayList<ReasonmasterV2Response.Reason_SLA>
    ):Serializable

    data class  Subcategory(
        @field:SerializedName("uid")
        val uid: String? = null
    ):Serializable

    data class  ProblemImages(
        @field:SerializedName("images")
        val images: List<Image>
    ):Serializable

    data class Image(
        @field:SerializedName("url")
        val url: String? = null
    ):Serializable

}

data class Ticket_it(
    @field:SerializedName("tid")
    var tid: Row,
    @field:SerializedName("bill_number")
    val bill_number: String,
    @field:SerializedName("transaction_id")
    val transaction_id: String,
    @field:SerializedName("approval_code")
    val approval_code: String,
    @field:SerializedName("bill_amount")
    var bill_amount: Double
)





