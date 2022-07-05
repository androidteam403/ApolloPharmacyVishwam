package com.apollopharmacy.vishwam.ui.home.cms.complainList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.cms.CmsResposeV2
import com.apollopharmacy.vishwam.data.model.cms.RequestComplainList
import com.apollopharmacy.vishwam.data.model.cms.RequestTicketHistory
import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.cms.registration.CmsCommand
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ComplainListViewModel : ViewModel() {

    var complainLiveData = MutableLiveData<ArrayList<CmsResposeV2.DataItem>>()
    var newcomplainLiveData = MutableLiveData<ArrayList<ResponseNewTicketlist.Row>>()
    var newtickethistoryLiveData = MutableLiveData<ResponseNewTicketlist.NewTicketHistoryResponse>()
    var command = LiveEvent<CmsCommand>()
    val state = MutableLiveData<State>()
    val TAG = "ComplainListViewModel"

    var tickethistory = ArrayList<ResponseNewTicketlist.NewTicketHistoryResponse.Row>()

    lateinit var Ticketlistdata: ResponseNewTicketlist

    //get ticket list api........................................................
    fun getNewticketlist(requestComplainList: RequestComplainList) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS TICKETLIST")) {
                val baseUrl = data.APIS[i].URL
                // "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/reason/list/reason-list?page=1&rows=100"
                //val token = data.APIS[i].TOKEN

                viewModelScope.launch {
                    state.value = State.SUCCESS
                    val response = withContext(Dispatchers.IO) {
                        RegistrationRepo.getDetails(
                            "h72genrSSNFivOi/cfiX3A==",
                            GetDetailsRequest(
                                "$baseUrl&site_id=${requestComplainList.siteid}&from_date=${requestComplainList.fromDate}&to_date=${requestComplainList.toDate}",
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
                                        newcomplainLiveData.value =
                                            responseNewTicketlist.data.listData.rows
                                    } else {
                                        command.value =
                                            CmsCommand.ShowToast(responseNewTicketlist.message.toString())
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
                                        command.value = CmsCommand.ShowToast(
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
}