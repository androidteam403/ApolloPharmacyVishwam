package com.apollopharmacy.vishwam.ui.home.planogram.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.planogram.activity.PlanogramActivityViewModel
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.ListBySiteIdResponse
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlanogramViewModel :ViewModel() {
    val state = MutableLiveData<State>()
    val commands = LiveEvent<Command>()
    fun getList(siteId: String, planogramCallback: PlanogramCallback) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)


        var proxyUrl = ""
        var proxyToken = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                proxyUrl = data.APIS[i].URL
                proxyToken = data.APIS[i].TOKEN
                break
            }
        }

        var baseUrl = "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/planogram/list/planogram-list-by-site-id"
        var token = ""
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("SWND employee-details-mobile")) {
//                baseUrl = data.APIS[i].URL
//                token = data.APIS[i].TOKEN
//                break
//            }
//        }

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(proxyUrl,proxyToken,
                    GetDetailsRequest(baseUrl+"?site_id=${siteId}", "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val siteIdResponse =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    ListBySiteIdResponse::class.java)
                            if (siteIdResponse.success!!) {
                                planogramCallback.onSuccessPlanogramSiteIdList(
                                    siteIdResponse
                                )
                            } else {
                                planogramCallback.onFailurePlanogramSiteIdList(
                                    siteIdResponse.message!!
                                )
                            }
                        }

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

                else -> {}
            }
        }
//            }
//        }
    }
    sealed class Command {
        data class ShowToast(val message: String) : Command()
    }
}