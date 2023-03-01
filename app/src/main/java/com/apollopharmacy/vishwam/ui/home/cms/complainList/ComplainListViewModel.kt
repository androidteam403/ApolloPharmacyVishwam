package com.apollopharmacy.vishwam.ui.home.cms.complainList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.cms.*
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.*
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Data
import com.apollopharmacy.vishwam.ui.home.cms.registration.CmsCommand
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class ComplainListViewModel : ViewModel() {
    var cmsTicketResponse = MutableLiveData<CmsTicketResponse>()

    var complainLiveData = MutableLiveData<ArrayList<CmsResposeV2.DataItem>>()
    var newcomplainLiveData = MutableLiveData<ArrayList<ResponseNewTicketlist.Row>>()
    var newtickethistoryLiveData = MutableLiveData<ResponseNewTicketlist.NewTicketHistoryResponse>()
    var getManagersLiveData = MutableLiveData<Data>()
    var inventoryDetailsLiveData = MutableLiveData<InventoryDetailsModel>()
    var ticketDetailsResponseLiveData = MutableLiveData<TicketDetailsResponse>()
    var creditCardDetailsLiveData = MutableLiveData<CreditCardTSDetails>()
    var command = LiveEvent<CmsCommand>()
    val state = MutableLiveData<State>()
    val TAG = "ComplainListViewModel"
    var resLiveData = MutableLiveData<ResponseNewTicketlist>()
    var tickethistory = ArrayList<ResponseNewTicketlist.NewTicketHistoryResponse.Row>()
    var cmsTicketResponseList = MutableLiveData<CmsTicketResponse>()

    lateinit var Ticketlistdata: ResponseNewTicketlist
    var baseUrL = ""
    var token = ""
    val url = Preferences.getApi()


    //get ticket list api........................................................
    fun getNewticketlist(
        requestComplainList: RequestComplainList,
        status: String,
        isDrugList: Boolean,
        isSearch: Boolean,
        searchQuary: String,
        isApprovalList: Boolean,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        if (isApprovalList) {
            var empDetailsResponse = Preferences.getEmployeeDetailsResponseJson()
            var employeeDetailsResponse: EmployeeDetailsResponse? = null
            try {
                val gson = GsonBuilder().setPrettyPrinting().create()
                employeeDetailsResponse = gson.fromJson<EmployeeDetailsResponse>(empDetailsResponse,
                    EmployeeDetailsResponse::class.java)

            } catch (e: JsonParseException) {
                e.printStackTrace()
            }
            baseUrl =
//                "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket/list/ticket-subwrkflw-pending-approval-list?emp_role=498DA96C612D21956508945D24896C6D&emp_dept=64D9D9BE4A621E9C13A2C73404646655"

                "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket/list/ticket-subwrkflw-pending-approval-list?emp_role=${employeeDetailsResponse!!.data!!.role!!.uid}&emp_dept=${employeeDetailsResponse!!.data!!.department!!.uid}"// dept - 64D9D9BE4A621E9C13A2C73404646655  role - 498DA96C612D21956508945D24896C6D

        } else {
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("CMS mobile_ticket_list_by_emp_id")) {
                    baseUrl = data.APIS[i].URL
//                token = data.APIS[i].TOKEN
                    break
                }
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
//        var baseUrl = "";
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("CMS TICKETLIST")) {
//                baseUrl = data.APIS[i].URL
//                // "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/reason/list/reason-list?page=1&rows=100"
//                //val token = data.APIS[i].TOKEN
//                break
//
//            }
//        }
        val new = if (status.contains("new")) "new" else ""
        val inprogress = if (status.contains("inprogress")) "inprogress" else ""
        val solved = if (status.contains("solved")) "solved" else ""
        val rejected = if (status.contains("rejected")) "rejected" else ""
        val reopened = if (status.contains("reopened")) "reopened" else ""
        val closed = if (status.contains("closed")) "closed" else ""
        val onHold = if (status.contains("onHold")) "onHold" else ""
        if (!isApprovalList) {
            baseUrl =
                baseUrl + "employee_id=${requestComplainList.empid}&from_date=${requestComplainList.fromDate}&to_date=${requestComplainList.toDate}&page=${requestComplainList.page}&rows=10&" + if (isDrugList) {
                    "reason_code=new_drug&"
                } else {
                    ""
                } + if (isSearch) {
                    "site_ticket=$searchQuary&"
                } else {
                    "site_ticket=$searchQuary&"
                } + "${
                    URLEncoder.encode("status[0]", "utf-8")
                }=${new}&${
                    URLEncoder.encode("status[1]", "utf-8")
                }=${inprogress}&${
                    URLEncoder.encode("status[2]", "utf-8")
                }=${solved}&${
                    URLEncoder.encode("status[3]", "utf-8")
                }=${rejected}&${
                    URLEncoder.encode("status[4]", "utf-8")
                }=${reopened}&${
                    URLEncoder.encode("status[5]", "utf-8")
                }=${closed}&${
                    URLEncoder.encode("status[6]", "utf-8")
                }=${onHold}"
        }
//"https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/list/mobile-ticket-list-by-emp-id?&employee_id=${requestComplainList.empid}&status=${status}&from_date=${requestComplainList.fromDate}&to_date=${requestComplainList.toDate}&page=${requestComplainList.page}&rows=10"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "GET", "The", "", ""))
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val responseNewTicketlist =
                                Gson().fromJson(BackShlash.removeSubString(res),
                                    ResponseNewTicketlist::class.java)
                            if (responseNewTicketlist.success) {
                                resLiveData.value = responseNewTicketlist
//                                        newcomplainLiveData.value =
//                                            responseNewTicketlist.data.listData.rows
                            } else {
                                command.value =
                                    CmsCommand.VisibleLayout(responseNewTicketlist.message.toString())
                            }
                        }
                        //  unComment it  newcomplainLiveData.value = response.value.data.listData.rows
                        //  Ticketlistdata = response.value
                        //  val reasonlitrows = response.value.data.listData.rows
                        // for (row in reasonlitrows) {
                        //  deartmentlist.add(row.department)
                        // }
                    } else {
                        //  unComment it   command.value = CmsCommand.ShowToast(response.value.message.toString())
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

    fun setTicketListFromSheredPeff() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        resLiveData.value =
            gson.fromJson<ResponseNewTicketlist>(Preferences.getResponseNewTicketlist(),
                ResponseNewTicketlist::class.java)
    }

    //get new ticket history...........................................
    fun getNewticketHistory(requestTicketHistory: RequestTicketHistory) {
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
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS TICKETTRACKING")) {
                baseUrl =
//                    "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket_touch_point/list/?"
                data.APIS[i].URL
                //val token = data.APIS[i].TOKEN
                break
            }

        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {

                RegistrationRepo.getDetails(proxyBaseUrl,
                    proxyToken,
                    GetDetailsRequest(baseUrl+"?" + "&page=" + requestTicketHistory.page + "&rows=" + requestTicketHistory.rows + "&ticket_uid=" + requestTicketHistory.dependents,
                        "GET",
                        "The",
                        "",
                        ""))


//                        RegistrationRepo.getticketHistory(
//                            baseUrl,
//                            requestTicketHistory.page,
//                            requestTicketHistory.rows,
//                            requestTicketHistory.dependents
//                        )
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
                                ResponseNewTicketlist.NewTicketHistoryResponse::class.java)
                            if (responseNewTicketlistNewTicketHistoryResponse.success) {
                                newtickethistoryLiveData.value =
                                    responseNewTicketlistNewTicketHistoryResponse

                                tickethistory =
                                    responseNewTicketlistNewTicketHistoryResponse.data.listData.rows

                            } else {
                                command.value = CmsCommand.VisibleLayout(
                                    responseNewTicketlistNewTicketHistoryResponse.message.toString())
                            }

                        }
                        //  Ticketlistdata = response.value
                        //  val reasonlitrows = response.value.data.listData.rows
                        // for (row in reasonlitrows) {
                        //  deartmentlist.add(row.department)
                        // }
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

    fun cmsTicketStatusUpdate(cmsTicketRequest: CmsTicketRequest) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket_it/save-update/it-cc-frwd-to-fin-status-update"
        var token1 = ""
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("CMS pos_ticket_accept_reject_update")) {
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
                RegistrationRepo.getDetails(baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestcmsTicketRequestJson, "", ""))
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val cmsTicketResponse = Gson().fromJson(BackShlash.removeSubString(res),
                                CmsTicketResponse::class.java)

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

    fun getInventoryAdditionalDetails(requestTicketHistory: String?, itemPos: Int) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token1 = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS inventory_details_for_mobile")) {
                baseUrL = data.APIS[i].URL
                token1 = data.APIS[i].TOKEN
                break
            }
        }
        val baseUrl = "${baseUrL}ticket_id=${requestTicketHistory}"//data.APIS[i].URL

//        CMS inventory_details_for_mobile
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("CMS TICKETTRACKING")) {
//                val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/select/inventory-details-for-mobile?ticket_id="+requestTicketHistory//data.APIS[i].URL
        //val token = data.APIS[i].TOKEN
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

                RegistrationRepo.getDetails(baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "GET", "The", "", ""))
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
                                InventoryDetailsModel::class.java)
                            if (responseNewTicketlistNewTicketHistoryResponse.success) {
                                responseNewTicketlistNewTicketHistoryResponse.position = itemPos
                                inventoryDetailsLiveData.value =
                                    responseNewTicketlistNewTicketHistoryResponse
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    responseNewTicketlistNewTicketHistoryResponse.message.toString())
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
//            }
//        }


    fun getCreditCardTSAdditionalDetails(requestTicketHistory: String?, itemPos: Int) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token1 = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS cc_tx_details_for_mobile")) {
                baseUrl = data.APIS[i].URL
                token1 = data.APIS[i].TOKEN
                break
            }
        }
        var proxyUrl = ""
        var proxyToken = ""
//        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                proxyUrl = data.APIS[i].URL
                proxyToken = data.APIS[i].TOKEN
                break
            }
        }

        val baseUrL = "${baseUrl}ticket_id=${requestTicketHistory}"
//        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/select/cc-tx-details-for-mobile?ticket_id="+requestTicketHistory
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(proxyUrl,
                    proxyToken,
                    GetDetailsRequest(baseUrL, "GET", "The", "", ""))
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
                                CreditCardTSDetails::class.java)
                            if (responseNewTicketlistNewTicketHistoryResponse.success) {
                                responseNewTicketlistNewTicketHistoryResponse.position = itemPos
                                creditCardDetailsLiveData.value =
                                    responseNewTicketlistNewTicketHistoryResponse
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    responseNewTicketlistNewTicketHistoryResponse.message.toString())
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

    fun actionCCAccept(request: CCAcceptRejectModel?, itemPos: Int) {
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
                RegistrationRepo.getDetails(baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", ""))
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val request = Gson().fromJson(BackShlash.removeSubString(res),
                                CCAcceptRejectResponse::class.java)
                            if (request.success) {
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
                RegistrationRepo.getDetails(baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", ""))
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
                                InventoryAcceptRejectResponse::class.java)
                            if (responseNewTicketlistNewTicketHistoryResponse.success) {
//                                command.value = CmsCommand.RefreshPageOnSuccess(
//                                    responseNewTicketlistNewTicketHistoryResponse.data.uid
//                                )
//                                command.value = CmsCommand.ShowToast(
//                                    responseNewTicketlistNewTicketHistoryResponse.data.uid.toString()
//                                )
//                                actionWorkflowUpdate(workFlowUpdateModel,itemPos)
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    responseNewTicketlistNewTicketHistoryResponse.data.res.toString())
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


    fun actionWorkflowUpdate(request: WorkFlowUpdateModel?, itemPos: Int) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("ticket_actions_workflow_update")) {
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
//        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/ticket-actions-workflow-update"
        val requestNewComplaintRegistrationJson = Gson().toJson(request)
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", ""))
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val response = Gson().fromJson(BackShlash.removeSubString(res),
                                ChangeManagerResponse::class.java)
                            if (response.success) {
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

    fun actionForwardToManager(request: ForwardToManagerModel?, itemPos: Int) {
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
                RegistrationRepo.getDetails(baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", ""))
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val response = Gson().fromJson(BackShlash.removeSubString(res),
                                InventoryAcceptRejectResponse::class.java)
                            if (response.success) {
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

    fun getComplainListOrder(requestComplainList: RequestComplainList) {
        state.value = State.SUCCESS
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS APP")) {
                val baseUrl = data.APIS[i].URL
                val token = data.APIS[i].TOKEN
                viewModelScope.launch() {
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getListOfComplain(token,
                            baseUrl,
                            Config.CMS_Registered_Cmp_List,
                            requestComplainList)
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            Utils.printMessage(TAG,
                                "Complaint List :: " + response.value.toString())
                            complainLiveData.value = response.value.data
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            command.value = CmsCommand.ShowToast("Please Check Internet Connection")
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            command.value = CmsCommand.ShowToast("Unknown Error")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            command.value =
                                CmsCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            state.value = State.ERROR
                            command.value =
                                CmsCommand.ShowToast("Something went wrong, please try again later")
                        }
                    }
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
                RegistrationRepo.getDetails(baseUrL, token,
//                            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/user/list/store-manager-list-for-change-manager?page=1&rows=10&globalFilter%5BfieldName%5D=globalFilter&globalFilter%5Bkey%5D=globalFilter&globalFilter%5Bvalue%5D=ra&globalFilter%5BmatchType%5D=any&sort%5B0%5D%5Bkey%5D=first_name&sort%5B0%5D%5Border%5D=ASC&dependents%5Bmanager%5D%5Buid%5D="+uid,

                    GetDetailsRequest("" + baseUrl + "?page=1&rows=10&globalFilter%5BfieldName%5D=globalFilter&globalFilter%5Bkey%5D=globalFilter&globalFilter%5Bvalue%5D=ra&globalFilter%5BmatchType%5D=any&sort%5B0%5D%5Bkey%5D=first_name&sort%5B0%5D%5Border%5D=ASC&dependents%5Bmanager%5D%5Buid%5D=" + uid,

                        "GET", "The", "", ""))
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
                                GetManagersModel::class.java)
                            if (responseNewTicketlistNewTicketHistoryResponse.success) {
                                getManagersLiveData.value =
                                    responseNewTicketlistNewTicketHistoryResponse.data
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    responseNewTicketlistNewTicketHistoryResponse.message.toString())
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

    fun actionChangeForwardToManager(
        request: ChangeManagerRequest?,
        actionRequest: ChangeManagerRequest?,
        itemPos: Int,
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
                RegistrationRepo.getDetails(baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", ""))
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val response = Gson().fromJson(BackShlash.removeSubString(res),
                                ChangeManagerResponse::class.java)
                            if (response.success) {
                                actionInventoryItemsRequestSave(actionRequest, itemPos)
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

    fun actionInventoryItemsRequestSave(request: ChangeManagerRequest?, itemPos: Int) {
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
                RegistrationRepo.getDetails(baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", ""))
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val response = Gson().fromJson(BackShlash.removeSubString(res),
                                ChangeManagerResponse::class.java)
                            if (response.success) {
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

    fun actionTicketResolveClose(
        request: TicketResolveCloseModel?,
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
                RegistrationRepo.getDetails(baseUrL,
                    token,
                    GetDetailsRequest(baseUrl, "POST", requestNewComplaintRegistrationJson, "", ""))
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
                                InventoryAcceptRejectResponse::class.java)
                            if (responseNewTicketlistNewTicketHistoryResponse.success) {
//                                command.value = CmsCommand.RefreshPageOnSuccess(
//                                    ""
//                                )
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    responseNewTicketlistNewTicketHistoryResponse.data.errors[0].msg)
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
                        RegistrationRepo.getDetails(baseUrL,
                            token,
                            GetDetailsRequest(baseUrl, "GET", "the", "", ""))
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            state.value = State.ERROR
                            val resp: String = response.value.string()
                            val res = BackShlash.removeBackSlashes(resp)
                            val responseticketRatingApi =
                                Gson().fromJson(BackShlash.removeSubString(res),
                                    ResponseticketRatingApi::class.java)
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


    fun getTicketFullDetails(requestTicketHistory: String?, itemPos: Int) {

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
        var baseUrL = ""
//        if (Config.KEY.equals("2034")){
//            baseUrL = "https://cms.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/select/mobile-ticket-details?"
//        }else{
//            baseUrL = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/select/mobile-ticket-details?"
//        }


        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS MBTICKETDTS")) {
                baseUrL =
//                    "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket/select/mobile-ticket-details?"
                data.APIS[i].URL
                //val token = data.APIS[i].TOKEN
                break
            }

        }
        val baseUrl = "${baseUrL}ticket_id=${requestTicketHistory}"//data.APIS[i].URL
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {

                RegistrationRepo.getDetails(proxyBaseUrl,
                    proxyToken,
                    GetDetailsRequest(baseUrl, "GET", "The", "", ""))
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
                                TicketDetailsResponse::class.java)
                            if (responseNewTicketlistNewTicketHistoryResponse.success) {
                                responseNewTicketlistNewTicketHistoryResponse.position = itemPos
                                ticketDetailsResponseLiveData.value =
                                    responseNewTicketlistNewTicketHistoryResponse
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    responseNewTicketlistNewTicketHistoryResponse.message.toString())
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

    fun subWorkflowAcceptApiCAll(
        subWorkflowAcceptRequest: SubWorkflowAcceptRequest,
        mCallback: ComplaintListFragmentCallback,
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
            "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket/save-update/subworkflow-update"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {

                RegistrationRepo.getDetails(proxyBaseUrl,
                    proxyToken,
                    GetDetailsRequest(subWorkflowAcceptUrl,
                        "POST",
                        subWorkflowAcceptRequestJson,
                        "",
                        ""))
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val subWorkflowAcceptResponse =
                                Gson().fromJson(BackShlash.removeSubString(res),
                                    SubWorkflowAcceptResponse::class.java)
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
        mCallback: ComplaintListFragmentCallback,
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
            "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket/save-update/subworkflow-reject"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {

                RegistrationRepo.getDetails(proxyBaseUrl,
                    proxyToken,
                    GetDetailsRequest(subWorkflowRejectUrl,
                        "POST",
                        subWorkflowRejectRequestJson,
                        "",
                        ""))
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val subWorkflowRejectResponse =
                                Gson().fromJson(BackShlash.removeSubString(res),
                                    SubWorkflowRejectResponse::class.java)
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
}
