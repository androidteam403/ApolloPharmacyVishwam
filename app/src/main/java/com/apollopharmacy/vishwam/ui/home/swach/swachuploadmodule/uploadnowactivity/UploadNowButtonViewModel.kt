package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.azure.ConnectionAzureSwacch
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.SwachApiRepo
import com.apollopharmacy.vishwam.data.network.SwachApiiRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.model.UpdateSwachhDefaultSiteRequest
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.model.UpdateSwachhDefaultSiteResponse
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.OnUploadSwachModelRequest
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.OnUploadSwachModelResponse
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
    var updateSwachhDefaultSiteResponseModel = MutableLiveData<UpdateSwachhDefaultSiteResponse>()


    fun swachImagesRegisters() {
        state.postValue(State.LOADING)

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("SW STORE WISE CATEGORY DETAILS")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SwachApiRepo.swachImagesRegister(baseUrl, token, Preferences.getSwachhSiteId())
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        swachhapolloModel.value = result.value
                    } else {
                        state.value = State.ERROR
                        commandsNewSwach.value = CommandsNewSwachImp.ShowToast(result.value.message)
                        swachhapolloModel.value = result.value
                    }
                }
                is ApiResult.GenericError -> {
                    commandsNewSwach.postValue(result.error?.let {
                        CommandsNewSwachImp.ShowToast(it)
                    })
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

    fun connectToAzure(swachhModelResponseList: ArrayList<SwachModelResponse>) {
        state.value = State.SUCCESS
        viewModelScope.launch(Dispatchers.IO) {
            val response = ConnectionAzureSwacch.connectToAzurList(swachhModelResponseList!!,
                Config.CONTAINER_NAME,
                Config.STORAGE_CONNECTION_FOR_CCR_APP)

            commandsNewSwach.postValue(CommandsNewSwachImp.ImageIsUploadedInAzur(response))
        }
    }


    fun onUploadSwach(onUploadSwachModelRequest: OnUploadSwachModelRequest) {
//        val url = Preferences.getApi()
//        val data = Gson().fromJson(url, ValidateResponse::class.java)
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("SAVE CATEGORY WISE IMAGE URLS")) {
//                val baseUrl = data.APIS[i].URL
//                val token = data.APIS[i].TOKEN
        /*  val baseUrl =
              "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/save-update/mobile-ticket-save"*/
//                val onSubmitSwachModelRequestJson =
//                    Gson().toJson(onSubmitSwachModelRequest)

//                val header = "application/json"


        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("SW SAVE IMAGE URLS")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                SwachApiiRepo.onUploadSwach(baseUrl, token, onUploadSwachModelRequest)

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


                    if (response.value.status ?: null == false) {
                        state.value = State.ERROR
                        CommandsNewSwachImp.ShowToast(response.value.message)
                        uploadSwachModel.value?.message = response.value.message


                    }
                }
                is ApiResult.GenericError -> {
                    commandsNewSwach.postValue(response.error?.let {
                        CommandsNewSwachImp.ShowToast(it)
                    })
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

    fun updateSwachhSiteIdApiCall(updateSwachhDefaultSiteRequest: UpdateSwachhDefaultSiteRequest) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)


        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        val requestCMSLoginJson = Gson().toJson(updateSwachhDefaultSiteRequest)
        //https://apis.v35.dev.zeroco.de
        //
        val baseUrl: String =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/user/save-update/update-swatch-default-site"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                SwachApiiRepo.updateSwachhDefaultSite(baseUrL,"h72genrSSNFivOi/cfiX3A==",
                    GetDetailsRequest(baseUrl, "POST", requestCMSLoginJson, "", ""))
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val updateSwachhDefaultSiteResponse =
                                Gson().fromJson(BackShlash.removeSubString(res),
                                    UpdateSwachhDefaultSiteResponse::class.java)
                            if (updateSwachhDefaultSiteResponse.success!!) {
                                updateSwachhDefaultSiteResponseModel.value =
                                    updateSwachhDefaultSiteResponse
                            } else {

                            }
                        }
                    } else {
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


sealed class CommandsNewSwachImp {
    data class ShowToast(val message: String?) : CommandsNewSwachImp()
    data class ImageIsUploadedInAzur(val filePath: ArrayList<SwachModelResponse>) :
        CommandsNewSwachImp()
}