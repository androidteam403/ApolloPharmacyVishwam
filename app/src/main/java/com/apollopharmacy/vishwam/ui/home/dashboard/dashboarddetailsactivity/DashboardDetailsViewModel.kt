package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity

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
import com.apollopharmacy.vishwam.ui.home.dashboard.ceodashboard.CeoDashboardViewModel
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.model.ReasonWiseTicketCountbyRoleResponse
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardDetailsViewModel : ViewModel() {
    val commands = LiveEvent<CeoDashboardViewModel.Command>()
    val state = MutableLiveData<State>()
    fun getReasonWiseTicketCountByRole(
        dashboardDetailsCallback: DashboardDetailsCallback,
        fromDate: String,
        toDate: String,
        employeeId: String,
        roleCode: String,
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
            }
        }
        //api/ticket/list/reason-wise-ticket-count-by-role
        //api/ticket/list/reason-wise-ticket-count-role-mobile
        //https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket/list/reason-wise-ticket-count-role-mobile
        var reasonWiseTicketCountByRoleUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/list/reason-wise-ticket-count-role-mobile?"
        reasonWiseTicketCountByRoleUrl += "from_date=$fromDate&to_date=$toDate&employee_id=$employeeId&role_code=$roleCode"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrl,
                    token,
                    GetDetailsRequest(reasonWiseTicketCountByRoleUrl, "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val reasonWiseTicketCountByRoleResponse =
                        Gson().fromJson(
                            BackShlash.removeSubString(res),
                            ReasonWiseTicketCountbyRoleResponse::class.java
                        )
                    if (reasonWiseTicketCountByRoleResponse.success!!) {
                        dashboardDetailsCallback.onSuccessGetReasonWiseTicketCountByRoleApiCall(
                            reasonWiseTicketCountByRoleResponse
                        )
                    } else {
                        dashboardDetailsCallback.onFailureGetReasonWiseTicketCountByRoleApiCall(
                            reasonWiseTicketCountByRoleResponse
                        )
                    }
                }

                is ApiResult.GenericError -> {
                    commands.postValue(response.error?.let {
                        CeoDashboardViewModel.Command.ShowToast(it)
                    })
                    state.value = State.ERROR
                }

                is ApiResult.NetworkError -> {
                    commands.postValue(CeoDashboardViewModel.Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }

                is ApiResult.UnknownError -> {
                    commands.postValue(CeoDashboardViewModel.Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }

                else -> {
                    commands.postValue(CeoDashboardViewModel.Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }
}