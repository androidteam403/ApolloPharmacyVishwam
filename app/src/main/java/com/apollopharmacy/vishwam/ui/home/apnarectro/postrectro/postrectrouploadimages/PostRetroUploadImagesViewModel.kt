package com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.postrectrouploadimages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.azure.ConnectionAzureApna
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApnaRectroApiRepo
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlsModelApnaRequest
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveImagesUrlsRequest
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.CommandsNewSwachImp
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.GetStoreWiseCatDetailsApnaResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.CommandsNeww
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class PostRetroUploadImagesViewModel: ViewModel() {
    val state = MutableLiveData<State>()
    val commands = LiveEvent<CommandsUploadImages>()


    fun connectToAzure(apnaConfigList: ArrayList<GetStoreWiseCatDetailsApnaResponse>, postRetroUploadImagesCallback: PostRetroUploadImagesCallback) {
        state.value = State.SUCCESS
        viewModelScope.launch(Dispatchers.IO) {
            val response = ConnectionAzureApna.connectToAzurList(apnaConfigList!!,
                Config.CONTAINER_NAME,
                Config.STORAGE_CONNECTION_FOR_CCR_APP)
            postRetroUploadImagesCallback.onSuccessImageIsUploadedInAzur(response)

        }
    }

    fun getStoreWiseDetailsApna(postRetroUploadImagesCallback: PostRetroUploadImagesCallback) {
        state.postValue(State.LOADING)

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("RT STORE WISE CATEGORY DETAILS")) {
                baseUrl = "https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/GetStoreWiseCategoryDetails?Storeid=16001"
                token = data.APIS[i].TOKEN
                break
            }
        }


        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ApnaRectroApiRepo.getStoreWiseCatDetailsApna("https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/GetStoreWiseCategoryDetails?Storeid=16001", "h72genrSSNFivOi/cfiX3A==", "16001")
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true && result.value.message == "success") {
                        postRetroUploadImagesCallback.onSuccessGetStoreWiseDetails(result.value)

                    } else {
                        state.value = State.ERROR
                        commands.value = CommandsUploadImages.ShowToast(result.value.message)
                        postRetroUploadImagesCallback.onFailureStoreWiseDetails(result.value)

                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(result.error?.let {
                       CommandsUploadImages.ShowToast(it)
                    })
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(CommandsUploadImages.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(CommandsUploadImages.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(CommandsUploadImages.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }


    }

    fun onUploadImagesApna(saveImageUrlsRequest: SaveImagesUrlsRequest, postRetroUploadImagesCallback: PostRetroUploadImagesCallback) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("RT SAVE IMAGE URLS")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                ApnaRectroApiRepo.saveImageUrlsApna("https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/SaveImageUrls", "h72genrSSNFivOi/cfiX3A==", saveImageUrlsRequest)

//                        RegistrationRepo.NewComplaintRegistration(
//                            baseUrl,
//                            header,
//                            requestNewComplaintRegistration
//                        )
            }
            when (response) {

                is ApiResult.Success -> {

                    state.value = State.ERROR
                    postRetroUploadImagesCallback.onSuccessSaveImageUrlsApi(response.value)


                    if (response.value.status ?: null == false) {
                        state.value = State.ERROR
                        CommandsNewSwachImp.ShowToast(response.value.message)
                        postRetroUploadImagesCallback.onFailureSaveImageUrlsApi(response.value)


                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(response.error?.let {
                        CommandsUploadImages.ShowToast(it)
                    })
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(CommandsUploadImages.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(CommandsUploadImages.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(CommandsUploadImages.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
//            }
//        }
    }

    fun getImageUrl(getImageUrlsModelApnaRequest: GetImageUrlsModelApnaRequest, postRetroUploadImagesCallback: PostRetroUploadImagesCallback) {
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
            if (data.APIS[i].NAME.equals("RT IMAGE URLS")) {
                baseUrl = "https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/GetImageUrls"
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                ApnaRectroApiRepo.getImageUrlApnaRetro("https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/GetImageUrls", "h72genrSSNFivOi/cfiX3A==", getImageUrlsModelApnaRequest)


            }
            when (response) {

                is ApiResult.Success -> {
                    if (response.value.status ?: null == true) {
                        state.value = State.ERROR
                        postRetroUploadImagesCallback.onSuccessImageUrlsList(response.value,getImageUrlsModelApnaRequest.retroId)
                    }

                    if (response.value.status ?: null == false) {
                        state.value = State.ERROR
                        CommandsNeww.ShowToast(response.value.message)
                        postRetroUploadImagesCallback.onFailureImageUrlsList(response.value)


                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(response.error?.let {
                        CommandsUploadImages.ShowToast(it)

                    })
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(CommandsUploadImages.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(CommandsUploadImages.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(CommandsUploadImages.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
//            }
//        }
    }

    sealed class CommandsUploadImages {
        data class ShowToast(val message: String?) : CommandsUploadImages()
    }
}