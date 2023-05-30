package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.azure.ConnectionAzureApna
import com.apollopharmacy.vishwam.data.azure.ConnectionToAzurePreRetroUploadApna
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApnaRectroApiRepo
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveImagesUrlsRequest
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.CommandsNewSwachImp
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.GetStoreWiseCatDetailsApnaResponse
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class UploadImagesViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    val commands = LiveEvent<CommandsUploadImages>()

    fun getStoreWiseDetailsApna(uploadImagesCallback: UploadImagesCallback) {
        state.postValue(State.LOADING)

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (Config.KEY=="2039") {
                if (data.APIS[i].NAME.equals("RT STORE WISE CATEGORY DETAILS")) {
                    baseUrl = data.APIS[i].URL
                    token = data.APIS[i].TOKEN
                    break
                }
            }
            else
                if (Config.KEY=="2034"){
                    baseUrl = "https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/GetStoreWiseCategoryDetails?Storeid=16001"
                    token = "h72genrSSNFivOi/cfiX3A=="
                }
        }


        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ApnaRectroApiRepo.getStoreWiseCatDetailsApna(baseUrl, token, Preferences.getApnaSiteId())
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true && result.value.message == "success") {
                        uploadImagesCallback.onSuccessGetStoreWiseDetails(result.value)

                    } else {
                        state.value = State.ERROR
                        commands.value =CommandsUploadImages.ShowToast(result.value.message)
                        uploadImagesCallback.onFailureStoreWiseDetails(result.value)

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun connectToAzure(apnaConfigList: ArrayList<GetStoreWiseCatDetailsApnaResponse>, uploadImagesCallback: UploadImagesCallback, boolean: Boolean) {
        state.value = State.SUCCESS
        viewModelScope.launch(Dispatchers.IO) {
            val response = ConnectionToAzurePreRetroUploadApna.connectToAzurList(
                apnaConfigList!!,
                Config.CONTAINER_NAME,
                Config.STORAGE_CONNECTION_FOR_CCR_APP
            )
            uploadImagesCallback.onSuccessImageIsUploadedInAzur(response)

        }
    }

    fun onUploadImagesApna(saveImageUrlsRequest: SaveImagesUrlsRequest, uploadImagesCallback: UploadImagesCallback) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (Config.KEY=="2039") {

                if (data.APIS[i].NAME.equals("RT SAVE IMAGE URLS")) {
                    baseUrl = data.APIS[i].URL
                    token = data.APIS[i].TOKEN
                    break
                }
            }
            else
                if (Config.KEY=="2034"){
                    baseUrl ="https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/SaveImageUrls"
                    token = "h72genrSSNFivOi/cfiX3A=="
                }
        }
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                ApnaRectroApiRepo.saveImageUrlsApna(baseUrl, token, saveImageUrlsRequest)

//                        RegistrationRepo.NewComplaintRegistration(
//                            baseUrl,
//                            header,
//                            requestNewComplaintRegistration
//                        )
            }
            when (response) {

                is ApiResult.Success -> {

                    state.value = State.ERROR
                    uploadImagesCallback.onSuccessSaveImageUrlsApi(response.value)


                    if (response.value.status ?: null == false) {
                        state.value = State.ERROR
                        CommandsNewSwachImp.ShowToast(response.value.message)
                        uploadImagesCallback.onFailureSaveImageUrlsApi(response.value)


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