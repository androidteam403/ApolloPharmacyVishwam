package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewlastimage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApproveListActivityRepo
import com.apollopharmacy.vishwam.data.network.discount.PreviewLastImageRepo
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.SaveAcceptAndReshootRequest
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.SaveAcceptAndReshootResponse
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.RatingModelRequest
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.RatingModelResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreviewLastImageViewModel: ViewModel()  {
    var saveAcceptAndReshootResponse = MutableLiveData<SaveAcceptAndReshootResponse>()
    var ratingBarResponse = MutableLiveData<RatingModelResponse>()
    var errorMessage = MutableLiveData<String>()

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


    fun submitRatingBar(ratingModelRequest: RatingModelRequest) {

        val state = MutableLiveData<State>()

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                PreviewLastImageRepo.submitRatingBar(ratingModelRequest)
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