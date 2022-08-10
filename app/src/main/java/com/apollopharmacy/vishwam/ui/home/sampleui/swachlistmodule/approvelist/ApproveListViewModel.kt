package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApproveListActivityRepo
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsRequest
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.SaveAcceptAndReshootRequest
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.SaveAcceptAndReshootResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApproveListViewModel : ViewModel() {

    var getImageUrlsResponse = MutableLiveData<GetImageUrlsResponse>()
    var saveAcceptAndReshootResponse = MutableLiveData<SaveAcceptAndReshootResponse>()
    var errorMessage = MutableLiveData<String>()


    fun getImageUrlsApiCall(pendingAndApproved: PendingAndApproved) {
        val state = MutableLiveData<State>()

        state.postValue(State.LOADING)
        val getImageUrlsRequest = GetImageUrlsRequest()
        getImageUrlsRequest.storeId = pendingAndApproved.storeId
        getImageUrlsRequest.swachhId = pendingAndApproved.swachhid


        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ApproveListActivityRepo.getImageUrlsApiCall(getImageUrlsRequest)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    when (response.value.status) {
                        true -> {
                            getImageUrlsResponse.value = response.value
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

    fun saveAccepetAndReshoot(saveAcceptAndReshootRequest: SaveAcceptAndReshootRequest) {

        val state = MutableLiveData<State>()

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ApproveListActivityRepo.saveAcceptAndReshoot(saveAcceptAndReshootRequest)
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
}