package com.apollopharmacy.vishwam.ui.home.communityadvisor.servicescustomerinteraction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.data.network.ServicesCustomerRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.communityadvisor.CommunityAdvisorFragmentCallback
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.HomeServiceDetailsRequest
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.HomeServicesSaveDetailsRequest
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.ListBySiteIdResponse
import com.apollopharmacy.vishwam.ui.home.planogram.fragment.PlanogramCallback
import com.apollopharmacy.vishwam.ui.login.Command
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServicesCustomerViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()


    fun getCategoryServicesCustomerDetails(
        callback: ServicesCustomerCallback) {

        val state = MutableLiveData<State>()
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
       /* for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }*/
        baseUrl = "https://phrmaptestp.apollopharmacy.info:8443/HMS/getcategorydetails"
        token="5678ubvgfhlknk89"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ServicesCustomerRepo.getCategoryDetails(
                    baseUrl,
                    token
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value.status == true) {
                        callback.onSuccessGetServicesCustomerResponse(response.value)

                    } else {
                        callback.onFailureGetServicesCustomerResponse(response.value.message.toString())
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

    sealed class Command {
        data class ShowToast(val message: String) : Command()
    }

    fun getHomeServicesSaveDetails(
        homeServicesSaveDetailsRequest: HomeServicesSaveDetailsRequest,
        callback: ServicesCustomerCallback,
    ) {
        val state = MutableLiveData<State>()
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        /* for (i in data.APIS.indices) {
             if (data.APIS[i].NAME.equals("")) {
                 baseUrl = data.APIS[i].URL
                 token = data.APIS[i].TOKEN
                 break
             }
         }*/
        baseUrl = "https://phrmaptestp.apollopharmacy.info:8443/HMS/savehomeservicesdetails"
        token = "5678ubvgfhlknk89"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ServicesCustomerRepo.getHomeServicesSaveDetails(
                    baseUrl,
                    token, homeServicesSaveDetailsRequest
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value.status == true) {
                        callback.onSuccessGetHomeServicesSaveDetailsResponse(response.value)
                    } else {
                        callback.onFailureGetHomeServicesSaveDetailsResponse(response.value.message.toString())
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