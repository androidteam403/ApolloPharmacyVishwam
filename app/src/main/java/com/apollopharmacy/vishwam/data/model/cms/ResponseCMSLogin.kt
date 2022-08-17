package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseCMSLogin (
    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("success")
    val success:Boolean,

    @field:SerializedName("zcServerDateTime")
    val zcServerDateTime:String? = null,

    @field:SerializedName("zcServerIp")
    val zcServerIp:String? = null,

    @field:SerializedName("zcServerHost")
    val zcServerHost:String? = null,
    ):Serializable{

      data class Data(

          @field:SerializedName("user_code")
          val user_code:String? = null,

          @field:SerializedName("phone")
          val phone:String? = null,

          @field:SerializedName("login_unique")
          val login_unique:String? = null,

          @field:SerializedName("email")
          val email:String? = null,

          @field:SerializedName("first_name")
          val first_name:String? = null,

          @field:SerializedName("middle_name")
          val middle_name:String? = null,

          @field:SerializedName("last_name")
          val last_name:String? = null,

          @field:SerializedName("role")
          val role:Role,

          @field:SerializedName("available_status")
          val available_status:String? = null,

          @field:SerializedName("pic")
          val pic:ArrayList<Pic>,

          @field:SerializedName("name")
          val name:String? = null,

          @field:SerializedName("token")
          val token:String? = null,

          @field:SerializedName("addInfo")
          val addInfo:AddInfo,

          @field:SerializedName("expiry")
          val expiry:Int? = null,


          @field:SerializedName("userGroups")
          val userGroups:ArrayList<String>,

          @field:SerializedName("multiple-apps")
          val multipleapps:Boolean,

          @field:SerializedName("redirectUrl")
          val redirectUrl:String? = null,

          @field:SerializedName("shortToken")
          val shortToken:String? = null,

          @field:SerializedName("uid")
          val uid:String? = null,

          ):Serializable

    data class Role(

        @field:SerializedName("mobileAccess")
        val mobileAccess:String? = null,

        @field:SerializedName("code")
        val code:String? = null,

        @field:SerializedName("name")
        val name:String? = null,


    ):Serializable


    data class  Pic(
        @field:SerializedName("size")
        val size: Int? = null,

        @field:SerializedName("saved")
        val saved: Boolean,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("contentType")
        val contentType: String? = null,

        @field:SerializedName("dimenesions")
        val dimenesions: Dimenesions,

        @field:SerializedName("path")
        val path: String? = null,

        @field:SerializedName("fullPath")
        val fullPath: String? = null,

        @field:SerializedName("created_info")
        val created_info:CreatedInfo,
    ):Serializable


    data class  Dimenesions(

        @field:SerializedName("200_200")
        val image_200_200: String? = null,

        @field:SerializedName("200_200_fullPath")
        val fullPath_200_200: String? = null,

        @field:SerializedName("100_100")
        val image100_100: String? = null,

        @field:SerializedName("100_100_fullPath")
        val fullPath_100_100: String? = null,
    ):Serializable

    data class  CreatedInfo(
        @field:SerializedName("created_on")
        val created_on: String? = null,

        @field:SerializedName("user_id")
        val user_id: String? = null,

        @field:SerializedName("app_user_id")
        val app_user_id: String? = null,

        @field:SerializedName("user_code")
        val user_code: String? = null,

        @field:SerializedName("user_name")
        val user_name: String? = null,

        @field:SerializedName("login_unique")
        val login_unique: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("phone")
        val phone: String? = null,

        @field:SerializedName("role_code")
        val role_code: String? = null,

        @field:SerializedName("role_name")
        val role_name: String? = null,

        ):Serializable


    data class  AddInfo(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("salutation")
        val salutation:Salutation,

        @field:SerializedName("status")
        val status:Status,

        @field:SerializedName("level")
        val level:Level,

        @field:SerializedName("department")
        val department:Department,

        @field:SerializedName("country")
        val country:Country,

        @field:SerializedName("site")
        val site:Site,

        @field:SerializedName("reports_to")
        val reports_to:ReportsTo,

        @field:SerializedName("role")
        val role:Role__1,

        @field:SerializedName("user_reports_to")
        val user_reports_to:UserReportsTo,

        @field:SerializedName("user_team")
        val user_team:UserTeam,

        @field:SerializedName("site__executive")
        val site__executive:ArrayList<String>,


        @field:SerializedName("site_hod")
        val site_hod:ArrayList<String>,

        @field:SerializedName("site_manager")
        val site_manager:ArrayList<String>,

        @field:SerializedName("site_region_head")
        val site_region_head:ArrayList<String>,

        @field:SerializedName("site_supervisor")
        val site_supervisor:ArrayList<String>,

        @field:SerializedName("site_mngr_exec")
        val site_mngr_exec:ArrayList<String>,

        @field:SerializedName("app_user")
        val app_user:AppUser,
        ):Serializable

    data class  Salutation(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

    ):Serializable

    data class Status(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

    ):Serializable

    data class Level(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("name")
        val name: String? = null,
    ):Serializable

    data class Department(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("name")
        val name: String? = null,
    ):Serializable

    data class  Country(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("isd_code")
        val isd_code: String? = null,

        @field:SerializedName("name")
        val name: String? = null,
    ):Serializable

    data class  Site(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("site")
        val site: String? = null,

        @field:SerializedName("site_type")
        val site_type:SiteType ,

        @field:SerializedName("store_name")
        val store_name: String? = null,

    ):Serializable

    data class SiteType(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("other")
        val other: Other,

        @field:SerializedName("icon")
        val icon: String? = null,

    ):Serializable

    data class  Other(
        @field:SerializedName("color")
        val color: String? = null,
    ):Serializable

    data class ReportsTo(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("first_name")
        val first_name: String? = null,

    ):Serializable

    data class  Role__1(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("department_roles")
        val department_roles: DepartmentRoles,

        @field:SerializedName("knowledge_mngmt_access_role")
        val knowledge_mngmt_access_role: KnowledgeMngmtAccessRole,
        ):Serializable


    data class  DepartmentRoles(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("department")
        val department:Department__1,

        @field:SerializedName("roles")
        val roles:Roles,
        ):Serializable

    data class Department__1(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,
    ):Serializable

    data class Roles(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,
    ):Serializable

    data class KnowledgeMngmtAccessRole(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("knowledge_mngmt_access")
        val knowledge_mngmt_access: KnowledgeMngmtAccess,
    ):Serializable

    data class  KnowledgeMngmtAccess(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("department")
        val department:Department__2,
        ):Serializable

    data class  Department__2(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,
    ):Serializable

    data class  UserReportsTo(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("phone")
        val phone: String? = null,
    ):Serializable

    data class  UserTeam(

        @field:SerializedName("uid")
        val uid: String? = null,

//        @field:SerializedName("user_team_region")
//        val user_team_region: ArrayList<String>,

//        @field:SerializedName("user_team_category")
//        val user_team_category: ArrayList<String>,

    ):Serializable

    data class  AppUser(
        @field:SerializedName("uid")
        val uid: String? = null,
    ):Serializable

}