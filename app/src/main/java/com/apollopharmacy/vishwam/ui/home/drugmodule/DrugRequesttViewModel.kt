package com.apollopharmacy.vishwam.ui.home.drugmodule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.azure.ConnectionToAzure
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.Image
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.cms.*
import com.apollopharmacy.vishwam.data.network.*
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DrugReason
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DrugRequest
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DrugResponse
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.ItemTypeDropDownResponse
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DrugFragmentViewModel : ViewModel() {
    val commands = LiveEvent<Commands>()
    val state = MutableLiveData<State>()
    var itemTypeDropDownResponse = MutableLiveData<ItemTypeDropDownResponse>()

    //    var command = LiveEvent<CmsCommand>()
    var siteLiveData = ArrayList<StoreListItem>()
    private var storeDetailsSend = StoreListItem()
    lateinit var selectedSubCategory: ReasonmasterV2Response.TicketSubCategory

    var drugList = MutableLiveData<DrugResponse>()

    fun getDrugList(drugRequest: DrugRequest, mCallback: DrugFragmentCallback) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("ND add_article")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                SwachhListApiResponse.getDrugResponse(baseUrl, token, drugRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.requestStatus ?: null == true) {
//                        state.value = State.ERROR
//                        drugList.value = result.value


                        var itemType = RequestSaveUpdateComplaintRegistration.ItemType()
                        itemType.uid = drugRequest.itemType

                        var doctorSpecialty =
                            RequestSaveUpdateComplaintRegistration.DoctorSpecialty()
                        doctorSpecialty.uid = drugRequest.doctorSpecialty


                        var drugList =
                            ArrayList<RequestSaveUpdateComplaintRegistration.DrugRequest>()
                        drugList.add(RequestSaveUpdateComplaintRegistration.DrugRequest(if (drugRequest.images?.size!! > 0) {
                            drugRequest.images?.get(0)?.imageURL
                        } else null,
//                            drugRequest.images?.get(0)?.imageURL,
                            if (drugRequest.images?.size!! > 1) {
                                drugRequest.images?.get(1)?.imageURL
                            } else null,
//                            drugRequest.images?.get(1)?.imageURL,
                            if (drugRequest.images?.size!! > 2) {
                                drugRequest.images?.get(2)?.imageURL
                            } else null,
                            if (drugRequest.images?.size!! > 3) {
                                drugRequest.images?.get(3)?.imageURL
                            } else null,
                            drugRequest.batch,
                            drugRequest.barCode,
                            Utils.getticketlistfiltersdate(drugRequest.manufactureDate),
                            Utils.getticketlistfiltersdate(drugRequest.expiryDate),
                            drugRequest.purchasePrice!!.toDouble(),
                            drugRequest.mrp!!.toDouble(),
                            result.value.referenceId,
                            drugRequest.packSize,
                            drugRequest.hSNCode,
                            drugRequest.gst!!.toDouble(),
                            drugRequest.itemName,
                            drugRequest.remarks,
                            itemType,
                            doctorSpecialty,
                            drugRequest.doctorName,
                            drugRequest.requiredQty))
                        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                            Locale.ENGLISH).format(Date())
                        submitTicketInventorySaveUpdate(RequestSaveUpdateComplaintRegistration(
                            LoginRepo.getProfile()!!.EMPID,
                            currentTime,
                            drugRequest.description,
                            RequestSaveUpdateComplaintRegistration.Platform("mobile"),
                            RequestSaveUpdateComplaintRegistration.Category(reasonData.data.ticket_category.uid),
                            RequestSaveUpdateComplaintRegistration.Department(reasonData.data.department.uid,
                                reasonData.data.department.code),
                            RequestSaveUpdateComplaintRegistration.Site(drugRequest.site?.uid,
                                drugRequest.site?.site,
                                drugRequest.site?.store_name),
                            RequestSaveUpdateComplaintRegistration.Reason(reasonData.data.uid,
                                reasonData.data.reason_sla),
                            RequestSaveUpdateComplaintRegistration.Subcategory(selectedSubCategory.uid),
                            RequestSaveUpdateComplaintRegistration.TicketInventory(null,
                                drugList,
                                null),
                            RequestSaveUpdateComplaintRegistration.TicketType("64D9D9BE4A621E9C13A2C73404646655",
                                "store",
                                "store"),
                            tisketstatusresponse.data.region,
                            tisketstatusresponse.data.cluster,
                            tisketstatusresponse.data.phone_no,
                            tisketstatusresponse.data.executive,
                            tisketstatusresponse.data.manager,
                            tisketstatusresponse.data.region_head,
                        ), mCallback)
                    } else {
                        state.value = State.ERROR
                        commands.postValue(Commands.ShowToast(result.value.message))
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(result.error?.let {
                        Commands.ShowToast(it)
                    })
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(Commands.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(Commands.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(Commands.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }

    var responsenewcomplaintregistration = MutableLiveData<ResponseNewComplaintRegistration>()

    fun submitTicketInventorySaveUpdate(
        requestNewComplaintRegistration: RequestSaveUpdateComplaintRegistration,
        mCallback: DrugFragmentCallback,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseProxyUrl = ""
        var token = ""

        var baseUrl = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseProxyUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS ticket_inventory_save_update")) {
                baseUrl = data.APIS[i].URL
//                token = data.APIS[i].TOKEN
                break
            }
        }

        if (requestNewComplaintRegistration.reason.reason_sla?.get(0)?.bh_start_time == null) {
            requestNewComplaintRegistration.reason.reason_sla?.get(0)?.bh_start_time =
                requestNewComplaintRegistration.reason.reason_sla?.get(0)?.default_tat_hrs.toString()
        }
        if (requestNewComplaintRegistration.reason.reason_sla?.get(0)?.bh_end_time == null) {
            requestNewComplaintRegistration.reason.reason_sla?.get(0)?.bh_end_time =
                requestNewComplaintRegistration.reason.reason_sla?.get(0)?.default_tat_mins?.uid
        }
//        val baseUrl =
//            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/ticket-inventory-save-update"
        val requestNewComplaintRegistrationJson = Gson().toJson(requestNewComplaintRegistration)
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(baseProxyUrl,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", ""))
            }
            when (response) {
                is ApiResult.Success -> {
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val responseNewComplaintRegistration =
                                Gson().fromJson(BackShlash.removeSubString(res),
                                    ResponseNewComplaintRegistration::class.java)
                            if (responseNewComplaintRegistration.success) {
                                responsenewcomplaintregistration.value =
                                    responseNewComplaintRegistration
                            } else {
                                mCallback.onFailureMessage(responseNewComplaintRegistration.data.ticket_id!!,
                                    responseNewComplaintRegistration.data.createdUser!!.firstName+ "  " + responseNewComplaintRegistration.data.createdUser!!.middleName + "  " + responseNewComplaintRegistration.data.createdUser!!.lastName,
                                    responseNewComplaintRegistration.data?.errors?.get(0)?.msg.toString())


//                                commands.postValue(Commands.ShowToast(
//                                    responseNewComplaintRegistration.data?.errors?.get(0)?.msg.toString()))

                            }
                        }
                    }
                }
                is ApiResult.GenericError -> {
                    commands.value =
                        Commands.ShowToast(ViswamApp.context.resources?.getString(R.string.label_unableto_save)
                            .toString())
                }
                is ApiResult.NetworkError -> {
                    commands.value =
                        Commands.ShowToast(ViswamApp.context.resources?.getString(R.string.label_network_error)
                            .toString())
                }
                is ApiResult.UnknownError -> {
                    commands.value =
                        Commands.ShowToast(ViswamApp.context.resources?.getString(R.string.label_something_wrong_try_later)
                            .toString())
                }
                is ApiResult.UnknownHostException -> {
                    commands.value =
                        Commands.ShowToast(ViswamApp.context.resources?.getString(R.string.label_something_wrong_try_later)
                            .toString())
                }
            }
        }
    }

    lateinit var reasonData: DrugReason

    fun fetchTransactionPOSDetails() {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseProxyUrl = ""
        var token = ""
        var baseUrl = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseProxyUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("ND reason_code")) {
                baseUrl = data.APIS[i].URL
//                token = data.APIS[i].TOKEN
                break
            }
        }


//        URL":"https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/reason/select/reason-details-by-code-for-mobile?","NAME":"ND reason_code","TOKEN":""},
//        var baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/reason/select/reason-details-by-code-for-mobile?reason_code=new_drug"
        baseUrl = baseUrl + "reason_code=new_drug"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(baseProxyUrl,
                    token,
                    GetDetailsRequest(baseUrl, "GET", "The", "", ""))
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
                                Gson().fromJson(resString, DrugReason::class.java)
                            reasonData = responseTicktResolvedapi

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

    lateinit var tisketstatusresponse: ResponseTicktResolvedapi
    fun getTicketstatus(site: String?, department: String?) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)

        var baseProxyUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseProxyUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
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
                        RegistrationRepo.getDetails(baseProxyUrl,
                            token,
                            GetDetailsRequest(baseUrl, "GET", "The", "", ""))
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
                                        Gson().fromJson(BackShlash.removeSubString(res),
                                            ResponseTicktResolvedapi::class.java)
                                    tisketstatusresponse = responseTicktResolvedapi
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun connectToAzure(image: ArrayList<Image>) {
        state.value = State.SUCCESS
        viewModelScope.launch(Dispatchers.IO) {
            val response = ConnectionToAzure.connectToAzur(image,
                Config.CONTAINER_NAME,
                Config.STORAGE_CONNECTION_FOR_CCR_APP)

            commands.postValue(Commands.DrugImagesUploadInAzur(response))
        }
    }

    fun siteId() {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseProxyUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseProxyUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        if (Preferences.isSiteIdListFetched()) {
            siteLiveData.clear()
            val gson = Gson()
            val siteIdList = Preferences.getSiteIdListJson()
            val type = object : TypeToken<List<StoreListItem?>?>() {}.type

            this.siteLiveData =
                gson.fromJson<List<StoreListItem>>(siteIdList, type) as ArrayList<StoreListItem>
            commands.value = Commands.ShowSiteInfo("")
        } else {

            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("CMS GETSITELIST")) {
                    val baseUrl = data.APIS[i].URL
//                    val token = data.APIS[i].TOKEN
                    viewModelScope.launch {
                        state.value = State.SUCCESS
                        val response = withContext(Dispatchers.IO) {
                            RegistrationRepo.getDetails(baseProxyUrl,
                                token,
                                GetDetailsRequest(baseUrl, "GET", "The", "", ""))
//                        RegistrationRepo.selectSiteId(token, baseUrl)
                        }
                        when (response) {
                            is ApiResult.Success -> {
                                state.value = State.ERROR
                                val resp: String = response.value.string()
                                val res = BackShlash.removeBackSlashes(resp)
                                val reasonmasterV2Response =
                                    Gson().fromJson(BackShlash.removeSubString(res),
                                        SiteDto::class.java)

                                if (reasonmasterV2Response.status) {
                                    siteLiveData.clear()
                                    reasonmasterV2Response.siteData?.listData?.rows?.map {
                                        siteLiveData.add(it)
                                    }
                                    // getDepartment()
                                    commands.value = Commands.ShowSiteInfo("")
                                } else {
                                    commands.value =
                                        Commands.ShowToast(reasonmasterV2Response.message.toString())
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


    var deartmentlist = java.util.ArrayList<ReasonmasterV2Response.Department>()
    lateinit var Reasonlistdata: ReasonmasterV2Response
    var reasonlistapiresponse = MutableLiveData<ReasonmasterV2Response>()
    fun getRemarksMasterList() {
        val url = Preferences.getApi()

        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseProxyUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseProxyUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        val userData = LoginRepo.getProfile()!!
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS REASONLIST")) {
                var baseUrl = data.APIS[i].URL + "emp_id=" + userData.EMPID + "&page=1&rows=1000"
                viewModelScope.launch {
                    state.value = State.SUCCESS
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getDetails(baseProxyUrl,
                            token,
                            GetDetailsRequest(baseUrl, "GET", "The", "", ""))
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
                                        Gson().fromJson(BackShlash.removeSubString(res),
                                            ReasonmasterV2Response::class.java)

                                    if (reasonmasterV2Response.success) {
                                        Reasonlistdata = reasonmasterV2Response
                                        reasonlistapiresponse.value = reasonmasterV2Response
                                        val reasonlitrows =
                                            reasonmasterV2Response.data.listdata.rows
                                        for (row in reasonlitrows) {
                                            deartmentlist.add(row.department)
                                        }
                                        getSubCategoriesfromReasons("")
                                    } else {
                                        commands.postValue(Commands.ShowToast(reasonmasterV2Response.message.toString()))
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

    var subCategories = MutableLiveData<ArrayList<ReasonmasterV2Response.TicketSubCategory>>()
    var SubCategorylistfromreasons = java.util.ArrayList<ReasonmasterV2Response.TicketSubCategory>()
    var uniqueSubCategoryList = java.util.ArrayList<ReasonmasterV2Response.TicketSubCategory>()
    fun getSubCategoriesfromReasons(departmentname: String): java.util.ArrayList<ReasonmasterV2Response.TicketSubCategory> {

        var tempuniqueSubCategoryList =
            java.util.ArrayList<ReasonmasterV2Response.TicketSubCategory>()
        val reasonlist = Reasonlistdata.data.listdata.rows
        for (rowdata in reasonlist) {
            if (rowdata.ticket_category.code.equals("new_drug_req")) {
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
        subCategories.value = uniqueSubCategoryList
        return uniqueSubCategoryList
    }


    fun getSiteData(): ArrayList<StoreListItem> {
        return siteLiveData
    }

    fun getSelectedStoreDetails(storeDetails: StoreListItem) {
        storeDetailsSend = storeDetails
    }


    fun getNames(): ArrayList<String> {

        var names = ArrayList<String>()

        names.add("Pharma")
        names.add("FMCG")
        names.add("Surgical")
        names.add("Homeopathy")
        names.add("Sports")
        names.add("Ayurveda")


        return names

    }


    sealed class Commands {
        data class ShowToast(val message: String?) : Commands()
        data class DrugImagesUploadInAzur(val filePath: ArrayList<Image>) : Commands()

        data class CheckValidatedUserWithSiteID(
            val message: String,
            val slectedStoreItem: StoreListItem,
        ) : Commands()


        data class ShowSiteInfo(val message: String) : Commands()

    }

    fun itemTypeApi(drugFragmentCallback: DrugFragmentCallback) {
        if (Preferences.isItemTypeListFetched()) {

        } else {
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)
            var baseProxyUrl = ""
            var proxyToken = ""

            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                    baseProxyUrl = data.APIS[i].URL
                    proxyToken = data.APIS[i].TOKEN
                    break
                }
            }
            var baseUrl = ""
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("ND item_type")) {
                    baseUrl = data.APIS[i].URL
                    break
                }
            }

//        var baseUrl =
//            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/choose-data/item_type"

            viewModelScope.launch {
                val response = withContext(Dispatchers.IO) {
                    RegistrationRepo.getDetails(baseProxyUrl,
                        proxyToken,
                        GetDetailsRequest(baseUrl, "GET", "Get", "", ""))
                }
                when (response) {
                    is ApiResult.Success -> {
                        if (response != null) {
                            val resp: String = response.value.string()
                            if (resp != null) {
                                val res = BackShlash.removeBackSlashes(resp)
                                val chooseDataItemType =
                                    Gson().fromJson(BackShlash.removeSubString(res),
                                        ItemTypeDropDownResponse::class.java)
                                if (chooseDataItemType.success) {
                                    if (drugFragmentCallback != null) {
                                        drugFragmentCallback.onSuccessItemTypeApi(chooseDataItemType)
                                    }
                                }
                            }
                        }
                    }
                    is ApiResult.GenericError -> {
                        commands.value =
                            Commands.ShowToast(ViswamApp.context.resources?.getString(R.string.label_unableto_save)
                                .toString())
                    }
                    is ApiResult.NetworkError -> {
                        commands.value =
                            Commands.ShowToast(ViswamApp.context.resources?.getString(R.string.label_network_error)
                                .toString())
                    }
                    is ApiResult.UnknownError -> {
                        commands.value =
                            Commands.ShowToast(ViswamApp.context.resources?.getString(R.string.label_something_wrong_try_later)
                                .toString())
                    }
                    is ApiResult.UnknownHostException -> {
                        commands.value =
                            Commands.ShowToast(ViswamApp.context.resources?.getString(R.string.label_something_wrong_try_later)
                                .toString())
                    }
                }
            }
        }
    }

    fun doctorSpecialityApi(drugFragmentCallback: DrugFragmentCallback) {
        if (Preferences.isDoctorSpecialityListFetched()) {

        } else {

            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)
            var baseProxyUrl = ""
            var proxyToken = ""

            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                    baseProxyUrl = data.APIS[i].URL
                    proxyToken = data.APIS[i].TOKEN
                    break
                }
            }
            var baseUrl = ""
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("ND doctor_specality")) {
                    baseUrl = data.APIS[i].URL
                    break
                }
            }
//        var baseUrl =
//            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/choose-data/doctor_specialty"

            viewModelScope.launch {
                val response = withContext(Dispatchers.IO) {
                    RegistrationRepo.getDetails(baseProxyUrl,
                        proxyToken,
                        GetDetailsRequest(baseUrl, "GET", "Get", "", ""))
                }
                when (response) {
                    is ApiResult.Success -> {
                        if (response != null) {
                            val resp: String = response.value.string()
                            if (resp != null) {
                                val res = BackShlash.removeBackSlashes(resp)
                                val chooseDataItemType =
                                    Gson().fromJson(BackShlash.removeSubString(res),
                                        ItemTypeDropDownResponse::class.java)
                                if (chooseDataItemType.success) {
                                    if (drugFragmentCallback != null) {
                                        drugFragmentCallback.onSuccessDoctorSpecialityApi(
                                            chooseDataItemType)
                                    }
                                }
                            }
                        }
                    }
                    is ApiResult.GenericError -> {
                        commands.value =
                            Commands.ShowToast(ViswamApp.context.resources?.getString(R.string.label_unableto_save)
                                .toString())
                    }
                    is ApiResult.NetworkError -> {
                        commands.value =
                            Commands.ShowToast(ViswamApp.context.resources?.getString(R.string.label_network_error)
                                .toString())
                    }
                    is ApiResult.UnknownError -> {
                        commands.value =
                            Commands.ShowToast(ViswamApp.context.resources?.getString(R.string.label_something_wrong_try_later)
                                .toString())
                    }
                    is ApiResult.UnknownHostException -> {
                        commands.value =
                            Commands.ShowToast(ViswamApp.context.resources?.getString(R.string.label_something_wrong_try_later)
                                .toString())
                    }
                }
            }
        }
    }

}