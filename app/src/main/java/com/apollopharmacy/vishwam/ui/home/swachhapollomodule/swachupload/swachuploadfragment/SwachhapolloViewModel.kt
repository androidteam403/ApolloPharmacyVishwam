package com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.swachuploadfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.azure.ConnectionAzureSwacch
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.SwachApiRepo
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.OnSubmitSwachModelRequest
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.OnSubmitSwachModelResponse
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SwachhapolloViewModel : ViewModel()
{
    val commands = LiveEvent<CommandsNew>()
    val state = MutableLiveData<State>()
    var swachhapolloModel = MutableLiveData<SwachModelResponse>()
    var onSubmitSwachModel = MutableLiveData<OnSubmitSwachModelResponse>()


    fun swachImagesRegister() {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SwachApiRepo.swachImagesRegister(Preferences.getSiteId())
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status?:null == true) {
                        state.value = State.ERROR
                        swachhapolloModel.value = result.value
                    }

                    else {
                        state.value = State.ERROR
                        commands.value = CommandsNew.ShowToast(result.value.message)
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        result.error?.let {
                            CommandsNew.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(CommandsNew.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(CommandsNew.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(CommandsNew.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }

    fun connectToAzure(image: SwachModelResponse.Config.ImgeDtcl?) {
        state.value = State.SUCCESS
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                ConnectionAzureSwacch.connectToAzur(image,
                    Config.CONTAINER_NAME,
                    Config.STORAGE_CONNECTION_FOR_CCR_APP
                )

            commands.postValue(CommandsNew.ImageIsUploadedInAzur(response))
        }
    }

    fun onSubmitSwacch(onSubmitSwachModelRequest: ArrayList<OnSubmitSwachModelRequest>) {
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
                        SwachApiRepo.onSubmitSwacch(
                            "h72genrSSNFivOi/cfiX3A==", onSubmitSwachModelRequest)

//                        RegistrationRepo.NewComplaintRegistration(
//                            baseUrl,
//                            header,
//                            requestNewComplaintRegistration
//                        )
                    }
                    when (response) {

                        is ApiResult.Success -> {
                            if (response.value.status?:null == true) {
                                state.value = State.ERROR
                                onSubmitSwachModel.value = response.value
                            }

                            else {
                                state.value = State.ERROR
                                commands.value = CommandsNew.ShowToast(response.value.message)
                            }
                        }
                        is ApiResult.GenericError -> {
                            commands.postValue(
                                response.error?.let {
                                    CommandsNew.ShowToast(it)
                                }
                            )
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            commands.postValue(CommandsNew.ShowToast("Network Error"))
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            commands.postValue(CommandsNew.ShowToast("Something went wrong, please try again later"))
                            state.value = State.ERROR
                        }
                        else -> {
                            commands.postValue(CommandsNew.ShowToast("Something went wrong, please try again later"))
                            state.value = State.ERROR
                        }
                    }
                }
//            }
//        }
    }

}

sealed class CommandsNew {
    data class ShowToast(val message: String?) : CommandsNew()
    data class ImageIsUploadedInAzur(val filePath: SwachModelResponse.Config.ImgeDtcl) :
        CommandsNew()
}