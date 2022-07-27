package com.apollopharmacy.vishwam.data

import com.apollopharmacy.vishwam.data.model.*
import com.apollopharmacy.vishwam.data.model.attendance.*
import com.apollopharmacy.vishwam.data.model.cms.*
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model.*
import okhttp3.ResponseBody
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

interface ViswamAppApi {

    @POST("https://viswam.apollopharmacy.org/mproddisc/Apollo/DiscountRequest/SaveDeviceDetailsForviswamAPP")
    suspend fun validateEmpWithOtp(
        @Header("token") token: String,
        @Body validateOtpRequest: ValidateOtpRequest
    ): ValidateOtpResponse

    @GET("https://viswam.apollopharmacy.org/mprodutil/Apollo/VISWAM/ActivateAndDeActivateViswamRegistration?")
    suspend fun deRegisterDevice(
        @Header("token") token: String,
        @Query("EmpId") id: String
    ): DeviceDeRegResponse

    @POST("https://viswam.apollopharmacy.org/mprodvend/APOLLO/Vendor/VALIDATEVENDOR")
    suspend fun getValidate(@Header("token") token: String, @Body data: CommonRequest): String

    @POST("https://viswam.apollopharmacy.org/mprodutil/Apollo/VISWAM/IUVVISWAMMPINDETAILS")
    suspend fun handleMPinService(
        @Header("token") token: String,
        @Body data: MPinRequest
    ): MPinResponse

    //API's for Discount App

    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/GetpendingAndApprovedList")
    suspend fun swachhResponseList(
        @Header("token") token: String,
        @Body data: ApproveRejectListRequest
    ): ApproveRejectListResponse
    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/GetImageUrls")
    suspend fun swachhResponseImageList(
        @Header("token") token: String,
        @Body data: LineImagesRequest
    ): LineImagesResponse


    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/SaveImageUrls")
    suspend fun ApproveRejectResponse(
        @Header("token") token: String,
        @Body data: ArrayList<ApproveRequest>): ApproveResponse
    @POST
    suspend fun loginUser(@Url url: String, @Body data: CommonRequest): String

    @POST
    suspend fun getPendingList(@Url url: String, @Body data: CommonRequest): String

    @POST
    suspend fun getApprovalAndRejectedList(@Url url: String, @Body data: CommonRequest): String

    @POST
    suspend fun getRejectedList(@Url url: String, @Body data: CommonRequest): String

    @POST
    suspend fun acceptOrReject(@Url url: String, @Body data: CommonRequest): String

    @POST
    suspend fun billOrder(@Url url: String, @Body data: CommonRequest): String

    @POST
    suspend fun bulkAcceptOrReject(@Url url: String, @Body data: CommonRequest): String

    @POST
    suspend fun getFilteredList(@Url url: String, @Body data: CommonRequest): String

    // API's for CMS Application

    @POST
    suspend fun getTrackingList(
        @Header("token") token: String,
        @Url url: String,
        @Body data: CMSCommonRequest
    ): CMSCommonResponse

    @POST
    suspend fun getDepartmentList(
        @Header("token") token: String,
        @Url url: String,
        @Body data: CMSCommonRequest
    ): CMSCommonResponse

    @POST
    suspend fun submitComplain(
        @Header("token") token: String,
        @Url url: String,
        @Body data: CMSCommonRequest
    ): CMSCommonResponse

    @POST
    suspend fun submitComplainWithImage(
        @Header("token") token: String,
        @Url url: String,
        @Body data: CMSCommonRequest
    ): CMSCommonResponse

    @POST
    suspend fun getListOfComplain(
        @Header("token") token: String,
        @Url url: String,
        @Body data: CMSCommonRequest
    ): CMSCommonResponse

    @POST
    suspend fun acknowledgeTheList(
        @Header("token") token: String,
        @Url url: String,
        @Body data: CMSCommonRequest
    ): CMSCommonResponse

    @POST
    suspend fun fetchArticleCode(
        @Header("token") token: String,
        @Url url: String,
        @Body data: CMSCommonRequest
    ): CMSCommonResponse

    @GET
    suspend fun getSiteId(@Header("token") token: String, @Url url: String): SiteDto


    @GET
    suspend fun getReasonsmaster(@Url url: String): ReasonmasterV2Response

    //new complaint Registartion api.......
    @POST
    suspend fun submitNewComplaintreg(
        @Url url: String,
        @Header("content-type") autherisation: String,
        @Body requestNewComplaintRegistration: RequestNewComplaintRegistration
    ): ResponseNewComplaintRegistration


    @POST
    suspend fun submitEmpWithSiteIDReg(
        @Header("token") token: String,
        @Url url: String,
        @Body userSiteIDRegReqModel: UserSiteIDRegReqModel
    ): UserSiteIDRegResModel


    // API's for Attendance App

    @GET
    suspend fun getLastLogin(
        @Header("token") token: String,
        @Url url: String,
        @Query("EMPID") id: String
    ): LoginInfoRes

    @GET
    suspend fun getTaskList(
        @Header("token") token: String,
        @Url url: String,
        @Query("EMPID") id: String
    ): ArrayList<GetTaskListResponse>

    @POST
    suspend fun taskInsertUpdate(
        @Header("token") token: String,
        @Url url: String,
        @Body taskInfoReq: TaskInfoReq
    ): TaskInfoRes

    @GET
    suspend fun getAttendanceHistory(
        @Header("token") token: String,
        @Url url: String,
        @Query("EMPID") id: String
    ): ArrayList<AttendanceHistoryRes>

    @POST
    suspend fun atdSignInOutService(
        @Header("token") token: String,
        @Url url: String,
        @Body atdLogInOutReq: AtdLogInOutReq
    ): AtdLogInOutRes

    @GET
    suspend fun getAtdDepartmentList(
        @Header("token") token: String,
        @Url url: String
    ): DepartmentListRes

    @GET
    suspend fun getAtdDepartmentTaskList(
        @Header("token") token: String,
        @Url url: String,
        @Query("DEPTID") id: Int
    ): DepartmentTaskListRes

    //@GET("https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/list/ticket-list-by-site-id?")
    @GET
    suspend fun getTicketlist(
        @Url url: String,
        @Query("site_id") siteid: String,
        @Query("from_date") fromdate: String,
        @Query("to_date") todate: String
    ): ResponseNewTicketlist


    // @GET("https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket_touch_point/list/?")
    @GET
    suspend fun getTicketHistoryApi(
        @Url url: String,
        @Query("page") page: Int,
        @Query("rows") rows: Int,
        @Query("ticket_uid") ticketuid: String
    ): ResponseNewTicketlist.NewTicketHistoryResponse

    /* @GET("https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollo_cms/api/site/select/site-details?")
    suspend fun getresolvedticketstatus(
        @Query("site%5Bsite%5D") site: String?,
        @Query("department%5Buid%5D") department:String?,
        ): ResponseTicktResolvedapi*/

    @GET
    suspend fun getresolvedticketstatus(
        @Url url: String
    ): ResponseTicktResolvedapi


    //cms login api....
    @POST
    suspend fun cmsLoginapi(
        @Url url: String,
        @Body cmsLogin: RequestCMSLogin
    ): ResponseCMSLogin

    //ticket Ratin api......
    @GET
    suspend fun getTicketRating(
        @Url url: String
    ): ResponseticketRatingApi

    //Ticket Closing Api......
    @POST
    suspend fun ticketclosingapi(
        @Url url: String,
        @Header("Content-Type") contenttype: String,
        @Header("authorization") autherization: String,
        @Body requestClosedticketApi: RequestClosedticketApi
    ): ResponseClosedTicketApi

    @POST("https://viswam.apollopharmacy.org/LIGHTPOSPROXY/Apollo/UTIES/GETDetails")
    suspend fun getDetails(
        @Header("token") token: String,
        @Body getDetailsRequest: GetDetailsRequest
    ): ResponseBody


    @GET("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/GetStoreWiseCategoryDetails?Storeid=16001")
    suspend fun swachhImagesUpload(
        @Header("token") token: String,
    ): SwachModelResponse
}