 package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.uploadnowactivity

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
import com.apollopharmacy.vishwam.data.network.SwachApiiRepo
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.OnSubmitSwachModelRequest
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.swachuploadfragment.CommandsNew
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetStorePersonHistoryodelRequest
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.OnUploadSwachModelRequest
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.OnUploadSwachModelResponse
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UploadNowButtonViewModel : ViewModel() {

    val commandsNewSwach = LiveEvent<CommandsNewSwachImp>()
    val state = MutableLiveData<State>()
    var swachhapolloModel = MutableLiveData<SwachModelResponse>()
    var uploadSwachModel = MutableLiveData<OnUploadSwachModelResponse>()


    fun swachImagesRegisters() {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SwachApiRepo.swachImagesRegister()
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status?:null == true) {
                        state.value = State.ERROR
                        swachhapolloModel.value = result.value
                    }

                    else {
                        state.value = State.ERROR
                        commandsNewSwach.value = CommandsNewSwachImp.ShowToast(result.value.message)
                    }
                }
                is ApiResult.GenericError -> {
                    commandsNewSwach.postValue(
                        result.error?.let {
                            CommandsNewSwachImp.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commandsNewSwach.postValue(CommandsNewSwachImp.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commandsNewSwach.postValue(CommandsNewSwachImp.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commandsNewSwach.postValue(CommandsNewSwachImp.ShowToast("Something went wrong, please try again later"))
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

            commandsNewSwach.postValue(CommandsNewSwachImp.ImageIsUploadedInAzur(response))
        }
    }


    fun onUploadSwach(onUploadSwachModelRequest: OnUploadSwachModelRequest) {
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
                SwachApiiRepo.onUploadSwach(
                    "h72genrSSNFivOi/cfiX3A==", onUploadSwachModelRequest)

//                        RegistrationRepo.NewComplaintRegistration(
//                            baseUrl,
//                            header,
//                            requestNewComplaintRegistration
//                        )
            }
            when (response) {

                is ApiResult.Success -> {

                        state.value = State.ERROR
                        uploadSwachModel.value = response.value


                     if(response.value.status?:null == false) {
                      state.value = State.ERROR
                         CommandsNewSwachImp.ShowToast(response.value.message)
                        uploadSwachModel.value?.message = response.value.message


                    }
                }
                is ApiResult.GenericError -> {
                    commandsNewSwach.postValue(
                        response.error?.let {
                            CommandsNewSwachImp.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commandsNewSwach.postValue(CommandsNewSwachImp.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commandsNewSwach.postValue(CommandsNewSwachImp.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commandsNewSwach.postValue(CommandsNewSwachImp.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
//            }
//        }
    }




}

sealed class CommandsNewSwachImp {
    data class ShowToast(val message: String?) : CommandsNewSwachImp()
    data class ImageIsUploadedInAzur(val filePath: SwachModelResponse.Config.ImgeDtcl) :
        CommandsNewSwachImp()
}