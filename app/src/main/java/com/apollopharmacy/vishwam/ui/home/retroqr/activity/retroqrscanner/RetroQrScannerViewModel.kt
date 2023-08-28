package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.QrRetroRepo
import com.apollopharmacy.vishwam.data.network.RetroQrRepo
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.RetroQrUploadCallback
import com.apollopharmacy.vishwam.ui.login.Command
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RetroQrScannerViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()


    fun getImageUrlsFromRackId(barcode: String,callback: RetroQrScannerCallback) {

        val state = MutableLiveData<State>()
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
//        var baseUrl = "https://phrmaptestp.apollopharmacy.info:8443/apnaqrcode/getStoreWiseRackDetails?STOREID=${Preferences.getQrSiteId()}"
//        var token = "h72genrSSNFivOi/cfiX3A=="
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("APQR GETSTOREWISERACKDETAILS")) {
                baseUrl = "https://phrmaptestp.apollopharmacy.info:8443/apnaqrcode/GetImagesByRackidAndStoreId?"
                token = data.APIS[i].TOKEN
                break
            }
        }
        baseUrl+="STOREID=${barcode.split("-").get(0)}&RACKID=${barcode.split("-").get(1)}"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                QrRetroRepo.getImageUrlsByRack(
                    baseUrl ,
                    token
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value.status == true) {
                        callback.onSuccessGetImageUrlApiCall(response.value)

                    } else {
                        callback.onFailureGetImageUrlApiCall(response.value.message.toString())
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


//    fun getImageUrl(url: String, callback: RetroQrScannerCallback) {
//        viewModelScope.launch {
//            state.postValue(State.SUCCESS)
//            val result = withContext(Dispatchers.IO) {
//                RetroQrRepo.getImageUrl(url)
//            }
//            when (result) {
//                is ApiResult.Success -> {
//                    callback.onSuccessGetImageUrlApiCall(result.value)
//                }
//                else -> {}
//            }
//        }
//    }
}