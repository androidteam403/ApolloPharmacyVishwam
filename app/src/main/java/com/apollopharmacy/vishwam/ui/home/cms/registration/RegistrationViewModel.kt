package com.apollopharmacy.vishwam.ui.home.cms.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Config.CONTAINER_NAME
import com.apollopharmacy.vishwam.data.Config.STORAGE_CONNECTION_FOR_CCR_APP
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.data.azure.ConnectionAzure
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.cms.*
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.data.network.SwachApiiRepo
import com.apollopharmacy.vishwam.dialog.model.TransactionPOSModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.FetchItemModel
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.UpdateUserDefaultSiteRequest
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.UpdateUserDefaultSiteResponse
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

class RegistrationViewModel : ViewModel() {
    var departmentLiveData = ArrayList<DepartmentV2Response.DepartmentListItem>()
    var categoryLiveData = ArrayList<CategoryListResponse.CategoryListItem>()
    var command = LiveEvent<CmsCommand>()
    val state = MutableLiveData<State>()
    var visibleState = LiveEvent<State>()
    var siteLiveData = ArrayList<StoreListItem>()
    var pendingListLiveData = MutableLiveData<PendingListToAcknowledge>()
     var storeDetailsSend = StoreListItem()
    val TAG = "RegistrationModel"

    var deartmentlist = ArrayList<ReasonmasterV2Response.Department>()
    var uniquedeartmentlist = ArrayList<ReasonmasterV2Response.Department>()

    var Categorylistfromreasons = ArrayList<ReasonmasterV2Response.TicketCategory>()
    var uniqueCategoryList = ArrayList<ReasonmasterV2Response.TicketCategory>()


    var SubCategorylistfromreasons = ArrayList<ReasonmasterV2Response.TicketSubCategory>()
    var uniqueSubCategoryList = ArrayList<ReasonmasterV2Response.TicketSubCategory>()

    var reasonsList = ArrayList<ReasonmasterV2Response.Row>()

    var inventoryCategotyItem = FetchItemModel.Rows()

     var transactionPOSDetails= MutableLiveData<TransactionPOSModel>()

    lateinit var Reasonlistdata: ReasonmasterV2Response

    var tisketstatusresponse = MutableLiveData<ResponseTicktResolvedapi>()

    var cmsloginapiresponse = MutableLiveData<ResponseCMSLogin>()

    var cmsticketRatingresponse = MutableLiveData<ResponseticketRatingApi>()

    var cmsticketclosingapiresponse = MutableLiveData<ResponseClosedTicketApi>()

    var reasonlistapiresponse = MutableLiveData<ReasonmasterV2Response>()

    var responsenewcomplaintregistration = MutableLiveData<ResponseNewComplaintRegistration>()

    var updateUserDefaultSiteResponseMutable = MutableLiveData<UpdateUserDefaultSiteResponse>()

    fun siteId() {
        if (Preferences.isSiteIdListFetched()) {
            siteLiveData.clear()
            val gson = Gson()
            val siteIdList = Preferences.getSiteIdListJson()
            val type = object : TypeToken<List<StoreListItem?>?>() {}.type

            this.siteLiveData =
                gson.fromJson<List<StoreListItem>>(siteIdList, type) as ArrayList<StoreListItem>
            command.value = CmsCommand.ShowSiteInfo("")
        } else {
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("CMS GETSITELIST")) {
                    val baseUrl = data.APIS[i].URL
                    val token = data.APIS[i].TOKEN
                    viewModelScope.launch {
                        state.value = State.SUCCESS
                        val response = withContext(Dispatchers.IO) {
                            RegistrationRepo.getDetails(
                                "h72genrSSNFivOi/cfiX3A==",
                                GetDetailsRequest(
                                    baseUrl,
                                    "GET",
                                    "The",
                                    "",
                                    ""
                                )
                            )
//                        RegistrationRepo.selectSiteId(token, baseUrl)
                        }
                        when (response) {
                            is ApiResult.Success -> {
                                state.value = State.ERROR
                                val resp: String = response.value.string()
                                val res = BackShlash.removeBackSlashes(resp)
                                val reasonmasterV2Response =
                                    Gson().fromJson(
                                        BackShlash.removeSubString(res),
                                        SiteDto::class.java
                                    )

                                if (reasonmasterV2Response.status) {
                                    siteLiveData.clear()
                                    reasonmasterV2Response.siteData?.listData?.rows?.map {
                                        siteLiveData.add(it)
                                    }
                                    // getDepartment()
                                    command.value = CmsCommand.ShowSiteInfo("")
                                } else {
                                    command.value = CmsCommand.ShowToast(
                                        reasonmasterV2Response.message.toString()
                                    )
                                }
                            }
                            is ApiResult.GenericError -> {
                                state.value = State.ERROR
                            }
                            is ApiResult.NetworkError -> {
                                state.value = State.ERROR
                            }
                            is ApiResult.UnknownError -> {
                                state.value = State.ERROR
                            }
                            is ApiResult.UnknownHostException -> {
                                state.value = State.ERROR
                            }
                        }
                    }
                }
            }
        }
    }


    //get Remarsks list api......................................
    fun getRemarksMasterList() {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS REASONLIST")) {
                var baseUrl = data.APIS[i].URL + "page=1&rows=1000"
                viewModelScope.launch {
                    state.value = State.SUCCESS
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getDetails(
                            "h72genrSSNFivOi/cfiX3A==",
                            GetDetailsRequest(
                                baseUrl,
                                "GET",
                                "The",
                                "",
                                ""
                            )
                        )
                        //  RegistrationRepo.getReasonslistmaster(baseUrl)
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            state.value = State.ERROR
                            if (response != null) {
                                val resp: String = response.value.string()
                                if (resp != null) {
                                    val res = BackShlash.removeBackSlashes(resp)
                                    val reasonmasterV2Response =
                                        Gson().fromJson(
                                            BackShlash.removeSubString(res),
                                            ReasonmasterV2Response::class.java
                                        )

                                    if (reasonmasterV2Response.success) {
                                        Reasonlistdata = reasonmasterV2Response
                                        reasonlistapiresponse.value = reasonmasterV2Response
                                        val reasonlitrows =
                                            reasonmasterV2Response.data.listdata.rows
                                        for (row in reasonlitrows) {
                                            deartmentlist.add(row.department)
                                        }
                                    } else {
                                        command.value = CmsCommand.ShowToast(
                                            reasonmasterV2Response.message.toString()
                                        )
                                    }
                                }
                            } else {
//                                command.value = CmsCommand.ShowToast(
//                                    response.value.message.toString()
//                                )
                            }
                        }
                        is ApiResult.GenericError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }


    //Ticket viewlook up api....................................
    fun getTicketstatus(site: String?, department: String?) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS OPENTICKETLIST")) {
                /* var baseUrl =
                     "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/site/select/site-details?"*/
                //val token = data.APIS[i].TOKEN
                var baseUrl = data.APIS[i].URL
                val querystr = "site%5Bsite%5D=" + site + "&department%5Buid%5D=" + department
                //val encodestr=URLEncoder.encode(querystr,"UTF-8")
                baseUrl = baseUrl + querystr
                viewModelScope.launch {
                    state.value = State.SUCCESS
                    // RegistrationRepo.getticketresolvedstatus(site,department)
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getDetails(
                            "h72genrSSNFivOi/cfiX3A==",
                            GetDetailsRequest(
                                baseUrl,
                                "GET",
                                "The",
                                "",
                                ""
                            )
                        )
                        // RegistrationRepo.getticketresolvedstatus(baseUrl)
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            state.value = State.ERROR
                            // if (!response.value.success) {
                            if (response != null) {
                                val resp: String = response.value.string()
                                if (resp != null) {
                                    val res = BackShlash.removeBackSlashes(resp)
                                    val responseTicktResolvedapi =
                                        Gson().fromJson(
                                            BackShlash.removeSubString(res),
                                            ResponseTicktResolvedapi::class.java
                                        )
                                    tisketstatusresponse.value = responseTicktResolvedapi
//                                    if (!responseTicktResolvedapi.success) {
//                                        tisketstatusresponse.value = responseTicktResolvedapi
//                                    } else {
//                                        command.value = CmsCommand.ShowToast(
//                                            responseTicktResolvedapi.toString()
//                                        )
//                                    }
                                }
                            }
                            /* val reasonlitrows = response.value.data.listdata.rows
                             for (row in reasonlitrows) {
                                 deartmentlist.add(row.department)
                             }*/
                            /* } else {
                                 command.value = CmsCommand.ShowToast(
                                     response.value.message.toString())
                             }*/
                        }
                        is ApiResult.GenericError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }

    //User Log In api.......
    fun getCMSLoginApi(cmsLogin: RequestCMSLogin) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS LOGIN")) {
                /* var baseUrl =
                     "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/login"*/
                var baseUrl = data.APIS[i].URL
                //val token = data.APIS[i].TOKEN
                //   val querystr="site%5Bsite%5D="+site+"&department%5Buid%5D="+department
                //val encodestr=URLEncoder.encode(querystr,"UTF-8")
                //  baseUrl=baseUrl+querystr

                val requestCMSLoginJson = Gson().toJson(cmsLogin)
                viewModelScope.launch {
                    state.value = State.SUCCESS
                    // RegistrationRepo.getticketresolvedstatus(site,department)
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getDetails(
                            "h72genrSSNFivOi/cfiX3A==",
                            GetDetailsRequest(
                                baseUrl,
                                "POST",
                                requestCMSLoginJson,
                                "",
                                ""
                            )
                        )
                        //  RegistrationRepo.getCMSLoginApi(baseUrl, cmsLogin)
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            state.value = State.ERROR
                            if (response != null) {
                                val resp: String = response.value.string()
                                if (resp != null) {
                                    val res = BackShlash.removeBackSlashes(resp)
                                    val responseCMSLogin =
                                        Gson().fromJson(
                                            BackShlash.removeSubString(res),
                                            ResponseCMSLogin::class.java
                                        )
                                    cmsloginapiresponse.value = responseCMSLogin

                                }


                            }
                        }
                        is ApiResult.GenericError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }

    //ticket rating Api.............
    fun getTicketRatingApi() {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS FEEDBACKRATING")) {
                /* var baseUrl =
                     "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/choose-data/ticket_rating"*/
                var baseUrl = data.APIS[i].URL

                //val token = data.APIS[i].TOKEN
                //   val querystr="site%5Bsite%5D="+site+"&department%5Buid%5D="+department
                //val encodestr=URLEncoder.encode(querystr,"UTF-8")
                //  baseUrl=baseUrl+querystr
                viewModelScope.launch {
                    state.value = State.SUCCESS
                    // RegistrationRepo.getticketresolvedstatus(site,department)
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getDetails(
                            "h72genrSSNFivOi/cfiX3A==",
                            GetDetailsRequest(
                                baseUrl,
                                "GET",
                                "the",
                                "",
                                ""
                            )
                        )

//                        RegistrationRepo.getTicketRating(baseUrl)
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            state.value = State.ERROR
                            if (response != null) {
                                // tisketstatusresponse.value = response.value
                                val resp: String = response.value.string()
                                if (resp != null) {
                                    val res = BackShlash.removeBackSlashes(resp)
                                    val responseticketRatingApi =
                                        Gson().fromJson(
                                            BackShlash.removeSubString(res),
                                            ResponseticketRatingApi::class.java
                                        )
                                    cmsticketRatingresponse.value = responseticketRatingApi


                                }


                            }
                        }
                        is ApiResult.GenericError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }


    //ticket closing api.....................
    fun getTicketclosingApi(token: String?, requestClosedticketApi: RequestClosedticketApi) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS CLOSEORREOPENTICKET")) {
                var baseUrl = data.APIS[i].URL
                /* var baseUrl =
                     "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/ticket-actions-workflow-update"*/
                //val token = data.APIS[i].TOKEN
                //   val querystr="site%5Bsite%5D="+site+"&department%5Buid%5D="+department
                //val encodestr=URLEncoder.encode(querystr,"UTF-8")
                //  baseUrl=baseUrl+querystr

                val requestClosedticketApiJson = Gson().toJson(requestClosedticketApi)


                var autherisation = "Bearer " + token
                viewModelScope.launch {
                    state.value = State.SUCCESS
                    // RegistrationRepo.getticketresolvedstatus(site,department)
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getDetails(
                            "h72genrSSNFivOi/cfiX3A==",
                            GetDetailsRequest(
                                baseUrl,
                                "POST",
                                requestClosedticketApiJson,
                                "authorization",
                                autherisation
                            )
                        )


//                        RegistrationRepo.getTicketClosingapi(
//                            baseUrl,
//                            "application/json",
//                            autherisation,
//                            requestClosedticketApi
//                        )
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            state.value = State.ERROR
                            if (response != null) {
                                val resp: String = response.value.string()
                                if (resp != null) {
                                    val res = BackShlash.removeBackSlashes(resp)
                                    val responseClosedTicketApi =
                                        Gson().fromJson(
                                            BackShlash.removeSubString(res),
                                            ResponseClosedTicketApi::class.java
                                        )
                                    // tisketstatusresponse.value = response.value
                                    cmsticketclosingapiresponse.value = responseClosedTicketApi


                                }
                            }
                        }
                        is ApiResult.GenericError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }

    fun getDepartment() {
        clearAllList()
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS APP")) {
                val baseUrl = data.APIS[i].URL
                val token = data.APIS[i].TOKEN
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.callDepartmentList(
                            token,
                            baseUrl,
                            Config.CMS_List_Of_Departments
                        )
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            command.value = CmsCommand.SuccessDeptList("")
                            response.value.departmentList.map { departmentLiveData.add(it) }
                        }
                        is ApiResult.GenericError -> {
                        }
                        is ApiResult.NetworkError -> {
                        }
                        is ApiResult.UnknownError -> {
                        }
                        is ApiResult.UnknownHostException -> {
                        }
                    }
                }
            }
        }
    }

    fun getdepartmrntsformreasonslist(): ArrayList<ReasonmasterV2Response.Department> {
        clearAllList();
        uniquedeartmentlist.clear()
        var tempuniquedeartmentlist = ArrayList<ReasonmasterV2Response.Department>()
        var checkuplicate: Boolean
        if (deartmentlist.size > 1) {
            tempuniquedeartmentlist.clear()
            for (item in deartmentlist) {
                checkuplicate = true;
                if (tempuniquedeartmentlist.size > 0) {
                    for (item1 in tempuniquedeartmentlist) {
                        if (item1.uid.equals(item.uid)) {
                            checkuplicate = false;
                            break;
                        }

                    }
                    if (checkuplicate) {
                        tempuniquedeartmentlist.add(item)
                    }
                } else {
                    tempuniquedeartmentlist.add(item)
                }
                uniquedeartmentlist = tempuniquedeartmentlist
            }
            // uniquedeartmentlist =
            //  deartmentlist.distinct() as ArrayList<ReasonmasterV2Response.Department>;*/
        } else {
            uniquedeartmentlist = deartmentlist;
        }
        return uniquedeartmentlist;
    }

    fun getCategoriesfromReasons(departmentname: String): ArrayList<ReasonmasterV2Response.TicketCategory> {
        Categorylistfromreasons.clear()
        uniqueCategoryList.clear()
        var tempuniqueCategoryList = ArrayList<ReasonmasterV2Response.TicketCategory>()
        // clearAllList();
        val reasonlist = Reasonlistdata.data.listdata.rows
        for (rowdata in reasonlist) {
            if (rowdata.department.uid.equals(departmentname)) {
                Categorylistfromreasons.add(rowdata.ticket_category)
            }

        }
        var checkuplicate: Boolean
        if (Categorylistfromreasons.size > 1) {
            tempuniqueCategoryList.clear()
            for (item in Categorylistfromreasons) {
                checkuplicate = true;
                if (tempuniqueCategoryList.size > 0) {
                    for (item1 in tempuniqueCategoryList) {
                        if (item1.uid.equals(item.uid)) {
                            checkuplicate = false;
                            break;
                        }

                    }
                    if (checkuplicate) {
                        tempuniqueCategoryList.add(item)
                    }
                } else {
                    tempuniqueCategoryList.add(item)
                }
                uniqueCategoryList = tempuniqueCategoryList

            }
            /* uniqueCategoryList =
                    Categorylistfromreasons.distinct() as ArrayList<ReasonmasterV2Response.TicketCategory>;*/
        } else {
            uniqueCategoryList = Categorylistfromreasons;
        }
        uniqueCategoryList.sortBy { it.name }
        return uniqueCategoryList;
    }

    fun getSubCategoriesfromReasons(departmentname: String): ArrayList<ReasonmasterV2Response.TicketSubCategory> {
        // clearAllList();
        SubCategorylistfromreasons.clear()
        uniqueSubCategoryList.clear()
        // var filtersubcategoryarray = ArrayList<String?>()
        var tempuniqueSubCategoryList = ArrayList<ReasonmasterV2Response.TicketSubCategory>()
        val reasonlist = Reasonlistdata.data.listdata.rows
        for (rowdata in reasonlist) {
            if (rowdata.ticket_category.uid.equals(departmentname)) {
                SubCategorylistfromreasons.add(rowdata.ticket_sub_category)
            }
        }
        var checkuplicate: Boolean
        tempuniqueSubCategoryList.clear()
        if (SubCategorylistfromreasons.size > 1) {
            //  uniqueSubCategoryList.clear()
            for (item in SubCategorylistfromreasons) {
                checkuplicate = true;
                if (tempuniqueSubCategoryList?.size!! > 0) {
                    for (item1 in tempuniqueSubCategoryList!!) {
                        if (item1.uid.equals(item.uid)) {
                            checkuplicate = false;
                            break;
                        }
                    }
                    if (checkuplicate) {
                        tempuniqueSubCategoryList!!.add(item)
                    }
                } else {
                    tempuniqueSubCategoryList!!.add(item)
                }
            }
            uniqueSubCategoryList = tempuniqueSubCategoryList
        } else {
            uniqueSubCategoryList = SubCategorylistfromreasons;
        }
        return uniqueSubCategoryList
    }

    fun hasDuplicates(arr: ArrayList<ReasonmasterV2Response.TicketSubCategory>): Boolean {
        return arr.size != arr.distinct().count();
    }

    fun getreasonlist(departmentname: String): ArrayList<ReasonmasterV2Response.Row> {
        reasonsList.clear()
        val reasonlist = Reasonlistdata.data.listdata.rows
        // var rowuid: String? = null
        for (rowdata in reasonlist) {
            if (rowdata.ticket_sub_category.uid.equals(departmentname)) {
                reasonsList.add(rowdata)
            }
        }
        return reasonsList
    }

    fun getDepartmentData(): ArrayList<DepartmentV2Response.DepartmentListItem> {
        return departmentLiveData
    }

    private fun clearAllList() {
        categoryLiveData.clear()
        departmentLiveData.clear()

        Categorylistfromreasons.clear()
        uniqueCategoryList.clear()

        SubCategorylistfromreasons.clear()
        uniqueSubCategoryList?.clear()
    }


    //newcomplaint registration  api.................
    fun submitNewcomplaintregApi(requestNewComplaintRegistration: RequestNewComplaintRegistration) {
        if (requestNewComplaintRegistration.reason.reason_sla[0].bh_start_time == null) {
            requestNewComplaintRegistration.reason.reason_sla[0].bh_start_time =
                requestNewComplaintRegistration.reason.reason_sla[0].default_tat_hrs.toString()
        }
        if (requestNewComplaintRegistration.reason.reason_sla[0].bh_end_time == null) {
            requestNewComplaintRegistration.reason.reason_sla[0].bh_end_time =
                requestNewComplaintRegistration.reason.reason_sla[0].default_tat_mins.uid
        }
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS SAVETICKET")) {
                val baseUrl = data.APIS[i].URL
                val token = data.APIS[i].TOKEN
                /*  val baseUrl =
                      "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/mobile-ticket-save"*/
                val requestNewComplaintRegistrationJson =
                    Gson().toJson(requestNewComplaintRegistration)

                val header = "application/json"
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getDetails(
                            "h72genrSSNFivOi/cfiX3A==",
                            GetDetailsRequest(
                                baseUrl,
                                "POST",
                                requestNewComplaintRegistrationJson,
                                "",
                                ""
                            )
                        )

//                        RegistrationRepo.NewComplaintRegistration(
//                            baseUrl,
//                            header,
//                            requestNewComplaintRegistration
//                        )
                    }
                    when (response) {

                        is ApiResult.Success -> {
                            //command.value = CmsCommand.RefreshPageOnSuccess(response.value.message)
                            if (response != null) {

                                val resp: String = response.value.string()
                                if (resp != null) {
                                    val res = BackShlash.removeBackSlashes(resp)
                                    val responseNewComplaintRegistration =
                                        Gson().fromJson(
                                            BackShlash.removeSubString(res),
                                            ResponseNewComplaintRegistration::class.java
                                        )
                                    if (responseNewComplaintRegistration.success) {
                                        responsenewcomplaintregistration.value =
                                            responseNewComplaintRegistration
                                    } else {
                                        command.value =
                                            CmsCommand.ShowToast(
                                                responseNewComplaintRegistration.data?.errors?.get(
                                                    0)?.msg.toString())
                                    }


                                }
                            } else {
//                                command.value =
//                                    CmsCommand.ShowToast(response.value.message.toString())


                            }
                        }
                        is ApiResult.GenericError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_unableto_save)
                                    .toString()
                            )
                            //command.value = CmsCommand.ShowToast(ApiResult.)

                        }
                        is ApiResult.NetworkError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_network_error)
                                    .toString()
                            )
                        }
                        is ApiResult.UnknownError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_something_wrong_try_later)
                                    .toString()
                            )
                        }
                        is ApiResult.UnknownHostException -> {
                            command.value =
                                CmsCommand.ShowToast(
                                    context.resources?.getString(R.string.label_something_wrong_try_later)
                                        .toString()
                                )
                        }
                    }
                }
            }
        }
    }


    fun submitApi(registrationSubmit: SubmitNewV2Response) {
//        registrationSubmit.cmode = storeDetailsSend.site
//        registrationSubmit.region = storeDetailsSend.dc_code?.code
//        registrationSubmit.siteId = storeDetailsSend.site
//        registrationSubmit.branchName = storeDetailsSend.store_name
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS APP")) {
                val baseUrl = data.APIS[i].URL
                val token = data.APIS[i].TOKEN
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.submitComplain(
                            token,
                            baseUrl,
                            Config.CMS_Registration,
                            registrationSubmit
                        )
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            command.value =
                                CmsCommand.RefreshPageOnSuccess(response.value.message)
                        }
                        is ApiResult.GenericError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_unknown_err)
                                    .toString()
                            )
                        }
                        is ApiResult.NetworkError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_network_error)
                                    .toString()
                            )
                        }
                        is ApiResult.UnknownError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_something_wrong_try_later)
                                    .toString()
                            )
                        }
                        is ApiResult.UnknownHostException -> {
                            command.value =
                                CmsCommand.ShowToast(
                                    context.resources?.getString(R.string.label_something_wrong_try_later)
                                        .toString()
                                )
                        }
                    }
                }
            }
        }
    }

    fun submitRequestWithImages(submitRequestWithImages: SubmitNewV2Response, unitTag: String) {
//        submitRequestWithImages.cmode = storeDetailsSend.site
//        submitRequestWithImages.region = storeDetailsSend.dc_code?.code
//        submitRequestWithImages.siteId = storeDetailsSend.site
//        submitRequestWithImages.branchName = storeDetailsSend.store_name
        if (unitTag.equals("NEWBATCH")) {
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("CMS APP")) {
                    val baseUrl = data.APIS[i].URL
                    val token = data.APIS[i].TOKEN
                    viewModelScope.launch {
                        val response = withContext(Dispatchers.IO) {
                            RegistrationRepo.submitComplainWithImages(
                                token,
                                baseUrl,
                                Config.CMS_Registration,
                                submitRequestWithImages
                            )
                        }
                        state.value = State.SUCCESS
                        when (response) {
                            is ApiResult.Success -> {
                                command.value =
                                    CmsCommand.RefreshPageOnSuccess(response.value.message)
                                state.value = State.ERROR
                            }
                            is ApiResult.NetworkError -> {
                                state.value = State.ERROR
                                command.value = CmsCommand.ShowToast(
                                    context.resources?.getString(R.string.label_network_error)
                                        .toString()
                                )
                            }
                            is ApiResult.GenericError -> {
                                state.value = State.ERROR
                                command.value = CmsCommand.ShowToast(
                                    context.resources?.getString(R.string.label_unknown_err)
                                        .toString()
                                )
                            }
                            is ApiResult.UnknownError -> {
                                state.value = State.ERROR
                                command.value = CmsCommand.ShowToast(
                                    context.resources?.getString(R.string.label_something_wrong_try_later)
                                        .toString()
                                )
                            }
                            is ApiResult.UnknownHostException -> {
                                state.value = State.ERROR
                                command.value =
                                    CmsCommand.ShowToast(
                                        context.resources?.getString(R.string.label_something_wrong_try_later)
                                            .toString()
                                    )
                            }
                        }
                    }
                }
            }
        } else {
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("CMS APP")) {
                    val baseUrl = data.APIS[i].URL
                    val token = data.APIS[i].TOKEN
                    viewModelScope.launch {
                        val response = withContext(Dispatchers.IO) {
                            RegistrationRepo.submitComplainWithImages(
                                token,
                                baseUrl,
                                Config.CMS_Registration,
                                submitRequestWithImages
                            )
                        }
                        state.value = State.SUCCESS
                        when (response) {
                            is ApiResult.Success -> {
                                command.value =
                                    CmsCommand.RefreshPageOnSuccess(response.value.message)
                                state.value = State.ERROR
                            }
                            is ApiResult.NetworkError -> {
                                state.value = State.ERROR
                                command.value = CmsCommand.ShowToast(
                                    context.resources?.getString(R.string.label_network_error)
                                        .toString()
                                )
                            }
                            is ApiResult.GenericError -> {
                                state.value = State.ERROR
                                command.value = CmsCommand.ShowToast(
                                    context.resources?.getString(R.string.label_unknown_err)
                                        .toString()
                                )
                            }
                            is ApiResult.UnknownError -> {
                                state.value = State.ERROR
                                command.value = CmsCommand.ShowToast(
                                    context.resources?.getString(R.string.label_something_wrong_try_later)
                                        .toString()
                                )
                            }
                            is ApiResult.UnknownHostException -> {
                                state.value = State.ERROR
                                command.value =
                                    CmsCommand.ShowToast(
                                        context.resources?.getString(R.string.label_something_wrong_try_later)
                                            .toString()
                                    )
                            }
                        }
                    }
                }
            }
        }
    }


    fun connectToAzure(image: ArrayList<ImageDataDto>, tag: String) {
        state.value = State.SUCCESS
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                ConnectionAzure.connectToAzur(image,
                    CONTAINER_NAME,
                    STORAGE_CONNECTION_FOR_CCR_APP)
            command.postValue(CmsCommand.ImageIsUploadedInAzur(response, tag))
        }
    }

    fun getSiteData(): ArrayList<StoreListItem> {
        return siteLiveData
    }

    fun getSelectedStoreDetails(storeDetails: StoreListItem) {
        storeDetailsSend = storeDetails
    }

    fun getListOfPendingAcknowledgement(trackingListDto: StoreListItem) {
        Utils.printMessage(TAG, "pendingAcknowledgement" + trackingListDto.toString())
        state.value = State.SUCCESS
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS APP")) {
                val baseUrl = data.APIS[i].URL
                val token = data.APIS[i].TOKEN
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getListOfAcknowledgement(
                            token,
                            baseUrl,
                            Config.CMS_Tickets_List,
                            TrackingListDto(trackingListDto.site!!)
                        )
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            pendingListLiveData.value = response.value
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }

    fun submitRequestOfAcknowledgment(submitAcknowledge: SubmitAcknowledge) {
        state.value = State.SUCCESS
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS APP")) {
                val baseUrl = data.APIS[i].URL
                val token = data.APIS[i].TOKEN
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.submitAcknowledgement(
                            token,
                            baseUrl,
                            Config.CMS_Update_Ack,
                            submitAcknowledge
                        )
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            command.value = CmsCommand.InVisibleLayout(response.value.message)
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_network_error)
                                    .toString()
                            )
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_unknown_err)
                                    .toString()
                            )
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_something_wrong_try_later)
                                    .toString()
                            )
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            state.value = State.ERROR
                            command.value =
                                CmsCommand.ShowToast(
                                    context.resources?.getString(R.string.label_something_wrong_try_later)
                                        .toString()
                                )
                        }
                    }
                }
            }
        }
    }

    fun registerUserWithSiteID(
        userSiteIDRegReqModel: UserSiteIDRegReqModel,
        slectedStoreItem: StoreListItem,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS SAVE EMPLOYEE")) {
                val baseUrl = data.APIS[i].URL
                val token = data.APIS[i].TOKEN
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.submitEmpWithSiteIDReg(
                            token,
                            baseUrl,
                            userSiteIDRegReqModel
                        )
                    }
                    state.value = State.SUCCESS
                    when (response) {
                        is ApiResult.Success -> {
                           // getDepartment()
                            command.value = CmsCommand.CheckValidatedUserWithSiteID(
                                response.value.MESSAGE,
                                slectedStoreItem
                            )
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_network_error)
                                    .toString()
                            )
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_unknown_err)
                                    .toString()
                            )
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_something_wrong_try_later)
                                    .toString()
                            )
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            state.value = State.ERROR
                            command.value =
                                CmsCommand.ShowToast(
                                    context.resources?.getString(R.string.label_something_wrong_try_later)
                                        .toString()
                                )
                        }
                    }
                }
            }
        }
    }

    fun updateDefaultSiteIdApiCall(updateUserDefaultSiteRequest: UpdateUserDefaultSiteRequest) {
        val updateUserDefaultSiteRequestJson = Gson().toJson(updateUserDefaultSiteRequest)
     //
      //https://apis.v35.dev.zeroco.de-////apollocms
        val baseUrl: String =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/user/save-update/update-user-default-site"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                SwachApiiRepo.updateSwachhDefaultSite(
                    "h72genrSSNFivOi/cfiX3A==",
                    GetDetailsRequest(
                        baseUrl,
                        "POST",
                        updateUserDefaultSiteRequestJson,
                        "",
                        ""
                    )
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val updateUserDefaultSiteResponse =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    UpdateUserDefaultSiteResponse::class.java
                                )
                            if (updateUserDefaultSiteResponse.success!!) {
                                updateUserDefaultSiteResponseMutable.value =
                                    updateUserDefaultSiteResponse

//                                updateSwachhDefaultSiteResponseModel.value =
//                                    updateSwachhDefaultSiteResponse
                            } else {

                            }
                        }
                    } else {
                    }
                }
                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
    }

    fun fetchItemDetails(key: String?) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("CMS OPENTICKETLIST")) {
        /* var baseUrl =
                     "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/site/select/site-details?"*/
        //val token = data.APIS[i].TOKEN
        var baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket_inventory_item/list/fetch-item-code?page=1&rows=10&" +
                "globalFilter%5BfieldName%5D=globalFilter&globalFilter%5Bkey%5D=globalFilter&globalFilter%5Bvalue%5D=" +
                key +
                "&globalFilter%5BmatchType%5D=any&sort%5B0%5D%5Bkey%5D=artcode&sort%5B0%5D%5Border%5D=ASC"

        baseUrl = baseUrl
        viewModelScope.launch {
            state.value = State.SUCCESS
            // RegistrationRepo.getticketresolvedstatus(site,department)
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    "h72genrSSNFivOi/cfiX3A==",
                    GetDetailsRequest(
                        baseUrl,
                        "GET",
                        "The",
                        "",
                        ""
                    )
                )
                // RegistrationRepo.getticketresolvedstatus(baseUrl)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    // if (!response.value.success) {
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val resString = BackShlash.removeSubString(res)
                            val responseTicktResolvedapi =
                                Gson().fromJson(
                                    resString,
                                    FetchItemModel::class.java
                                )
                            inventoryCategotyItem = responseTicktResolvedapi.data.listData.rows.get(0)
//                                    if (!responseTicktResolvedapi.success) {
//                                        tisketstatusresponse.value = responseTicktResolvedapi
//                                    } else {
//                                        command.value = CmsCommand.ShowToast(
//                                            responseTicktResolvedapi.toString()
//                                        )
//                                    }
                        }
                    }

                }
                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
//            }
//        }
    }

    fun fetchTransactionPOSDetails(key: String?) {

        var baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/site_pine_lab_device/list?site%5Buid%5D="+key

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    "h72genrSSNFivOi/cfiX3A==",
                    GetDetailsRequest(
                        baseUrl,
                        "GET",
                        "The",
                        "",
                        ""
                    )
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    // if (!response.value.success) {
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val resString = BackShlash.removeSubString(res)
                            val responseTicktResolvedapi =
                                Gson().fromJson(
                                    resString,
                                    TransactionPOSModel::class.java
                                )
                            transactionPOSDetails.value = responseTicktResolvedapi

                        }
                    }

                }
                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
//            }
//        }
    }


    fun uploadImage(imageFromCameraFile: ArrayList<ImageDataDto>, tag: String) {

        state.value = State.SUCCESS
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                ConnectionAzure.connectToAzur(imageFromCameraFile,
                    CONTAINER_NAME,
                    STORAGE_CONNECTION_FOR_CCR_APP)
            command.postValue(CmsCommand.ImageIsUploadedInAzur(response, tag))
        }
//        val requestBody = RequestBody.create("image/png".toMediaTypeOrNull(),imageFromCameraFile)
//        val filePart = MultipartBody.Part.createFormData("file", imageFromCameraFile.name, requestBody)
//
//        viewModelScope.launch {
//            state.value = State.SUCCESS
//            val response = withContext(Dispatchers.IO) {
//                RegistrationRepo.uploadImage(
//                    "h72genrSSNFivOi/cfiX3A==",
//                    GetDetailsRequest(
//                        "https://cmsuat.apollopharmacy.org/zc-v3.1-fs-svc/2.0/apollo_cms/upload",
//                        "POST",
//                        filePart,
//                        "",
//                        ""
//                    )
//                )
//            }
//            when (response) {
//                is ApiResult.Success -> {
//                    state.value = State.ERROR
////                    when (requestCode) {
////                        1 -> {
////                            ticketInventoryItem.front_image = response.value.data
////                        }
////                        2 -> {
////                            ticketInventoryItem.back_image = response.value.data
////                        }
////                        3 -> {
////                            ticketInventoryItem.other_image = response.value.data
////                        }
////                    }
//
//                }
//                is ApiResult.GenericError -> {
//                    state.value = State.ERROR
//                }
//                is ApiResult.NetworkError -> {
//                    state.value = State.ERROR
//                }
//                is ApiResult.UnknownError -> {
//                    state.value = State.ERROR
//                }
//                is ApiResult.UnknownHostException -> {
//                    state.value = State.ERROR
//                }
//            }
//        }
    }


    fun submitTicketInventorySaveUpdate(requestNewComplaintRegistration: RequestSaveUpdateComplaintRegistration) {
        if (requestNewComplaintRegistration.reason.reason_sla[0].bh_start_time == null) {
            requestNewComplaintRegistration.reason.reason_sla[0].bh_start_time =
                requestNewComplaintRegistration.reason.reason_sla[0].default_tat_hrs.toString()
        }
        if (requestNewComplaintRegistration.reason.reason_sla[0].bh_end_time == null) {
            requestNewComplaintRegistration.reason.reason_sla[0].bh_end_time =
                requestNewComplaintRegistration.reason.reason_sla[0].default_tat_mins.uid
        }
                  val baseUrl =
                      "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/ticket-inventory-save-update"
                val requestNewComplaintRegistrationJson =
                    Gson().toJson(requestNewComplaintRegistration)
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getDetails(
                            "h72genrSSNFivOi/cfiX3A==",
                            GetDetailsRequest(
                                baseUrl,
                                "POST",
                                requestNewComplaintRegistrationJson,
                                "",
                                ""
                            )
                        )
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            if (response != null) {
                                val resp: String = response.value.string()
                                if (resp != null) {
                                    val res = BackShlash.removeBackSlashes(resp)
                                    val responseNewComplaintRegistration =
                                        Gson().fromJson(
                                            BackShlash.removeSubString(res),
                                            ResponseNewComplaintRegistration::class.java
                                        )
                                    if (responseNewComplaintRegistration.success) {
                                        responsenewcomplaintregistration.value =
                                            responseNewComplaintRegistration
                                    } else {
                                        command.value =
                                            CmsCommand.ShowToast(
                                                responseNewComplaintRegistration.data?.errors?.get(
                                                    0)?.msg.toString())
                                    }
                                }
                            }
                        }
                        is ApiResult.GenericError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_unableto_save)
                                    .toString()
                            )
                        }
                        is ApiResult.NetworkError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_network_error)
                                    .toString()
                            )
                        }
                        is ApiResult.UnknownError -> {
                            command.value = CmsCommand.ShowToast(
                                context.resources?.getString(R.string.label_something_wrong_try_later)
                                    .toString()
                            )
                        }
                        is ApiResult.UnknownHostException -> {
                            command.value =
                                CmsCommand.ShowToast(
                                    context.resources?.getString(R.string.label_something_wrong_try_later)
                                        .toString()
                                )
                        }
                    }
                }
    }

}

sealed class CmsCommand {
    data class VisibleLayout(val message: String) : CmsCommand()
    data class InVisibleLayout(val message: String) : CmsCommand()
    data class RefreshPageOnSuccess(val message: String) : CmsCommand()
    data class ImageIsUploadedInAzur(val filePath: ArrayList<ImageDataDto>, val tag: String) :
        CmsCommand()

    data class SuccessDeptList(val message: String) : CmsCommand()
    data class ShowToast(val message: String) : CmsCommand()
    data class CheckValidatedUserWithSiteID(
        val message: String,
        val slectedStoreItem: StoreListItem,
    ) : CmsCommand()

    data class ShowSiteInfo(val message: String) : CmsCommand()
}