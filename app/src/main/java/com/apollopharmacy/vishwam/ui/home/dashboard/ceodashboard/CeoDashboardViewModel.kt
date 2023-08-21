package com.apollopharmacy.vishwam.ui.home.dashboard.ceodashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.dashboard.model.TicketCountsByStatusRoleResponse
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CeoDashboardViewModel : ViewModel() {
    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    fun getTicketListByCountApi(
        ceoDashboardCallback: CeoDashboardCallback,
        startDate: String,
        endDate: String,
        id: String,
        roleCode:String,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }//https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/list/get-ticket-counts-by-status-role?
        }//https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket/list/get-ticket-counts-by-status-role?
        var getTicketListByCountUrl =
            "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket/list/get-ticket-counts-by-status-role?"
        if (roleCode.isNullOrEmpty()){
            getTicketListByCountUrl += "from_date=$startDate&to_date=$endDate&employee_id=$id"

        }else{
            getTicketListByCountUrl += "from_date=$startDate&to_date=$endDate&employee_id=$id&role_code=$roleCode"

        }
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
//                DashboardRepo.getTicketCountByStatus(startDate, endDate, id);
                RegistrationRepo.getDetails(baseUrl,
                    token,
                    GetDetailsRequest(getTicketListByCountUrl, "GET", "The", "", ""))
            }
            when (result) {
                is ApiResult.Success -> {
                    val resp: String = result.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val ticketListByCountResponse = Gson().fromJson(BackShlash.removeSubString(res),
                        TicketCountsByStatusRoleResponse::class.java)
                    if (ticketListByCountResponse.success) {
                        ceoDashboardCallback.onSuccessgetTicketListByCountApi(
                            ticketListByCountResponse)
                    } else {
                        ceoDashboardCallback.onFailuregetTicketListByCountApi(
                            ticketListByCountResponse)
                    }
//                    if (result.value.success!!) {
//                        state.value = State.ERROR
//                        ceoDashboardCallback.onSuccessgetTicketListByCountApi(result.value)
//                        getStoreDetailsChamps.value = result.value
//                    } else {
//                        state.value = State.ERROR
//                        ceoDashboardCallback.onFailuregetTicketListByCountApi(result.value)
//                        commands.value = Command.ShowToast(result.value.message!! as String)
//                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(result.error?.let {
                        Command.ShowToast(it)
                    })
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }

    sealed class Command {
        data class ShowToast(val message: String) : Command()
    }
}