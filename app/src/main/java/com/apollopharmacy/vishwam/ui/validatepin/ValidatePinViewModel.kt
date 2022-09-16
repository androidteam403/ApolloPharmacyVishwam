package com.apollopharmacy.vishwam.ui.validatepin

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.*
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ValidatePinViewModel : ViewModel() {

    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    var employeeDetails=MutableLiveData<EmployeeDetailsResponse>()
    fun checkMPinLogin(mPinRequest: MPinRequest) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                LoginRepo.checkMPinDetails(mPinRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.Status) {
                        state.value = State.ERROR
//                        commands.postValue(Command.ShowToast("Successfully login"))
//                        Preferences.saveSiteId("")
//                        LoginRepo.saveProfile(result.value)
//                        Preferences.savingToken(result.value.EMPID)
//                        Preferences.storeLoginJson(Gson().toJson(result.value))
                        commands.value = Command.NavigateTo(result.value)
                    } else {
                        state.value = State.ERROR
                        commands.value = Command.ShowToast(result.value.Message)
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        result.error?.let {
                            Command.ShowToast(it)
                        }
                    )
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


    fun getRole(validatedEmpId: String) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("CMS TICKETLIST")) {
//                val baseUrl = data.APIS[i].URL
        // "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/reason/list/reason-list?page=1&rows=100"
        //val token = data.APIS[i].TOKEN
//
//                val new = if (status.contains("new")) "new" else ""
//                val inprogress = if (status.contains("inprogress")) "inprogress" else ""
//                val solved = if (status.contains("solved")) "solved" else ""
//                val rejected = if (status.contains("rejected")) "rejected" else ""
//                val reopened = if (status.contains("reopened")) "reopened" else ""
//                val closed = if (status.contains("closed")) "closed" else ""

        val baseUrl: String =
            "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/user/select/employee-details-mobile?emp_id=${validatedEmpId}"

//"https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/list/mobile-ticket-list-by-emp-id?&employee_id=${requestComplainList.empid}&status=${status}&from_date=${requestComplainList.fromDate}&to_date=${requestComplainList.toDate}&page=${requestComplainList.page}&rows=10"
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
                            val responseNewTicketlist =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    EmployeeDetailsResponse::class.java
                                )
                            if (responseNewTicketlist.success!!) {
                                employeeDetails.value = responseNewTicketlist
                                Preferences.setEmployeeRoleUid(responseNewTicketlist.data?.uploadSwach?.uid!!)
                                Preferences.getEmployeeRoleUid()
//                                        newcomplainLiveData.value =
//                                            responseNewTicketlist.data.listData.rows
                            }
                            else {
//                                commands.value =
//                                 Command.ShowToast(responseNewTicketlist.message.toString())
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
//            }
//        }
    }
}

sealed class Command {
    data class ShowToast(val message: String) : Command()
    data class NavigateTo(
        val value: MPinResponse,
    ) : Command()

    data class ShowButtonSheet(
        val fragment: Class<out BottomSheetDialogFragment>,
        val arguments: Bundle,
    ) :
        Command()
}