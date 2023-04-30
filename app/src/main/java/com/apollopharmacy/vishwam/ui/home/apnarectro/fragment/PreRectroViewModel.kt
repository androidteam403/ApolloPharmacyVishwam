package com.apollopharmacy.vishwam.ui.home.apnarectro.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApnaRectroApiRepo
import com.apollopharmacy.vishwam.data.network.SwachApiiRepo
import com.apollopharmacy.vishwam.data.network.SwachhListRepo
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetStorePendingAndApprovedListReq
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.model.GetpendingAndApprovedListRequest
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.sampleswachui.SampleSwachUiCallback
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.sampleswachui.SampleSwachViewModel
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.CommandsNewSwachImp
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetStorePersonHistoryodelRequest
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreRectroViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    val commands = LiveEvent<CommandsApnaFrag>()

    fun getStorePendingApprovedListApiCallApnaRetro(
        getStorePendingAndApprovedListReq: GetStorePendingAndApprovedListReq,
        preRectroCallback: PreRectroCallback
    ) {
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
            if (data.APIS[i].NAME.equals("RT STORE PENDING AND APPROVED LIST")) {
                baseUrl = "https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/GetStorependingAndApprovedList"
                token = data.APIS[i].TOKEN
                break
            }
        }

        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                ApnaRectroApiRepo.getStorePendingAndApprovedListApnaRetro("https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/GetStorependingAndApprovedList"
                    ,
                    "h72genrSSNFivOi/cfiX3A==",
                    getStorePendingAndApprovedListReq)

//                        RegistrationRepo.NewComplaintRegistration(
//                            baseUrl,
//                            header,
//                            requestNewComplaintRegistration
//                        )
            }
            when (response) {

                is ApiResult.Success -> {

                    state.value = State.ERROR
//                    getStorePersonHistory.value = response.value
                    preRectroCallback.onSuccessgetStorePendingApprovedApiCall(response.value)

                    if (response.value.status ?: null == false) {
                        state.value = State.ERROR
                        CommandsNewSwachImp.ShowToast(response.value.message)
                        preRectroCallback.onSuccessgetStorePendingApprovedApiCall(response.value)
//                        getStorePersonHistory.value = response.value


                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(response.error?.let {
                        CommandsApnaFrag.ShowToast(it)
                    })
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(CommandsApnaFrag.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(CommandsApnaFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(CommandsApnaFrag.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
//            }
//        }
    }


    sealed class CommandsApnaFrag {
        data class ShowToast(val message: String?) : CommandsApnaFrag()
    }
}