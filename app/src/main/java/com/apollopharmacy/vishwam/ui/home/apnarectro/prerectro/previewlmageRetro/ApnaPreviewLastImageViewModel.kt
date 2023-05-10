package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.previewlmageRetro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApnaRectroApiRepo
import com.apollopharmacy.vishwam.data.network.ApproveListActivityRepo
import com.apollopharmacy.vishwam.data.network.discount.PreviewLastImageRepo
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.ApprovalReviewCallback
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptRequest
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model.SaveAcceptAndReshootRequest
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model.SaveAcceptAndReshootResponse
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.RatingModelRequest
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.RatingModelResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApnaPreviewLastImageViewModel : ViewModel() {
    var saveAcceptAndReshootResponse = MutableLiveData<SaveAcceptResponse>()
    var errorMessage = MutableLiveData<String>()


    fun saveAccepetAndReshoot(
        saveAcceptAndReshootRequest: SaveAcceptRequest,
        previewLastImageCallback: PreviewLastImageCallback,
    ) {

        val state = MutableLiveData<State>()
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("SW SAVE ACCEPT AND RESHOOT")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ApnaRectroApiRepo.saveAcceptAndReshoot("https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/SAVEACCEPTANDRESHOOT",
                    "h72genrSSNFivOi/cfiX3A==",
                    saveAcceptAndReshootRequest)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value.status == true) {
                        previewLastImageCallback.onSuccessSaveAcceptReshoot(response.value)
                        saveAcceptAndReshootResponse.value = response.value
                    } else {
                        previewLastImageCallback.onFailureSaveAcceptReshoot(response.value)
                        saveAcceptAndReshootResponse.value = response.value
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
    fun getRatingResponse(
        saveAcceptAndReshootRequest: SaveAcceptRequest,
        previewLastImageCallback: PreviewLastImageCallback,
    ) {

        val state = MutableLiveData<State>()
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("SW SAVE ACCEPT AND RESHOOT")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ApnaRectroApiRepo.saveAcceptAndReshoot("https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/SAVEACCEPTANDRESHOOT",
                    "h72genrSSNFivOi/cfiX3A==",
                    saveAcceptAndReshootRequest)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value.status == true) {
                        previewLastImageCallback.onSuccessRatingResponse(response.value)
                    }else{
                        previewLastImageCallback.onFailureRatingResponse(response.value)
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