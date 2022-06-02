package com.apollopharmacy.vishwam.data.model.cms

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
             val ticket_sub_category:TicketSubCategory

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
             val name:String?=null

         ):Serializable


         data class  TicketSubCategory(
             @field:SerializedName("uid")
             val uid:String?=null,

             @field:SerializedName("name")
             val name:String?=null
         ):Serializable

     }