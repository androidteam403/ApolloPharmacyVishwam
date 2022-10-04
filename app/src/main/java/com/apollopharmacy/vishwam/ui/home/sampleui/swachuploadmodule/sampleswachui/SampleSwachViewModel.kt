
package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.sampleswachui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.data.network.SwachApiRepo
import com.apollopharmacy.vishwam.data.network.SwachApiiRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reviewratingactivity.adapter.RatingReviewViewModel
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.sampleswachui.model.LastUploadedDateResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.uploadnowactivity.CommandsNewSwachImp
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.uploadnowactivity.model.UpdateSwachhDefaultSiteRequest
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.*
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.CommandsNeww
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SampleSwachViewModel : ViewModel() {
    val commands = LiveEvent<CommandsNewSwachFrag>()
    val state = MutableLiveData<State>()
    val lastUploadedDateResponse = MutableLiveData<LastUploadedDateResponse>()
    var swachhapolloModel = MutableLiveData<SwachModelResponse>()
    var checkDayWiseAccess = MutableLiveData<CheckDayWiseAccessResponse>()
    var getStorePersonHistory = MutableLiveData<GetStorePersonHistoryodelResponse>()
    var uploadSwachModel = MutableLiveData<OnUploadSwachModelResponse>()
    var getImageUrlsList = MutableLiveData<GetImageUrlModelResponse>()


    fun swachImagesRegisters() {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SwachApiRepo.swachImagesRegister(Preferences.getSwachhSiteId())
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        swachhapolloModel.value = result.value
                    } else {
                        state.value = State.ERROR
                        commands.value = CommandsNewSwachFrag.ShowToast(result.value.message)
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        result.error?.let {
                            CommandsNewSwachFrag.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }


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
                        getImageUrlsList.value = response.value
                        getImageUrlsList.value?.message = response.value.message


                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        response.error?.let {
                            CommandsNewSwachFrag.ShowToast(it)

                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
//            }
//        }
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
                    commands.postValue(
                        response.error?.let {
                            CommandsNewSwachFrag.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
//            }
//        }
    }


    fun checkDayWiseAccess(sampleSwachUiCallback: SampleSwachUiCallback) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SwachApiiRepo.checkDayWiseAccess(Preferences.getSwachhSiteId())
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true && result.value.message == "SUCCESS") {
                        state.value = State.ERROR
                        sampleSwachUiCallback.onSuccessDayWiseAccesss(result.value)

//                        checkDayWiseAccess.value = result.value
                    } else {
                        state.value = State.ERROR
                        commands.value = CommandsNewSwachFrag.ShowToast(result.value.message)
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        result.error?.let {
                            CommandsNewSwachFrag.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }

    fun getStorePersonHistory(getStorePersonHistoryodelRequest: GetStorePersonHistoryodelRequest) {
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
                SwachApiiRepo.getStorePersonHistory(
                    "h72genrSSNFivOi/cfiX3A==", getStorePersonHistoryodelRequest)

//                        RegistrationRepo.NewComplaintRegistration(
//                            baseUrl,
//                            header,
//                            requestNewComplaintRegistration
//                        )
            }
            when (response) {

                is ApiResult.Success -> {

                    state.value = State.ERROR
                    getStorePersonHistory.value = response.value


                    if (response.value.status ?: null == false) {
                        state.value = State.ERROR
                        CommandsNewSwachImp.ShowToast(response.value.message)
                        getStorePersonHistory.value?.message = response.value.message


                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        response.error?.let {
                            CommandsNewSwachFrag.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
//            }
//        }
    }

    fun getLastUploadedDate() {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SwachApiRepo.getLastUploadedDate("h72genrSSNFivOi/cfiX3A==",
                    Preferences.getSwachhSiteId(),
                    Preferences.getValidatedEmpId())
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        lastUploadedDateResponse.value = result.value
                    } else {
                        state.value = State.ERROR
                        commands.value = CommandsNewSwachFrag.ShowToast(result.value.message)
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        result.error?.let {
                            CommandsNewSwachFrag.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(CommandsNewSwachFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }

    }

    sealed class CommandsNewSwachFrag {
        data class ShowToast(val message: String?) : CommandsNewSwachFrag()
        data class ImageIsUploadedInAzur(val filePath: SwachModelResponse.Config.ImgeDtcl) :
            CommandsNewSwachFrag()
    }
}