package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApnaRectroApiRepo
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlRequest
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.login.Command
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApprovalPreviewViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()
    val imageUrlResponse = MutableLiveData<GetImageUrlResponse>()
    fun getRectroApprovalList(imageUrlRequest: GetImageUrlRequest, preRetroCallback: ApprovalReviewCallback) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("RT IMAGE URLS")) {
                baseUrl = "https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/GetImageUrls"
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                ApnaRectroApiRepo.retroImageUrlList("https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/GetImageUrls", "h72genrSSNFivOi/cfiX3A==", imageUrlRequest)

            }
            when (response) {

                is ApiResult.Success -> {


                    if (response.value.status ?: null == true) {
                        state.value = State.ERROR
                        preRetroCallback.onSuccessImageUrlList(response.value,
                            response.value.categoryList!!, imageUrlRequest.retroId!!)
                        imageUrlResponse.value = response.value
                    } else {
                        state.value = State.ERROR
                        preRetroCallback.onFailureImageUrlList(response.value)
                        imageUrlResponse.value = response.value
                    }
                }
                is ApiResult.GenericError -> {
                    command.postValue(response.error?.let {
                        Command.ShowToast(it)
                    })
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    command.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR

                }
                is ApiResult.UnknownError -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR

                }
                else -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))

                    state.value = State.ERROR
                }
            }
        }

    }

}