package com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApproveListActivityRepo
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model.GetImageUrlsRequest
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model.SaveAcceptAndReshootRequest
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model.SaveAcceptAndReshootResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.RatingModelRequest
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.RatingModelResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApproveListViewModel : ViewModel() {

    var getImageUrlsResponse = MutableLiveData<GetImageUrlsResponse>()
    var saveAcceptAndReshootResponse = MutableLiveData<SaveAcceptAndReshootResponse>()
    var ratingBarResponse = MutableLiveData<RatingModelResponse>()
    var errorMessage = MutableLiveData<String>()


    fun getImageUrlsApiCall(pendingAndApproved: PendingAndApproved) {
        val state = MutableLiveData<State>()

        state.postValue(State.LOADING)
        val getImageUrlsRequest = GetImageUrlsRequest()
        getImageUrlsRequest.storeId = pendingAndApproved.storeId
        getImageUrlsRequest.swachhId = pendingAndApproved.swachhid
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("SW IMAGE URLS")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ApproveListActivityRepo.getImageUrlsApiCall(baseUrl, token, getImageUrlsRequest)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    when (response.value.status) {
                        true -> {
                            getImageUrlsResponse.value = response.value
                        }
                        else -> {}
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

    fun saveAccepetAndReshoot(saveAcceptAndReshootRequest: SaveAcceptAndReshootRequest) {

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
                ApproveListActivityRepo.saveAcceptAndReshoot(baseUrl,
                    token,
                    saveAcceptAndReshootRequest)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value.status == true) {
                        saveAcceptAndReshootResponse.value = response.value
                    } else {
                        errorMessage.value = response.value.message
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

    fun submitRatingBar(ratingModelRequest: RatingModelRequest) {

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
                ApproveListActivityRepo.submitRatingBar(baseUrl, token, ratingModelRequest)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value.status == true) {
                        ratingBarResponse.value = response.value
                    } else {
                        ratingBarResponse.value = response.value
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