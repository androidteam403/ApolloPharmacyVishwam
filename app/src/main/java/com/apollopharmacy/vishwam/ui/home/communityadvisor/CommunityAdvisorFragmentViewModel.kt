package com.apollopharmacy.vishwam.ui.home.communityadvisor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ServicesCustomerRepo
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.HomeServiceDetailsRequest
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.HomeServiceDetailsResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommunityAdvisorFragmentViewModel : ViewModel() {
    fun getHomeServiceDetails(
        homeServiceDetailsRequest: HomeServiceDetailsRequest,
        callback: CommunityAdvisorFragmentCallback,
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
        baseUrl = "https://phrmapvtuat.apollopharmacy.info:8443/HMS/GetHomeServiceDetails"//"https://phrmaptestp.apollopharmacy.info:8443/HMS/GetHomeServiceDetails"
        token = "5678ubvgfhlknk89"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ServicesCustomerRepo.getHomeServiceDetails(
                    baseUrl,
                    token, homeServiceDetailsRequest
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value.status == true) {
                        callback.onSuccessHomeServiceDetails(response.value)
                    } else {
                        callback.onFailureHomeServiceDetails(response.value.message.toString())
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