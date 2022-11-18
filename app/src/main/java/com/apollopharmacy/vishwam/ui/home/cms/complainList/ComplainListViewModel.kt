package com.apollopharmacy.vishwam.ui.home.cms.complainList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
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
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.util.*

class ComplainListViewModel : ViewModel() {

    var complainLiveData = MutableLiveData<ArrayList<CmsResposeV2.DataItem>>()
    var newcomplainLiveData = MutableLiveData<ArrayList<ResponseNewTicketlist.Row>>()
    var newtickethistoryLiveData = MutableLiveData<ResponseNewTicketlist.NewTicketHistoryResponse>()
    var getManagersLiveData = MutableLiveData<Data>()
    var inventoryDetailsLiveData = MutableLiveData<InventoryDetailsModel>()
    var creditCardDetailsLiveData = MutableLiveData<CreditCardTSDetails>()
    var command = LiveEvent<CmsCommand>()
    val state = MutableLiveData<State>()
    val TAG = "ComplainListViewModel"
    var resLiveData = MutableLiveData<ResponseNewTicketlist>()
    var tickethistory = ArrayList<ResponseNewTicketlist.NewTicketHistoryResponse.Row>()

    lateinit var Ticketlistdata: ResponseNewTicketlist

    //get ticket list api........................................................
    fun getNewticketlist(requestComplainList: RequestComplainList, status: String,isDrugList : Boolean) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS TICKETLIST")) {
                val baseUrl = data.APIS[i].URL
                // "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/reason/list/reason-list?page=1&rows=100"
                //val token = data.APIS[i].TOKEN

                val new = if (status.contains("new")) "new" else ""
                val inprogress = if (status.contains("inprogress")) "inprogress" else ""
                val solved = if (status.contains("solved")) "solved" else ""
                val rejected = if (status.contains("rejected")) "rejected" else ""
                val reopened = if (status.contains("reopened")) "reopened" else ""
                val closed = if (status.contains("closed")) "closed" else ""
                val onHold = if (status.contains("onHold")) "onHold" else ""

                val url: String =
                    "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/list/mobile-ticket-list-by-emp-id?&employee_id=${requestComplainList.empid}&from_date=${requestComplainList.fromDate}&to_date=${requestComplainList.toDate}&page=${requestComplainList.page}&rows=10&"+ if(isDrugList){ "reason_code=new_drug&"} else{""}+"${
                        URLEncoder.encode("status[0]",
                            "utf-8")
                    }=${new}&${
                        URLEncoder.encode("status[1]",
                            "utf-8")
                    }=${inprogress}&${
                        URLEncoder.encode("status[2]",
                            "utf-8")
                    }=${solved}&${
                        URLEncoder.encode("status[3]",
                            "utf-8")
                    }=${rejected}&${
                        URLEncoder.encode("status[4]",
                            "utf-8")
                    }=${reopened}&${
                        URLEncoder.encode("status[5]",
                            "utf-8")
                    }=${closed}&${
                        URLEncoder.encode("status[6]",
                            "utf-8")
                    }=${onHold}"
//"https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/list/mobile-ticket-list-by-emp-id?&employee_id=${requestComplainList.empid}&status=${status}&from_date=${requestComplainList.fromDate}&to_date=${requestComplainList.toDate}&page=${requestComplainList.page}&rows=10"
                viewModelScope.launch {
                    state.value = State.SUCCESS
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getDetails(
                            "h72genrSSNFivOi/cfiX3A==",
                            GetDetailsRequest(
                                url,
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
                            if (response != null) {
                                val resp: String = response.value.string()
                                if (resp != null) {
                                    val res = BackShlash.removeBackSlashes(resp)
                                    val responseNewTicketlist =
                                        Gson().fromJson(
                                            BackShlash.removeSubString(res),
                                            ResponseNewTicketlist::class.java
                                        )
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
        }
    }





    //get new ticket history...........................................
    fun getNewticketHistory(requestTicketHistory: RequestTicketHistory) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS TICKETTRACKING")) {
                val baseUrl = data.APIS[i].URL
                //val token = data.APIS[i].TOKEN

                viewModelScope.launch {
                    state.value = State.SUCCESS
                    val response = withContext(Dispatchers.IO) {

                        RegistrationRepo.getDetails(
                            "h72genrSSNFivOi/cfiX3A==",
                            GetDetailsRequest(
                                baseUrl + "&page=" + requestTicketHistory.page + "&rows=" + requestTicketHistory.rows + "&ticket_uid=" + requestTicketHistory.dependents,
                                "GET",
                                "The",
                                "",
                                ""
                            )
                        )


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
                                    val responseNewTicketlistNewTicketHistoryResponse =
                                        Gson().fromJson(
                                            BackShlash.removeSubString(res),
                                            ResponseNewTicketlist.NewTicketHistoryResponse::class.java
                                        )
                                    if (responseNewTicketlistNewTicketHistoryResponse.success) {
                                        newtickethistoryLiveData.value =
                                            responseNewTicketlistNewTicketHistoryResponse

                                        tickethistory =
                                            responseNewTicketlistNewTicketHistoryResponse.data.listData.rows

                                    } else {
                                        command.value = CmsCommand.VisibleLayout(
                                            responseNewTicketlistNewTicketHistoryResponse.message.toString()
                                        )
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
        }
    }

    fun getInventoryAdditionalDetails(requestTicketHistory: String?, itemPos: Int) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("CMS TICKETTRACKING")) {
                val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/select/inventory-details-for-mobile?ticket_id="+requestTicketHistory//data.APIS[i].URL
                //val token = data.APIS[i].TOKEN

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
                            if (response != null) {
                                val resp: String = response.value.string()
                                if (resp != null) {
                                    val res = BackShlash.removeBackSlashes(resp)
                                    val responseNewTicketlistNewTicketHistoryResponse =
                                        Gson().fromJson(
                                            BackShlash.removeSubString(res),
                                            InventoryDetailsModel::class.java
                                        )
                                    if (responseNewTicketlistNewTicketHistoryResponse.success) {
                                        responseNewTicketlistNewTicketHistoryResponse.position = itemPos
                                        inventoryDetailsLiveData.value =
                                            responseNewTicketlistNewTicketHistoryResponse
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
//            }
//        }
    }

    fun getCreditCardTSAdditionalDetails(requestTicketHistory: String?, itemPos: Int) {
        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/select/cc-tx-details-for-mobile?ticket_id="+requestTicketHistory
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
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val responseNewTicketlistNewTicketHistoryResponse =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    CreditCardTSDetails::class.java
                                )
                            if (responseNewTicketlistNewTicketHistoryResponse.success) {
                                responseNewTicketlistNewTicketHistoryResponse.position = itemPos
                                creditCardDetailsLiveData.value =
                                            responseNewTicketlistNewTicketHistoryResponse
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

    fun actionCCAccept(request: CCAcceptRejectModel?, itemPos: Int) {
        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/pos-ticket-accept-reject-update"
        val requestNewComplaintRegistrationJson =
            Gson().toJson(request)
        viewModelScope.launch {
            state.value = State.SUCCESS
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
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val request =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    CCAcceptRejectResponse::class.java
                                )
                            if (request.success) {
                                command.value = CmsCommand.RefreshPageOnSuccess(
                                    request.message
                                )
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    request.message.toString()
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

    fun actionInventoryAcceptReject(
        request: InventoryAcceptrejectModel?,
        workFlowUpdateModel: WorkFlowUpdateModel,
        itemPos: Int
    ) {
        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket_inventory_item/save-update/inventory-items-approve-reject-update"
        val requestNewComplaintRegistrationJson =
            Gson().toJson(request)
        viewModelScope.launch {
            state.value = State.SUCCESS
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
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val responseNewTicketlistNewTicketHistoryResponse =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    InventoryAcceptRejectResponse::class.java
                                )
                            if (responseNewTicketlistNewTicketHistoryResponse.success) {
                                command.value = CmsCommand.RefreshPageOnSuccess(
                                    responseNewTicketlistNewTicketHistoryResponse.data.uid
                                )
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


    fun actionWorkflowUpdate(request: WorkFlowUpdateModel?, itemPos: Int) {
        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/ticket-actions-workflow-update"
        val requestNewComplaintRegistrationJson =
            Gson().toJson(request)
        viewModelScope.launch {
            state.value = State.SUCCESS
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
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val response =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    ChangeManagerResponse::class.java
                                )
                            if (response.success) {
                                command.value = CmsCommand.RefreshPageOnSuccess(
                                    response.message
                                )
                                command.value = CmsCommand.ShowToast(
                                    response.message.toString()
                                )
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    response.message.toString()
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

    fun actionForwardToManager(request: ForwardToManagerModel?, itemPos: Int) {
        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket_inventory_item/save-update/inventory-items-request-save"
        val requestNewComplaintRegistrationJson =
            Gson().toJson(request)
        viewModelScope.launch {
            state.value = State.SUCCESS
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
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val response =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    InventoryAcceptRejectResponse::class.java
                                )
                            if (response.success) {
                                command.value = CmsCommand.RefreshPageOnSuccess(
                                    response.message
                                )
                                command.value = CmsCommand.ShowToast(
                                    response.message.toString()
                                )
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    response.message.toString()
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
                        RegistrationRepo.getListOfComplain(
                            token,
                            baseUrl,
                            Config.CMS_Registered_Cmp_List,
                            requestComplainList
                        )
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            Utils.printMessage(
                                TAG,
                                "Complaint List :: " + response.value.toString()
                            )
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

                viewModelScope.launch {
                    state.value = State.SUCCESS
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getDetails(
                            "h72genrSSNFivOi/cfiX3A==",
                            GetDetailsRequest(
                                "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/user/list/store-manager-list-for-change-manager?page=1&rows=10&globalFilter%5BfieldName%5D=globalFilter&globalFilter%5Bkey%5D=globalFilter&globalFilter%5Bvalue%5D=ra&globalFilter%5BmatchType%5D=any&sort%5B0%5D%5Bkey%5D=first_name&sort%5B0%5D%5Border%5D=ASC&dependents%5Bmanager%5D%5Buid%5D="+uid,
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
                            if (response != null) {
                                val resp: String = response.value.string()
                                if (resp != null) {
                                    val res = BackShlash.removeBackSlashes(resp)
                                    val responseNewTicketlistNewTicketHistoryResponse =
                                        Gson().fromJson(
                                            BackShlash.removeSubString(res),
                                            GetManagersModel::class.java
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

    fun actionChangeForwardToManager(request: ChangeManagerRequest?,actionRequest: ChangeManagerRequest?, itemPos: Int) {
        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/change-site-manager-in-ticket"
        val requestNewComplaintRegistrationJson =
            Gson().toJson(request)
        viewModelScope.launch {
            state.value = State.SUCCESS
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
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val response =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    ChangeManagerResponse::class.java
                                )
                            if (response.success) {
                                actionInventoryItemsRequestSave(actionRequest,itemPos)
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    response.message.toString()
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

    fun actionInventoryItemsRequestSave(request: ChangeManagerRequest?, itemPos: Int) {
        val baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket_inventory_item/save-update/inventory-items-request-save"
        val requestNewComplaintRegistrationJson =
            Gson().toJson(request)
        viewModelScope.launch {
            state.value = State.SUCCESS
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
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val response =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    ChangeManagerResponse::class.java
                                )
                            if (response.success) {
                                command.value = CmsCommand.RefreshPageOnSuccess(
                                    response.message
                                )
                                command.value = CmsCommand.ShowToast(
                                    response.message.toString()
                                )
                            } else {
                                command.value = CmsCommand.ShowToast(
                                    response.message.toString()
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
}