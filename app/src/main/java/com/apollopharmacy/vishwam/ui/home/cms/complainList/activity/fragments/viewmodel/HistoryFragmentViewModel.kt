package com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.apollopharmacy.vishwam.data.model.cms.ResponseticketRatingApi
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.ComplaintsListDetailsCallback
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.callback.HistoryCallback
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.model.SubworkFlowAssignedtoMeRequest
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.model.SubworkFlowAssignedtoMeResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.CCAcceptRejectModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.CCAcceptRejectResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.ChangeManagerRequest
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.ChangeManagerResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.CmsTicketRequest
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.CmsTicketResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Data
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.ForwardToManagerModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.GetManagersModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.InventoryAcceptRejectResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.InventoryAcceptrejectModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.SubWorkflowAcceptRequest
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.SubWorkflowAcceptResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.SubWorkflowRejectRequest
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.SubWorkflowRejectResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.SubworkflowConfigDetailsResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketData
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketResolveCloseModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketSubworkflowActionUpdateRequest
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketSubworkflowActionUpdateResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.UserListForSubworkflowResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.WorkFlowUpdateModel
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class HistoryFragmentViewModel : ViewModel() {

    var baseUrL = ""
    var token = ""
    var cmsTicketResponseList = MutableLiveData<CmsTicketResponse>()
    var command = LiveEvent<CmsCommand>()
    var getManagersLiveData = MutableLiveData<Data>()

    val state = MutableLiveData<State>()

    fun cmsTicketStatusUpdate(cmsTicketRequest: CmsTicketRequest) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket_it/save-update/it-cc-frwd-to-fin-status-update"
        var token1 = ""
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("")) {
//                baseUrl = data.APIS[i].URL
//                token1 = data.APIS[i].TOKEN
//                break
//            }
//        }
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        val requestcmsTicketRequestJson = Gson().toJson(cmsTicketRequest)
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestcmsTicketRequestJson, "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val cmsTicketResponse = Gson().fromJson(
                                BackShlash.removeSubString(res), CmsTicketResponse::class.java
                            )

                            cmsTicketResponseList.value = cmsTicketResponse


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

    fun actionCCAccept(
        request: CCAcceptRejectModel?,
        itemPos: Int,
        complaintsListDetailsCallback: ComplaintsListDetailsCallback,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token1 = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS pos_ticket_accept_reject_update")) {
                baseUrl = data.APIS[i].URL
                token1 = data.APIS[i].TOKEN
                break
            }
        }
//        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
//        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/pos-ticket-accept-reject-update"
        val requestNewComplaintRegistrationJson = Gson().toJson(request)
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val request = Gson().fromJson(
                                BackShlash.removeSubString(res), CCAcceptRejectResponse::class.java
                            )
                            if (request.success) {
                                complaintsListDetailsCallback.onClickBack()
//                                command.value = CmsCommand.RefreshPageOnSuccess(
//                                    request.message
//                                )
                            } else {
                                command.value = CmsCommand.ShowToast(request.message.toString())
                            }

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

    fun actionInventoryAcceptReject(
        request: InventoryAcceptrejectModel?,
        workFlowUpdateModel: WorkFlowUpdateModel,
        itemPos: Int,
        complaintsListDetailsCallback: ComplaintsListDetailsCallback,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS inventory_items_approve_reject_update")) {
                baseUrl = data.APIS[i].URL
                break
            }
        }
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }

//        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket_inventory_item/save-update/inventory-items-approve-reject-update"
        val requestNewComplaintRegistrationJson = Gson().toJson(request)
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val responseNewTicketlistNewTicketHistoryResponse = Gson().fromJson(
                                BackShlash.removeSubString(res),
                                InventoryAcceptRejectResponse::class.java
                            )
                            if (responseNewTicketlistNewTicketHistoryResponse.success) {
                                complaintsListDetailsCallback.onClickBack()
//                                command.value = CmsCommand.RefreshPageOnSuccess(
//                                    responseNewTicketlistNewTicketHistoryResponse.data.uid
//                                )
//                                command.value = CmsCommand.ShowToast(
//                                    responseNewTicketlistNewTicketHistoryResponse.data.uid.toString()
//                                )
//                                actionWorkflowUpdate(workFlowUpdateModel,itemPos)
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    responseNewTicketlistNewTicketHistoryResponse.data.res.toString()
                                )
                            }

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

    fun actionForwardToManager(
        request: ForwardToManagerModel?,
        itemPos: Int,
        complaintsListDetailsCallback: ComplaintsListDetailsCallback,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS inventory_items_request_save")) {
                baseUrl = data.APIS[i].URL
                break
            }
        }

        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
//        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket_inventory_item/save-update/inventory-items-request-save"
        val requestNewComplaintRegistrationJson = Gson().toJson(request)
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val response = Gson().fromJson(
                                BackShlash.removeSubString(res),
                                InventoryAcceptRejectResponse::class.java
                            )
                            if (response.success) {
                                complaintsListDetailsCallback.onClickBack()
                                command.value = CmsCommand.RefreshPageOnSuccess(response.message)
                                command.value = CmsCommand.ShowToast(response.message.toString())
                            } else {
                                command.value = CmsCommand.ShowToast(response.message.toString())
                            }

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

    fun getManagers(uid: String) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS store_manager_list_for_change_manager")) {
                baseUrl = data.APIS[i].URL
                break
            }
        }
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL, token,
//                            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/user/list/store-manager-list-for-change-manager?page=1&rows=10&globalFilter%5BfieldName%5D=globalFilter&globalFilter%5Bkey%5D=globalFilter&globalFilter%5Bvalue%5D=ra&globalFilter%5BmatchType%5D=any&sort%5B0%5D%5Bkey%5D=first_name&sort%5B0%5D%5Border%5D=ASC&dependents%5Bmanager%5D%5Buid%5D="+uid,

                    GetDetailsRequest(
                        "" + baseUrl + "?page=1&rows=10&globalFilter%5BfieldName%5D=globalFilter&globalFilter%5Bkey%5D=globalFilter&globalFilter%5Bvalue%5D=ra&globalFilter%5BmatchType%5D=any&sort%5B0%5D%5Bkey%5D=first_name&sort%5B0%5D%5Border%5D=ASC&dependents%5Bmanager%5D%5Buid%5D=" + uid,

                        "GET", "The", "", ""
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
                            val responseNewTicketlistNewTicketHistoryResponse = Gson().fromJson(
                                BackShlash.removeSubString(res), GetManagersModel::class.java
                            )
                            if (responseNewTicketlistNewTicketHistoryResponse.success) {
                                getManagersLiveData.value =
                                    responseNewTicketlistNewTicketHistoryResponse.data
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    responseNewTicketlistNewTicketHistoryResponse.message.toString()
                                )
                            }

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

    fun actionTicketResolveClose(
        request: TicketResolveCloseModel?,
        historyCallback: HistoryCallback,
        complaintsListDetailsCallback: ComplaintsListDetailsCallback,
    ) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("ticket_status_update_by_emp_id")) {
                baseUrl = data.APIS[i].URL
                break
            }
        }


        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }

        }


//        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/ticket-status-update-by-emp-id"
        val requestNewComplaintRegistrationJson = Gson().toJson(request)
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val responseNewTicketlistNewTicketHistoryResponse = Gson().fromJson(
                                BackShlash.removeSubString(res),
                                InventoryAcceptRejectResponse::class.java
                            )
                            if (responseNewTicketlistNewTicketHistoryResponse.success) {
                                historyCallback.onSuccessBack(complaintsListDetailsCallback)
//                                command.value = CmsCommand.RefreshPageOnSuccess(
//                                    ""
//                                )
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    responseNewTicketlistNewTicketHistoryResponse.data.errors[0].msg
                                )
                            }

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

    var cmsticketRatingresponse = MutableLiveData<ResponseticketRatingApi>()
    fun getTicketRatingApi() {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS FEEDBACKRATING")) {
                var baseUrl = data.APIS[i].URL
                viewModelScope.launch {
                    state.value = State.SUCCESS
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getDetails(
                            baseUrL, token, GetDetailsRequest(baseUrl, "GET", "the", "", "")
                        )
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            state.value = State.ERROR
                            val resp: String = response.value.string()
                            val res = BackShlash.removeBackSlashes(resp)
                            val responseticketRatingApi = Gson().fromJson(
                                BackShlash.removeSubString(res), ResponseticketRatingApi::class.java
                            )
                            cmsticketRatingresponse.value = responseticketRatingApi
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

    fun subWorkflowAcceptApiCAll(
        subWorkflowAcceptRequest: SubWorkflowAcceptRequest,
        mCallback: HistoryCallback,
    ) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)

        var proxyBaseUrl = ""
        var proxyToken = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                proxyBaseUrl = data.APIS[i].URL
                proxyToken = data.APIS[i].TOKEN
                break
            }
        }
        val subWorkflowAcceptRequestJson = Gson().toJson(subWorkflowAcceptRequest)

        val subWorkflowAcceptUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/subworkflow-update"
//            "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket/save-update/subworkflow-update"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {

                RegistrationRepo.getDetails(
                    proxyBaseUrl, proxyToken, GetDetailsRequest(
                        subWorkflowAcceptUrl, "POST", subWorkflowAcceptRequestJson, "", ""
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
                            val subWorkflowAcceptResponse = Gson().fromJson(
                                BackShlash.removeSubString(res),
                                SubWorkflowAcceptResponse::class.java
                            )
                            if (subWorkflowAcceptResponse!!.success == true) {
                                mCallback.onSuccessSubWorkflowAcceptApiCall()
                            } else {
                                command.value =
                                    CmsCommand.ShowToast(subWorkflowAcceptResponse.message.toString())
                            }

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

    fun subWorkflowRejectApiCall(
        subWorkflowRejectRequest: SubWorkflowRejectRequest,
        mCallback: HistoryCallback,
    ) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)

        var proxyBaseUrl = ""
        var proxyToken = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                proxyBaseUrl = data.APIS[i].URL
                proxyToken = data.APIS[i].TOKEN
                break
            }
        }
        val subWorkflowRejectRequestJson = Gson().toJson(subWorkflowRejectRequest)

        val subWorkflowRejectUrl =
//            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/subworkflow-reject"
            "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket/save-update/subworkflow-reject"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {

                RegistrationRepo.getDetails(
                    proxyBaseUrl, proxyToken, GetDetailsRequest(
                        subWorkflowRejectUrl, "POST", subWorkflowRejectRequestJson, "", ""
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
                            val subWorkflowRejectResponse = Gson().fromJson(
                                BackShlash.removeSubString(res),
                                SubWorkflowRejectResponse::class.java
                            )
                            if (subWorkflowRejectResponse!!.success == true) {
                                mCallback.onSuccessSubWorkflowRejectApiCall()
                            } else {
                                command.value =
                                    CmsCommand.ShowToast(subWorkflowRejectResponse.message.toString())
                            }

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

    fun getSubworkFlowConfigDetails(
        mCallback: HistoryCallback,
        position: Int,
        row: ResponseNewTicketlist.Row,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var proxyBaseUrl = ""
        var proxyToken = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                proxyBaseUrl = data.APIS[i].URL
                proxyToken = data.APIS[i].TOKEN
                break
            }
        }
        var baseUrl = ""
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("")) {
//                baseUrl =
//                    "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket_touch_point/list/?"
////                    data.APIS[i].URL
//                //val token = data.APIS[i].TOKEN
//                break
//            }
//
//        }
        baseUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket_subworkflow_config/list/subworkflow-config-details?"
        // "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket_subworkflow_config/list/subworkflow-config-details?"

        var queryPath = "${
            URLEncoder.encode(
                "department[uid]", "utf-8"
            )
        }=${row.ticketSubworkflowInfo!!.subworkflow_dept!!.uid}&&${
            URLEncoder.encode(
                "role[uid]", "utf-8"
            )
        }=${row.ticketSubworkflowInfo!!.subworkflow_role!!.uid}&&${
            URLEncoder.encode(
                "reason[uid]", "utf-8"
            )
        }=${row.reason!!.uid}&&${
            URLEncoder.encode(
                "step_order", "utf-8"
            )
        }=${row.ticketSubworkflowInfo!!.subworkflow_step_order}&&ticket_id=${row.ticket_id}&&employee_id=${Preferences.getValidatedEmpId()}"//SE35674...RH75774748
        baseUrl = "$baseUrl$queryPath"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {

                RegistrationRepo.getDetails(
                    proxyBaseUrl, proxyToken, GetDetailsRequest(
                        baseUrl, "GET", "The", "", ""
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
                            val responseSubworkflowConfigDetailsResponse = Gson().fromJson(
                                BackShlash.removeSubString(res),
                                SubworkflowConfigDetailsResponse::class.java
                            )
                            if (responseSubworkflowConfigDetailsResponse!!.success!!) {
                                mCallback.onSuccessSubworkflowConfigDetailsApi(
                                    responseSubworkflowConfigDetailsResponse, position
                                )

                            } else {
                                mCallback.onFailureSubworkflowConfigDetailsApi(
                                    responseSubworkflowConfigDetailsResponse!!.message!!
                                )
                            }

                        }
                    } else {
                        Utlis.hideLoading()
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

    fun actionChangeForwardToManager(
        request: ChangeManagerRequest?,
        actionRequest: ChangeManagerRequest?,
        itemPos: Int,
        complaintsListDetailsCallback: ComplaintsListDetailsCallback,

        ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS change_site_manager_in_ticket")) {
                baseUrl = data.APIS[i].URL
                break
            }
        }

        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }


        }


//        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/change-site-manager-in-ticket"
        val requestNewComplaintRegistrationJson = Gson().toJson(request)
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val response = Gson().fromJson(
                                BackShlash.removeSubString(res), ChangeManagerResponse::class.java
                            )
                            if (response.success) {
                                actionInventoryItemsRequestSave(
                                    actionRequest,
                                    itemPos,
                                    complaintsListDetailsCallback
                                )
                            } else {
                                command.value = CmsCommand.ShowToast(response.message.toString())
                            }

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

    fun actionInventoryItemsRequestSave(
        request: ChangeManagerRequest?,
        itemPos: Int,
        complaintsListDetailsCallback: ComplaintsListDetailsCallback,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS inventory_items_request_save")) {
                baseUrl = data.APIS[i].URL
                break
            }
        }


        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }

        }


//        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket_inventory_item/save-update/inventory-items-request-save"
        val requestNewComplaintRegistrationJson = Gson().toJson(request)
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val response = Gson().fromJson(
                                BackShlash.removeSubString(res), ChangeManagerResponse::class.java
                            )
                            if (response.success) {
                                complaintsListDetailsCallback.onClickBack()
//                                command.value = CmsCommand.RefreshPageOnSuccess(
//                                    response.message
//                                )

                                command.value = CmsCommand.ShowToast(response.message.toString())
                            } else {
                                command.value = CmsCommand.ShowToast(response.message.toString())
                            }

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

    fun userlistForSubworkflowApiCall(
        historyCallback: HistoryCallback, ticketData: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int, row: SubworkflowConfigDetailsResponse.Rows,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
//            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/user/list/user-list-for-subworkflow"
        // "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/user/list/user-list-for-subworkflow"
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS USERS LIST FOR SUBWORKFLOW")) {
                baseUrl = data.APIS[i].URL
//                token = data.APIS[i].TOKEN
                break
            }
        }
        baseUrl = "${baseUrl}?" + "${
            URLEncoder.encode("dependents[reason]", "utf-8")
        }=${responseList.get(position)!!.reason!!.uid}" + "&&${
            URLEncoder.encode("dependents[action]", "utf-8")
        }=${row!!.action!!.uid}" + "&&${
            URLEncoder.encode("dependents[region]", "utf-8")
        }=${responseList.get(position)!!.region!!.uid}" + "&&${
            URLEncoder.encode("dependents[cluster]", "utf-8")
        }=${responseList.get(position)!!.cluster!!.uid}" + "&&${
            URLEncoder.encode("dependents[site_type]", "utf-8")
        }=${responseList.get(position)!!.site!!.site_type!!.uid}" + "&&${
            URLEncoder.encode("dependents[employee_id]", "utf-8")
        }=${Preferences.getValidatedEmpId()}"//SE35674  RH75774748
        /* + "&&${
             URLEncoder.encode(
                 "dependents[ch_role]",
                 "utf-8"
             )
         }=${}"*/

        var proxyBaseUrL = ""
        var proxyToken = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                proxyBaseUrL = data.APIS[i].URL
                proxyToken = data.APIS[i].TOKEN
                break
            }
        }

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    proxyBaseUrL,
                    proxyToken,
                    GetDetailsRequest(baseUrl, "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val request = Gson().fromJson(
                                BackShlash.removeSubString(res),
                                UserListForSubworkflowResponse::class.java
                            )
                            if (request.success!!) {
                                historyCallback.onSuccessUsersListforSubworkflow(
                                    ticketData,
                                    responseList,
                                    position,
                                    row,
                                    request, historyCallback
                                )
                            } else {
                                command.value = CmsCommand.ShowToast(request.message.toString())
                            }

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

    fun actionUpdateApiCall(
        historyCallback: HistoryCallback,
        ticketSubworkflowActionUpdateRequest: TicketSubworkflowActionUpdateRequest,
        row: SubworkflowConfigDetailsResponse.Rows,
        remark: String,
        data1: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/ticket-subworkflow-action-update"
        // "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket/save-update/ticket-subworkflow-action-update"
        var token1 = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("")) {
//                baseUrl = data.APIS[i].URL
//                token1 = data.APIS[i].TOKEN
                break
            }
        }
//        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var proxyBaseUrL = ""
        var proxyToken = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                proxyBaseUrL = data.APIS[i].URL
                proxyToken = data.APIS[i].TOKEN
                break
            }
        }
//        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/pos-ticket-accept-reject-update"
        val requestNewComplaintRegistrationJson =
            Gson().toJson(ticketSubworkflowActionUpdateRequest)
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    proxyBaseUrL,
                    proxyToken,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val request = Gson().fromJson(
                                BackShlash.removeSubString(res),
                                TicketSubworkflowActionUpdateResponse::class.java
                            )
                            if (request.success!!) {
                                historyCallback.onSuccessActionUpdate(
                                    request,
                                    row,
                                    remark,
                                    data1,
                                    responseList,
                                    position,
                                    historyCallback
                                )
                            } else {
                                command.value = CmsCommand.ShowToast(request.message.toString())
                            }

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

    fun subworkflowAssignedtoMeApiCall(
        historyCallback: HistoryCallback,
        subworkFowAssignedtoMeRequest: SubworkFlowAssignedtoMeRequest,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/subworkflow-assign-to-me-update"
            //"https://apis.v35.apollodev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket/save-update/subworkflow-assign-to-me-update"
        var token1 = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("")) {
//                baseUrl = data.APIS[i].URL
//                token1 = data.APIS[i].TOKEN
                break
            }
        }
        var proxyBaseUrL = ""
        var proxyToken = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                proxyBaseUrL = data.APIS[i].URL
                proxyToken = data.APIS[i].TOKEN
                break
            }
        }
        val subworkFowAssignedtoMeRequestJson =
            Gson().toJson(subworkFowAssignedtoMeRequest)
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    proxyBaseUrL,
                    proxyToken,
                    GetDetailsRequest(baseUrl, "POST", subworkFowAssignedtoMeRequestJson, "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val request = Gson().fromJson(
                                BackShlash.removeSubString(res),
                                SubworkFlowAssignedtoMeResponse::class.java
                            )
                            if (request.success!!) {
                                if (request.success!!) {
                                    historyCallback.onSuccessSubworkflowAssignedtoMeApiCall(request!!)
                                } else {
                                    command.value = CmsCommand.ShowToast(request.message.toString())
                                }
                            } else {
                                command.value = CmsCommand.ShowToast(request.message.toString())
                            }

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
}