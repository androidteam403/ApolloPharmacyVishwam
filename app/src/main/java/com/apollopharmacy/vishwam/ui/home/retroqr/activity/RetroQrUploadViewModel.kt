package com.apollopharmacy.vishwam.ui.home.retroqr.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.QrRetroRepo
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.QrSaveImageUrlsRequest
import com.apollopharmacy.vishwam.ui.login.Command
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RetroQrUploadViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()

    val key = "blobfilesload"


    fun saveImageUrlsApiCall(
        saveImageUrlsRequest: QrSaveImageUrlsRequest,
        retroQrUploadCallback: RetroQrUploadCallback,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var baseToken = ""
//        var baseUrl = "https://phrmaptestp.apollopharmacy.info:8443/apnaqrcode/SaveImageUrls"
//        var baseToken = "h72genrSSNFivOi/cfiX3A=="
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("APQR SAVEIMAGEURLS")) {
                baseUrl = data.APIS[i].URL
                baseToken = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                QrRetroRepo.saveImageUrlsApiCallQr(
                    baseUrl, baseToken, saveImageUrlsRequest
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.SUCCESS
                    if (response.value.status == true) {
                        retroQrUploadCallback.onSuccessUploadImagesApiCall(response.value.message!!)
                    } else {
                        if (response.value.message != null) {
                            retroQrUploadCallback.onFailureUploadImagesApiCall(
                                response.value.message!!
                            )
                        } else {
                            retroQrUploadCallback.onFailureUploadImagesApiCall("Something went wrong.")
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

    fun getStoreWiseRackDetails(retroQrUploadCallback: RetroQrUploadCallback) {

        val state = MutableLiveData<State>()
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
//        var baseUrl = "https://phrmaptestp.apollopharmacy.info:8443/apnaqrcode/getStoreWiseRackDetails?STOREID=${Preferences.getQrSiteId()}"
//        var token = "h72genrSSNFivOi/cfiX3A=="
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("APQR GETSTOREWISERACKDETAILS")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                QrRetroRepo.getStoreWiseRackDetails(
                    baseUrl + "STOREID=${Preferences.getQrSiteId()}",
                    token
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value.status == true) {
                        retroQrUploadCallback.onSuccessgetStoreWiseRackResponse(response.value)

                    } else {
                        retroQrUploadCallback.onFailureStoreWiseRackResponse(response.value.message.toString())
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