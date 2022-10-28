package com.apollopharmacy.vishwam.data

import com.apollopharmacy.vishwam.data.model.*
import com.apollopharmacy.vishwam.data.model.attendance.*
import com.apollopharmacy.vishwam.data.model.cms.*
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.FileResposne
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DrugRequest
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DrugResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsRequest
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.SaveAcceptAndReshootRequest
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.SaveAcceptAndReshootResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.GetpendingAndApprovedListRequest
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.GetpendingAndApprovedListResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.sampleswachui.model.LastUploadedDateResponse
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.*
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList


interface ViswamAppApi {

    @POST("https://viswam.apollopharmacy.org/mproddisc/Apollo/DiscountRequest/SaveDeviceDetailsForviswamAPP")
    suspend fun validateEmpWithOtp(
        @Header("token") token: String,
        @Body validateOtpRequest: ValidateOtpRequest,
    ): ValidateOtpResponse

    @GET("https://viswam.apollopharmacy.org/mprodutil/Apollo/VISWAM/ActivateAndDeActivateViswamRegistration?")
    suspend fun deRegisterDevice(
        @Header("token") token: String,
        @Query("EmpId") id: String,
    ): DeviceDeRegResponse

    @POST("https://viswam.apollopharmacy.org/mprodvend/APOLLO/Vendor/VALIDATEVENDOR")
    suspend fun getValidate(@Header("token") token: String, @Body data: CommonRequest): String

    @POST("https://viswam.apollopharmacy.org/mprodutil/Apollo/VISWAM/IUVVISWAMMPINDETAILS")
    suspend fun handleMPinService(
        @Header("token") token: String,
        @Body data: MPinRequest,
    ): MPinResponse

    //API's for Discount App

    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/GetpendingAndApprovedList")
    suspend fun swachhResponseList(
        @Header("token") token: String,
        @Body data: ApproveRejectListRequest,
    ): ApproveRejectListResponse

    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/GetImageUrls")
    suspend fun swachhResponseImageList(
        @Header("token") token: String,
        @Body data: LineImagesRequest,
    ): LineImagesResponse


    @POST("https://online.apollopharmacy.org/ITEMMASTER/AddArticle")
    suspend fun DrugResponse(
        @Header("token") token: String,
        @Body data: DrugRequest,
    ): DrugResponse

    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/SaveImageUrls")
    suspend fun ApproveRejectResponse(
        @Header("token") token: String,
        @Body data: ArrayList<ApproveRequest>,
    ): ApproveResponse

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
        @Body data: CMSCommonRequest,
    ): CMSCommonResponse

    @POST
    suspend fun getDepartmentList(
        @Header("token") token: String,
        @Url url: String,
        @Body data: CMSCommonRequest,
    ): CMSCommonResponse

    @POST
    suspend fun submitComplain(
        @Header("token") token: String,
        @Url url: String,
        @Body data: CMSCommonRequest,
    ): CMSCommonResponse

    @POST
    suspend fun submitComplainWithImage(
        @Header("token") token: String,
        @Url url: String,
        @Body data: CMSCommonRequest,
    ): CMSCommonResponse

    @POST
    suspend fun getListOfComplain(
        @Header("token") token: String,
        @Url url: String,
        @Body data: CMSCommonRequest,
    ): CMSCommonResponse

    @POST
    suspend fun acknowledgeTheList(
        @Header("token") token: String,
        @Url url: String,
        @Body data: CMSCommonRequest,
    ): CMSCommonResponse

    @POST
    suspend fun fetchArticleCode(
        @Header("token") token: String,
        @Url url: String,
        @Body data: CMSCommonRequest,
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
        @Body requestNewComplaintRegistration: RequestNewComplaintRegistration,
    ): ResponseNewComplaintRegistration


    @POST
    suspend fun submitEmpWithSiteIDReg(
        @Header("token") token: String,
        @Url url: String,
        @Body userSiteIDRegReqModel: UserSiteIDRegReqModel,
    ): UserSiteIDRegResModel


    // API's for Attendance App

    @GET
    suspend fun getLastLogin(
        @Header("token") token: String,
        @Url url: String,
        @Query("EMPID") id: String,
    ): LoginInfoRes

    @GET
    suspend fun getTaskList(
        @Header("token") token: String,
        @Url url: String,
        @Query("EMPID") id: String,
    ): ArrayList<GetTaskListResponse>

    @POST
    suspend fun taskInsertUpdate(
        @Header("token") token: String,
        @Url url: String,
        @Body taskInfoReq: TaskInfoReq,
    ): TaskInfoRes

    @GET
    suspend fun getAttendanceHistory(
        @Header("token") token: String,
        @Url url: String,
        @Query("EMPID") id: String,
    ): ArrayList<AttendanceHistoryRes>

    @POST
    suspend fun atdSignInOutService(
        @Header("token") token: String,
        @Url url: String,
        @Body atdLogInOutReq: AtdLogInOutReq,
    ): AtdLogInOutRes

    @GET
    suspend fun getAtdDepartmentList(
        @Header("token") token: String,
        @Url url: String,
    ): DepartmentListRes

    @GET
    suspend fun getAtdDepartmentTaskList(
        @Header("token") token: String,
        @Url url: String,
        @Query("DEPTID") id: Int,
    ): DepartmentTaskListRes

    //@GET("https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/list/ticket-list-by-site-id?")
    @GET
    suspend fun getTicketlist(
        @Url url: String,
        @Query("site_id") siteid: String,
        @Query("from_date") fromdate: String,
        @Query("to_date") todate: String,
    ): ResponseNewTicketlist


    // @GET("https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket_touch_point/list/?")
    @GET
    suspend fun getTicketHistoryApi(
        @Url url: String,
        @Query("page") page: Int,
        @Query("rows") rows: Int,
        @Query("ticket_uid") ticketuid: String,
    ): ResponseNewTicketlist.NewTicketHistoryResponse

// @GET("https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollo_cms/api/site/select/site-details?")
//    suspend fun getresolvedticketstatus(
//        @Query("site%5Bsite%5D") site: String?,
//        @Query("department%5Buid%5D") department:String?,
//        ): ResponseTicktResolvedapi

    @GET
    suspend fun getresolvedticketstatus(
        @Url url: String,
    ): ResponseTicktResolvedapi


    //cms login api....
    @POST
    suspend fun cmsLoginapi(
        @Url url: String,
        @Body cmsLogin: RequestCMSLogin,
    ): ResponseCMSLogin

    //ticket Ratin api......
    @GET
    suspend fun getTicketRating(
        @Url url: String,
    ): ResponseticketRatingApi

    //Ticket Closing Api......
    @POST
    suspend fun ticketclosingapi(
        @Url url: String,
        @Header("Content-Type") contenttype: String,
        @Header("authorization") autherization: String,
        @Body requestClosedticketApi: RequestClosedticketApi,
    ): ResponseClosedTicketApi

    @POST("https://viswam.apollopharmacy.org/LIGHTPOSPROXY/Apollo/UTIES/GETDetails")
    suspend fun getDetails(
        @Header("token") token: String,
        @Body getDetailsRequest: GetDetailsRequest,
    ): ResponseBody


    @GET("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/GetStoreWiseCategoryDetails")//?Storeid=16001
    suspend fun swachhImagesUpload(
        @Header("token") token: String, @Query("StoreId") storeId: String,
    ): SwachModelResponse



    @GET("https://online.apollopharmacy.org/QCFAILUAT/APOLLO/QCFAIL/GetPendingAndAcceptAndRejectList?")//?Storeid=16001
    suspend fun qcResponseList( @Query("EmpId") id: String,
                                @Query("FromDate") fromDate: String,
                                @Query("ToDate") tomDate: String,
                                @Query("StoreId") storeId: String,
                                @Query("REGIONID") region: String,
    ): QcListsResponse
    @GET("https://online.apollopharmacy.org/QCFAILUAT/APOLLO/QCFAIL/GetLineItems?")//qcfail
    suspend fun qcItemsResponseList( @Query("ORDERNO") id: String,): QcItemListResponse

    @POST("https://online.apollopharmacy.org/QCFAILUAT/APOLLO/QCFAIL/AcceptAndReject")
    suspend fun QcAcceptRejectResponse(
        @Body data: QcAcceptRejectRequest,
    ): QcAcceptRejectResponse

    @GET("https://online.apollopharmacy.org/QCFAILUAT/Apollo/QCFAIL/GetRemarksList")//qcfail
    suspend fun qcRejectionList(): QcReasonList

    @GET("https://online.apollopharmacy.org/QCFAILUAT/Apollo/QCFAIL/GetRegionList")//qcfail
    suspend fun qcRegionList(): QcRegionList

    @GET("https://online.apollopharmacy.org/QCFAILUAT/Apollo/QCFAIL/GetStoreList")//qcfail
    suspend fun qcStoreList(): QcStoreList

    @GET("https://online.apollopharmacy.org/QCFAILUAT/Apollo/QCFAIL/GETOMSQCFAILACTIONHISTORY?")//qcfail
    suspend fun qcStatusList(@Query("ORDERNO") id: String,): ActionResponse


//    @GET("https://viswam.apollopharmacy.org/mprodutil/Apollo/VISWAM/ActivateAndDeActivateViswamRegistration?")
//    suspend fun deRegisterDevice(
//        @Header("token") token: String,
//        @Query("EmpId") id: String,
//    ): DeviceDeRegResponse

    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/SaveImageUrls")
    suspend fun onSubmitSwacch(
        @Header("token") token: String,
        @Body data: ArrayList<OnSubmitSwachModelRequest>,
    ): OnSubmitSwachModelResponse


    @GET("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/GetStoreWiseACCessDetails")
    suspend fun checkDayWiseAccess(
        @Header("token") token: String, @Query("StoreId") storeId: String,
    ): CheckDayWiseAccessResponse

    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/SaveImageUrls")
    suspend fun onUploadSwacch(
        @Header("token") token: String,
        @Body data: OnUploadSwachModelRequest,
    ): OnUploadSwachModelResponse


    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/GetStorependingAndApprovedList")
    suspend fun getStorePersonHistory(
        @Header("token") token: String,
        @Body data: GetStorePersonHistoryodelRequest,
    ): GetStorePersonHistoryodelResponse

    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/GetImageUrls")
    suspend fun getImageUrlApi(
        @Header("token") token: String,
        @Body data: GetImageUrlModelRequest,
    ): GetImageUrlModelResponse

    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/GetpendingAndApprovedList")
    suspend fun GET_PENDING_ANDAPPROVED_LIST_API_CALL(
        @Header("token") token: String,
        @Body data: GetpendingAndApprovedListRequest?,
    ): GetpendingAndApprovedListResponse

    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/GetImageUrls")
    suspend fun GET_IMAGE_URLS_API_CALL(
        @Header("token") token: String,
        @Body data: GetImageUrlsRequest?,
    ): GetImageUrlsResponse

    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/SAVEACCEPTANDRESHOOT")
    suspend fun SAVE_ACCEPT_AND_RESHOOT(
        @Header("token") token: String,
        @Body data: SaveAcceptAndReshootRequest?,
    ): SaveAcceptAndReshootResponse


    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/SaveImageUrls")
    suspend fun onUploadSwacchRes(
        @Header("token") token: String,
        @Body data: OnUploadSwachModelRequest,
    ): OnUploadSwachModelResponse


    @POST("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/SAVEACCEPTANDRESHOOT")
    suspend fun RATING_BAR_API(
        @Header("token") token: String,
        @Body data: RatingModelRequest?,
    ): RatingModelResponse

    @GET("https://online.apollopharmacy.org/SWACHHUAT/APOLLO/SWCH/GetLastUploadedDate")
    suspend fun GET_LAST_UPLOADED_DATE(
        @Header("token") token: String,
        @Query("STOREID") storeId: String, @Query("USERID") userId: String,
    ): LastUploadedDateResponse

    @Multipart
    @POST("https://cmsuat.apollopharmacy.org/zc-v3.1-fs-svc/2.0/apollo_cms/upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
    ): FileResposne



    @POST("https://viswam.apollopharmacy.org/LIGHTPOSPROXY/Apollo/UTIES/GETDetails")
    suspend fun getUploadProxImage(
        @Header("token") token: String,
        @Body getDetailsRequest: GetDetailsRequest,
    ): ResponseBody
}


//