package com.apollopharmacy.vishwam.ui.home.apollosensing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApolloSensingRepo
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SaveImageUrlsRequest
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SendGlobalSmsRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApolloSensingViewModel : ViewModel() {

    val state = MutableLiveData<State>()
    fun sendGlobalSmsApiCall(
        type: String,
        sendGlobalSmsRequest: SendGlobalSmsRequest,
        apolloSensingFragmentCallback: ApolloSensingFragmentCallback,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)

        var baseUrl = "https://172.16.103.116:8443/GSMS/APOLLO/SMS/SendGlobalSms"
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("testt")) {
                baseUrl = data.APIS[i].URL
                break
            }
        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ApolloSensingRepo.sendGlobalSmsApiCall(
                    baseUrl, sendGlobalSmsRequest
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.SUCCESS
                    if (response.value.status == true) {
                        apolloSensingFragmentCallback.onSuccessSendGlobalSms(response.value, type)
                    } else {
                        apolloSensingFragmentCallback.onFailureSendGlobalSms(response.value, type)
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

    fun getLinkApiCall(
        customerName: String,
        customerMobileNumber: String,
        apolloSensingFragmentCallback: ApolloSensingFragmentCallback,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)

        var baseUrl =
            "https://t.zeroco.de/index.php?url=http://dev.thresholdsoft.com/Apollo-sensing/?format=text&cusomer=$customerName&mobile=$customerMobileNumber"
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("testt")) {
                baseUrl = data.APIS[i].URL
                break
            }
        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ApolloSensingRepo.getLinkApolloSensingApiCall(
                    baseUrl
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.SUCCESS
                    if (response.value != null && response.value.string() != null && response.value.string()
                            .isNotEmpty()
                    ) {
                        apolloSensingFragmentCallback.onSuccessGetLinkApolloSensing(response.value.string())
                    } else {
                        apolloSensingFragmentCallback.onFailureGetLinkApolloSensing("Something went wrong")
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

    fun saveImageUrlsApiCall(
        saveImageUrlsRequest: SaveImageUrlsRequest,
        apolloSensingFragmentCallback: ApolloSensingFragmentCallback,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)

        var baseUrl = "https://172.16.103.116:8443/SENSING/SaveSensingDetails"
        var baseToken = "h72genrSSNFivOi/cfiX3A=="
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("testt")) {
                baseUrl = data.APIS[i].URL
                baseToken = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ApolloSensingRepo.saveImageUrlsApiCall(
                    baseUrl, baseToken, saveImageUrlsRequest
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.SUCCESS
                    if (response.value.status == true) {
                        apolloSensingFragmentCallback.onSuccessUploadPrescriptionApiCall(response.value.message!!)
                    } else {
                        if (response.value.message != null) {
                            apolloSensingFragmentCallback.onFailureUploadPrescriptionApiCall(
                                response.value.message!!
                            )
                        } else {
                            apolloSensingFragmentCallback.onFailureUploadPrescriptionApiCall("Something went wrong.")
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