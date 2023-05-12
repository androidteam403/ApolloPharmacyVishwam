package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReasonmasterV2Response(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("zcServerIp")
    val zcServerIP: String? = null,

    @field:SerializedName("zcServerHost")
    val zcServerHost: String? = null,

    @field:SerializedName("data")
    val data : Data) :Serializable
     {
    data class Data(
        @field:SerializedName("listData")
        val listdata:ListData):Serializable

         data class  ListData(
             @field:SerializedName("records")
             val records:String?=null,

             @field:SerializedName("select")
             val select:Boolean?=null,

             @field:SerializedName("page")
             val page:Int?=null,

             @field:SerializedName("pivotData")
             val pivotData:Object ?=null,

             @field:SerializedName("aggregation")
             val aggregation:Object ?=null,

             @field:SerializedName("size")
             val size:Int ?=null,

             @field:SerializedName("rows")
             val rows:List<Row>

         ):Serializable

         data class Row(
             @field:SerializedName("uid")
             val uid:String?=null,

             @field:SerializedName("code")
             val code:String?=null,

             @field:SerializedName("name")
             val name:String?=null,

             @field:SerializedName("status")
             val status:Status,

             @field:SerializedName("department")
             val department:Department,

             @field:SerializedName("ticket_category")
             val ticket_category:TicketCategory,

             @field:SerializedName("ticket_sub_category")
             val ticket_sub_category:TicketSubCategory,

             @field:SerializedName("reason_sla")
             val reason_sla: ArrayList<Reason_SLA>

         ):Serializable

         data class  Status(
             @field:SerializedName("uid")
             val uid:String?=null,

             @field:SerializedName("name")
             val name:String?=null,

             @field:SerializedName("icon")
             val icon:Object?=null,

             @field:SerializedName("other")
             val other:Other):Serializable

         data class Other(
             @field:SerializedName("color")
             val color:Object?=null
         ):Serializable

         data class  Department(
             @field:SerializedName("uid")
             val uid:String?=null,

             @field:SerializedName("code")
             val code:String?=null,

             @field:SerializedName("name")
             var name:String?=null

         ):Serializable


         data class  TicketCategory(

             @field:SerializedName("uid")
             val uid:String?=null,

             @field:SerializedName("name")
             val name:String?=null,

             @field:SerializedName("code")
             val code:String?=null

         ):Serializable


         data class  TicketSubCategory(
             @field:SerializedName("uid")
             val uid:String?=null,

             @field:SerializedName("name")
             val name:String?=null,

             @field:SerializedName("code")
             val code:String?=null
         ):Serializable

         data class Reason_SLA(
             @Expose
             @SerializedName("uid")
             val uid: String,
             @Expose
             @SerializedName("bbh_tat")
             val bbh_tat: Int,
             @Expose
             @SerializedName("bh_tat")
             val bh_tat: Int,
             @Expose
             @SerializedName("esacltn_needed")
             val esacltn_needed: Esacltn_needed,
             @Expose
             @SerializedName("priority")
             val priority: Priority,
             @Expose
             @SerializedName("sla_status")
             val sla_status: Sla_status,
             @Expose
             @SerializedName("bbh_tat_mins")
             val bbh_tat_mins: Bbh_tat_mins,
             @Expose
             @SerializedName("bh_tat_mins")
             val bh_tat_mins: Bh_tat_mins,
             @Expose
             @SerializedName("default_tat_hrs")
             val default_tat_hrs: Int,
             @Expose
             @SerializedName("default_tat_mins")
             val default_tat_mins: Default_tat_mins,
             @Expose
             @SerializedName("bh_end_time")
             var bh_end_time: String?,
             @Expose
             @SerializedName("bh_start_time")
             var bh_start_time: String?,

         ):Serializable

         data class Default_tat_mins(
             @Expose
             @SerializedName("uid")
             val uid: String,
             @Expose
             @SerializedName("name")
             val name: String,
             @Expose
             @SerializedName("other")
             val other: Other
         ):Serializable

         data class Bh_tat_mins(
             @Expose
             @SerializedName("uid")
             val uid: String,
             @Expose
             @SerializedName("name")
             val name: String,
             @Expose
             @SerializedName("other")
             val other: Other
         ):Serializable

         data class Bbh_tat_mins(
             @Expose
             @SerializedName("uid")
             val uid: String,
             @Expose
             @SerializedName("name")
             val name: String,
             @Expose
             @SerializedName("other")
             val other: Other
         ):Serializable

         data class Sla_status(
             @Expose
             @SerializedName("uid")
             val uid: String,
             @Expose
             @SerializedName("name")
             val name: String,
             @Expose
             @SerializedName("other")
             val other: Other
         ):Serializable

         data class Priority(
             @Expose
             @SerializedName("uid")
             val uid: String,
             @Expose
             @SerializedName("name")
             val name: String,
             @Expose
             @SerializedName("other")
             val other: Other?
         ):Serializable


         data class Esacltn_needed(
             @Expose
             @SerializedName("uid")
             val uid: String,
             @Expose
             @SerializedName("name")
             val name: String,
             @Expose
             @SerializedName("other")
             val other: Other?
         ):Serializable
     }