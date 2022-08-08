package com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.SwachApiiRepo
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.*
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReShootActivityViewModel : ViewModel() {
    val commands = LiveEvent<CommandsNeww>()
    val state = MutableLiveData<State>()
    var getImageUrlsList = MutableLiveData<GetImageUrlModelResponse>()



fun getImageUrl(getImageUrlModelRequest: GetImageUrlModelRequest) {
    val url = Preferences.getApi()
    val data = Gson().fromJson(url, ValidateResponse::class.java)
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("SAVE CATEGORY WISE IMAGE URLS")) {
//                val baseUrl = data.APIS[i].URL
//                val token = data.APIS[i].TOKEN
    /*  val baseUrl =
          "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/mobile-ticket-save"*/
//                val onSubmitSwachModelRequestJson =
//                    Gson().toJson(onSubmitSwachModelRequest)

//                val header = "application/json"
    viewModelScope.launch {
        val response = withContext(Dispatchers.IO) {
            SwachApiiRepo.getImageUrl(
                "h72genrSSNFivOi/cfiX3A==", getImageUrlModelRequest
            )


        }
        when (response) {

            is ApiResult.Success -> {
                if (response.value.status?:null == true) {
                    state.value = State.ERROR
                    getImageUrlsList.value = response.value
                }

                if (response.value.status ?: null == false) {
                    state.value = State.ERROR
                    CommandsNeww.ShowToast(response.value.message)
                    getImageUrlsList.value?.message = response.value.message


                }
            }
            is ApiResult.GenericError -> {
                commands.postValue(
                    response.error?.let {
                        CommandsNeww.ShowToast(it)
                    }
                )
                state.value = State.ERROR
            }
            is ApiResult.NetworkError -> {
                commands.postValue(CommandsNeww.ShowToast("Network Error"))
                state.value = State.ERROR
            }
            is ApiResult.UnknownError -> {
                commands.postValue(CommandsNeww.ShowToast("Something went wrong, please try again later"))
                state.value = State.ERROR
            }
            else -> {
                commands.postValue(CommandsNeww.ShowToast("Something went wrong, please try again later"))
                state.value = State.ERROR
            }
        }
    }
//            }
//        }
}

}

sealed class CommandsNeww {
    data class ShowToast(val message: String?) : CommandsNeww()
    data class ImageIsUploadedInAzur(val filePath: SwachModelResponse.Config.ImgeDtcl) :
        CommandsNeww()
}