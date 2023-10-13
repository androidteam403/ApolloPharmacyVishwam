package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

import com.apollopharmacy.vishwam.data.model.cms.ReasonDept
import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.apollopharmacy.vishwam.data.model.cms.TicketInventoryItem
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TicketDetailsResponse(

    @Expose
    @SerializedName("message")
    val message: String,
    @Expose
    @SerializedName("success")
    val success: Boolean,
    @Expose
    @SerializedName("data")
    var data: TicketData,
    var position: Int,
):java.io.Serializable

data class TicketData(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("description")
    var description: String,
    @Expose
    @SerializedName("platform")
    val platform: Platform,
    @Expose
    @SerializedName("priority")
    val priority: Priority,
    @Expose
    @SerializedName("site_phone_no")
    val site_phone_no: String,
    @Expose
    @SerializedName("store_explanation")
    val store_explanation: String,
    @Expose
    @SerializedName("ticket_id")
    val ticket_id: String,
    @Expose
    @SerializedName("cluster")
    val cluster: Cluster,
    @Expose
    @SerializedName("level")
    val level: Level,
    @Expose
    @SerializedName("category")
    val category: Category,
    @Expose
    @SerializedName("region")
    val region: Region,
    @Expose
    @SerializedName("department")
    val department: TicketDepartment,
    @Expose
    @SerializedName("created_id")
    val created_id: Created_id,
    @Expose
    @SerializedName("executive")
    val executive: Executive,
    @Expose
    @SerializedName("ou")
    val ou: Ou,
    @Expose
    @SerializedName("location")
    val location: Location,
    @Expose
    @SerializedName("reason")
    val reason: Reason,
    @Expose
    @SerializedName("parent")
    val parent: ResponseNewTicketlist.Parent,
    @Expose
    @SerializedName("site")
    val site: TicketSite,
    @Expose
    @SerializedName("subcategory")
    val subcategory: Subcategory,
    @Expose
    @SerializedName("status")
    val status: Status,
    @Expose
    @SerializedName("ticket_inventory")
    val ticket_inventory: Ticket_inventory,
    @Expose
    @SerializedName("ticket_type")
    val ticket_type: Ticket_type,
    @Expose
    @SerializedName("ticket_it")
    val ticket_it: Ticket_it,
    @Expose
    @SerializedName("manager")
    val manager: TicketManager,
    @Expose
    @SerializedName("user")
    val user: TicketUser,
    @Expose
    @SerializedName("modified_time")
    val modified_time: String,
    @Expose
    @SerializedName("closed_date")
    val closed_date: String,
    @Expose
    @SerializedName("date_of_occurrence")
    val date_of_occurrence: String,
    @Expose
    @SerializedName("created_time")
    val created_time: String,
    @Expose
    @SerializedName("problem_images")
    val problem_images: ResponseNewTicketlist.ProblemImages? = null,
): Serializable

data class TicketUser(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("first_name")
    val first_name: String,
    @Expose
    @SerializedName("last_name")
    val last_name: String,
    @Expose
    @SerializedName("middle_name")
    val middle_name: String,
    @Expose
    @SerializedName("login_unique")
    val login_unique: String,
    @Expose
    @SerializedName("role")
    val role: Role,
    @Expose
    @SerializedName("level")
    val level: Level,
): Serializable

//
//data class Level(
//    @Expose
//    @SerializedName("uid")
//    val uid: String
//)
//
//data class Role(
//    @Expose
//    @SerializedName("uid")
//    val uid: String,
//    @Expose
//    @SerializedName("name")
//    val name: String
//)
//
//data class Manager(
//    @Expose
//    @SerializedName("uid")
//    val uid: String,
//    @Expose
//    @SerializedName("phone")
//    val phone: String,
//    @Expose
//    @SerializedName("email")
//    val email: String,
//    @Expose
//    @SerializedName("first_name")
//    val first_name: String,
//    @Expose
//    @SerializedName("last_name")
//    val last_name: String,
//    @Expose
//    @SerializedName("middle_name")
//    val middle_name: String,
//    @Expose
//    @SerializedName("login_unique")
//    val login_unique: String
//)
//
data class Ticket_it(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("approval_code")
    val approval_code: String,
    @Expose
    @SerializedName("asset_tag_id")
    val asset_tag_id: String,
    @Expose
    @SerializedName("bill_number")
    val bill_number: String,
    @Expose
    @SerializedName("branch_mail_id")
    val branch_mail_id: String,
    @Expose
    @SerializedName("status")
    val status: Status,
    @Expose
    @SerializedName("ticket_type")
    val ticket_type: Ticket_type,
    @Expose
    @SerializedName("transaction_id")
    val transaction_id: String,
    @Expose
    @SerializedName("tid")
    val tid: Tid,
    @Expose
    @SerializedName("acted_by")
    val acted_by: Acted_by,
    @Expose
    @SerializedName("bill_amount")
    val bill_amount: String,
    @Expose
    @SerializedName("acted_on")
    val acted_on: String,
): Serializable

//
data class Acted_by(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("phone")
    val phone: String,
    @Expose
    @SerializedName("email")
    val email: String,
    @Expose
    @SerializedName("first_name")
    val first_name: String,
    @Expose
    @SerializedName("last_name")
    val last_name: String,
    @Expose
    @SerializedName("middle_name")
    val middle_name: String,
    @Expose
    @SerializedName("login_unique")
    val login_unique: String,
): Serializable

//
data class Tid(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("tid")
    val tid: String,
): Serializable

//
data class Ticket_type(
    @Expose
    @SerializedName("uid")
    val uid: String,
): Serializable

//
//data class Status(
//    @Expose
//    @SerializedName("uid")
//    val uid: String
//)
//
//data class Ticket_type(
//    @Expose
//    @SerializedName("uid")
//    val uid: String,
//    @Expose
//    @SerializedName("name")
//    val name: String,
//    @Expose
//    @SerializedName("code")
//    val code: String
//)
//
data class Ticket_inventory(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("code_batch")
    val code_batch: Code_batch,
    @Expose
    @SerializedName("drug_request")
    val drug_request: Drug_request,
    @Expose
    @SerializedName("ticket_inventory_item")
    val ticket_inventory_item: List<TicketInventoryItem>,
): Serializable

//
data class Drug_request(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("back_mb")
    val back_mb: String,
    @Expose
    @SerializedName("barcode")
    val barcode: String,
    @Expose
    @SerializedName("batch_no")
    val batch_no: String,
    @Expose
    @SerializedName("bill_mb")
    val bill_mb: String,
    @Expose
    @SerializedName("doctor_specialty")
    val doctor_specialty: Doctor_specialty,
    @Expose
    @SerializedName("doctors_name")
    val doctors_name: String,
    @Expose
    @SerializedName("front_mb")
    val front_mb: String,
    @Expose
    @SerializedName("hsn_code")
    val hsn_code: String,
    @Expose
    @SerializedName("item_name")
    val item_name: String,
    @Expose
    @SerializedName("item_type")
    val item_type: Item_type,
    @Expose
    @SerializedName("pack_size")
    val pack_size: Int,
    @Expose
    @SerializedName("reference_no")
    val reference_no: String,
    @Expose
    @SerializedName("remarks")
    val remarks: String,
    @Expose
    @SerializedName("required_quantity")
    val required_quantity: Int,
    @Expose
    @SerializedName("side_mb")
    val side_mb: String,
    @Expose
    @SerializedName("expiry_date")
    val expiry_date: String,
    @Expose
    @SerializedName("front")
    val front: List<String>,
    @Expose
    @SerializedName("manufacturing_date")
    val manufacturing_date: String,
    @Expose
    @SerializedName("mrp")
    val mrp: Int,
    @Expose
    @SerializedName("gst")
    val gst: Int,
    @Expose
    @SerializedName("purchase_price")
    val purchase_price: Int,
    @Expose
    @SerializedName("bill")
    val bill: List<String>,
    @Expose
    @SerializedName("side")
    val side: List<String>,
): Serializable

//
data class Item_type(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("name")
    val name: String,

    ): Serializable

//
data class Doctor_specialty(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("name")
    val name: String,
): Serializable

//
data class Code_batch(
    @Expose
    @SerializedName("uid")
    val uid: String,
): Serializable

//
//data class Status(
//    @Expose
//    @SerializedName("uid")
//    val uid: String,
//    @Expose
//    @SerializedName("background_color")
//    val background_color: String,
//    @Expose
//    @SerializedName("code")
//    val code: String,
//    @Expose
//    @SerializedName("name")
//    val name: String,
//    @Expose
//    @SerializedName("text_color")
//    val text_color: String
//)
//
data class Subcategory(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("code")
    val code: String,
    @Expose
    @SerializedName("name")
    val name: String,
): Serializable

//
data class TicketSite(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("phone_no")
    val phone_no: String,
    @Expose
    @SerializedName("site")
    val site: String,
    @Expose
    @SerializedName("site_type")
    val site_type: Site_type,
    @Expose
    @SerializedName("store_name")
    val store_name: String,
    @Expose
    @SerializedName("region")
    val region: Region,
    @Expose
    @SerializedName("ou")
    val ou: Ou,
    @Expose
    @SerializedName("hod")
    val hod: Hod,
    @Expose
    @SerializedName("manager")
    val manager: Manager,
): Serializable

//
data class TicketManager(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("phone")
    val phone: String,
    @Expose
    @SerializedName("email")
    val email: String,
    @Expose
    @SerializedName("first_name")
    val first_name: String,
    @Expose
    @SerializedName("last_name")
    val last_name: String,
    @Expose
    @SerializedName("middle_name")
    val middle_name: String,
    @Expose
    @SerializedName("login_unique")
    val login_unique: String,
): Serializable

//
data class Hod(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("first_name")
    val first_name: String,
): Serializable

//
data class Ou(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("code")
    val code: String,
    @Expose
    @SerializedName("name")
    val name: String,
): Serializable

//
data class Region(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("name")
    val name: String,
): Serializable

//
data class Site_type(
    @Expose
    @SerializedName("uid")
    val uid: String,
): Serializable

//
//data class Parent(
//    @Expose
//    @SerializedName("uid")
//    val uid: String,
//    @Expose
//    @SerializedName("ticket_id")
//    val ticket_id: String
//)
//
data class Reason(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("code")
    val code: String,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("sub_workflow")
    val sub_workflow: Sub_workflow,
    @Expose
    @SerializedName("reason_sla")
    val reason_sla: List<Reason_sla>,
    @Expose
    @SerializedName("reason_dept")
    val reason_dept: List<ReasonDept>,

    @Expose
    @SerializedName("allow_manual_ticket_closure")
    val allow_manual_ticket_closure: AllowManualTicketClosure,

    ): Serializable

data class AllowManualTicketClosure(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("other")
    val other: Other,

    @Expose
    @SerializedName("icon")
    val icon: String,

    ): Serializable

//
data class Reason_sla(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("sla_status")
    val sla_status: Sla_status,
): Serializable

//
data class Sla_status(
    @Expose
    @SerializedName("uid")
    val uid: String,
): Serializable

//
data class Sub_workflow(
    @Expose
    @SerializedName("uid")
    val uid: String,
): Serializable

//
data class Location(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("name")
    val name: String,
): Serializable

//
//data class Ou(
//    @Expose
//    @SerializedName("uid")
//    val uid: String,
//    @Expose
//    @SerializedName("code")
//    val code: String,
//    @Expose
//    @SerializedName("name")
//    val name: String
//)
//
data class Executive(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("phone")
    val phone: String,
    @Expose
    @SerializedName("email")
    val email: String,
    @Expose
    @SerializedName("first_name")
    val first_name: String,
    @Expose
    @SerializedName("last_name")
    val last_name: String,
    @Expose
    @SerializedName("middle_name")
    val middle_name: String,
    @Expose
    @SerializedName("login_unique")
    val login_unique: String,
): Serializable

//
data class Created_id(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("email")
    val email: String,
    @Expose
    @SerializedName("first_name")
    val first_name: String,
    @Expose
    @SerializedName("last_name")
    val last_name: String,
    @Expose
    @SerializedName("login_unique")
    val login_unique: String,
    @Expose
    @SerializedName("middle_name")
    val middle_name: String,
    @Expose
    @SerializedName("phone")
    val phone: String,
    @Expose
    @SerializedName("role")
    val role: Role,
): Serializable

//
//data class Role(
//    @Expose
//    @SerializedName("uid")
//    val uid: String,
//    @Expose
//    @SerializedName("code")
//    val code: String
//)
//
data class TicketDepartment(
    @Expose
    @SerializedName("uid")
    val uid: String,
//    @Expose
//    @SerializedName("allocation_type")
//    val allocation_type: Allocation_type,
    @Expose
    @SerializedName("code")
    val code: String,
    @Expose
    @SerializedName("name")
    val name: String,
): Serializable

//
//data class Allocation_type(
//    @Expose
//    @SerializedName("uid")
//    val uid: String
//)
//
//data class Region(
//    @Expose
//    @SerializedName("uid")
//    val uid: String,
//    @Expose
//    @SerializedName("code")
//    val code: String,
//    @Expose
//    @SerializedName("name")
//    val name: String
//)
//
data class Category(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("code")
    val code: String,
    @Expose
    @SerializedName("name")
    val name: String,
): Serializable
//
//data class Level(
//    @Expose
//    @SerializedName("uid")
//    val uid: String
//)

data class Cluster(
    @Expose
    @SerializedName("uid")
    val uid: String,
    @Expose
    @SerializedName("code")
    val code: String,
    @Expose
    @SerializedName("name")
    val name: String,
): Serializable

data class Priority(
    @Expose
    @SerializedName("uid")
    val uid: String,
): Serializable

data class Platform(
    @Expose
    @SerializedName("uid")
    val uid: String,
): Serializable