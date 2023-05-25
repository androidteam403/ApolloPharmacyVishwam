package com.apollopharmacy.vishwam.ui.home.apnarectro.approval

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.BuildConfig
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApnaRectroApiRepo
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendindAndApproverequest
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse
import com.apollopharmacy.vishwam.ui.login.Command
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreRectroApprovalViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()
    val retroPendingAndApproveResponse = MutableLiveData<GetRetroPendingAndApproveResponse>()

    fun getRectroApprovalList(getRetroPendindAndApproverequest: GetRetroPendindAndApproverequest, preRetroCallback: PreRectroApprovalCallback) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {


                if (data.APIS[i].NAME.equals("RT PENDING APPROVED LIST")) {
                    baseUrl = data.APIS[i].URL
                    token = data.APIS[i].TOKEN
                    break
                }
            }
//            else
//                if (Config.KEY=="2034"){
//                    baseUrl =
//                        "https://online.apollopharmacy.org/ARTRO/APOLLO/Retro/GetpendingAndApprovedList"
//                    token = "h72genrSSNFivOi/cfiX3A=="
//                }
//        }
//        POST https://172.16.103.116:8443/ARTRO/APOLLO/Retro/SAVEACCEPTANDRESHOOT
//        {"IMAGEURLS":[{"IMAGEID":"681","STATUSID":"1"},{"IMAGEID":"682","STATUSID":"1"},{"IMAGEID":"683","STATUSID":"1"},{"IMAGEID":"684","STATUSID":"1"}],"RATING":"","REAMRKS":"","RETROAUTOID":"","STAGEID":"","STATUSID":"1","STOREID":"16001","TYPE":"","USERID":"APL49391"}


        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                ApnaRectroApiRepo.retroApprovalList(baseUrl, token, getRetroPendindAndApproverequest)

            }
            when (response) {

                is ApiResult.Success -> {


                        if (response.value.status ?: null == true) {
                            state.value = State.ERROR
                            preRetroCallback.onSuccessRetroApprovalList(response.value)
                            retroPendingAndApproveResponse.value = response.value!!
                        } else {
                            state.value = State.ERROR
                            preRetroCallback.onFailureRetroApprovalList(response.value)
                            retroPendingAndApproveResponse.value = response.value!!
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