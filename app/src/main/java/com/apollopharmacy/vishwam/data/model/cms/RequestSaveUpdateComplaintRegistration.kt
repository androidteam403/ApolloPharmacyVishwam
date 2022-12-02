package com.apollopharmacy.vishwam.data.model.cms

import com.apollopharmacy.vishwam.ui.home.cms.registration.model.Data
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.FetchItemModel
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.FileResposne
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import com.google.gson.annotations.Expose

data class RequestSaveUpdateComplaintRegistration(

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

//    @field:SerializedName("problem_images")
//    val problem_images: ProblemImages,

    @field:SerializedName("ticket_inventory")
    val ticket_inventory: TicketInventory?,

    @field:SerializedName("ticket_type")
    val ticket_type: TicketType?,

    @field:SerializedName("region")
    val region: ResponseTicktResolvedapi.Region?,

    @field:SerializedName("cluster")
    val cluster: ResponseTicktResolvedapi.Cluster?,

    @field:SerializedName("site_phone_no")
    val site_phone_no: String?,

    @field:SerializedName("executive")
    val executive: ResponseTicktResolvedapi.Executive?,

    @field:SerializedName("manager")
    val manager: ResponseTicktResolvedapi.Manager?,

    @field:SerializedName("region_head")
    val region_head: ResponseTicktResolvedapi.Region_head?,

//    @field:SerializedName("prefix")
//    val prefix: String?,


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
        val uid: String? = null,
        @field:SerializedName("code")
        val code: String? = null
    ) : Serializable

    data class  Site(
        @field:SerializedName("uid")
        val uid: String? = null,
        @field:SerializedName("site")
        val site: String? = null,
        @field:SerializedName("store_name")
        val store_name: String? = null,
    ):Serializable

    data class  Reason(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("reason_sla")
        val reason_sla: ArrayList<ReasonmasterV2Response.Reason_SLA>?
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

    data class TicketInventory(
        @field:SerializedName("ticket_inventory_item")
        val ticket_inventory_item: ArrayList<TicketInventoryItem>?,
        @field:SerializedName("drug_request")
        val drug_request: ArrayList<DrugRequest>?,
        @field:SerializedName("code_batch")
        val code_batch: CodeBatch?
    ):Serializable

    data class TicketInventoryItem(
        @field:SerializedName("front_img_blob")
        var front_image: String? = null,
        @field:SerializedName("back_img_blob")
        var back_image: String? = null,
        @field:SerializedName("other_img_blob")
        var other_image: String? = null,
        @field:SerializedName("itemCode")
        val itemCode: FetchItemModel.Rows? = null,
        @field:SerializedName("item_code")
        val item_code: String? = null,
        @field:SerializedName("batch_no")
        val batch_no: String? = null,
        @field:SerializedName("barcode")
        val barcode: String? = null,
        @field:SerializedName("expiry_date")
        val expiry_date: String? = null,
        @field:SerializedName("purchase_rate")
        val purchase_rate: Double?  = 0.0,
        @field:SerializedName("old_mrp")
        val old_mrp: Double? = 0.0,
        @field:SerializedName("new_mrp")
        val new_mrp: Double? = 0.0,
        @field:SerializedName("mrp")
        val mrp: Double? = 0.0,
        @field:SerializedName("rowIndex")
        val rowIndex: Int? = 0,
    ):Serializable

    data class CodeBatch(
        @field:SerializedName("uid")
        val uid: String? = null,
        @field:SerializedName("name")
        val name: String? = null,
    ):Serializable

    data class TicketType(
        @field:SerializedName("uid")
        val uid: String? = null,
        @field:SerializedName("name")
        val name: String? = null,
        @field:SerializedName("code")
        val code: String? = null,

    ):Serializable


    data class DrugRequest(


        @Expose
        @SerializedName("front_mb")
        val front_mb: String?,
        @Expose
        @SerializedName("back_mb")
        val back_mb: String?,
        @Expose
        @SerializedName("side_mb")
        val side_mb: String?,
        @Expose
        @SerializedName("bill_mb")
        val bill_mb: String?,
        @Expose
        @SerializedName("batch_no")
        val batch_no: String?,
        @Expose
        @SerializedName("barcode")
        val barcode: String?,
        @Expose
        @SerializedName("manufacturing_date")
        val manufacturing_date: String?,
        @Expose
        @SerializedName("expiry_date")
        val expiry_date: String?,
        @Expose
        @SerializedName("purchase_price")
        val purchase_price: Double?,
        @Expose
        @SerializedName("mrp")
        val mrp: Double?,
        @Expose
        @SerializedName("reference_no")
        val reference_no: String?,
        @Expose
        @SerializedName("pack_size")
        val pack_size: String?,
        @Expose
        @SerializedName("hsn_code")
        val hsn_code: String?,
        @Expose
        @SerializedName("gst")
        val gst: Double?,
        @Expose
        @SerializedName("item_name")
        val item_name: String?,
        @Expose
        @SerializedName("remarks")
        val remarks: String?
    )
}



