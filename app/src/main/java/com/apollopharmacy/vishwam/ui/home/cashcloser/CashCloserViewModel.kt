package com.apollopharmacy.vishwam.ui.home.cashcloser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.CashDepositApiRepo
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.CashDepositDetailsRequest
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.CashDepositDetailsResponse
import com.apollopharmacy.vishwam.ui.login.Command
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CashCloserViewModel : ViewModel() {
    val cashDepositDetails = MutableLiveData<CashDepositDetailsResponse>()
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()

    fun getCashDepositDetails(siteId: String, mCallback: CashCloserFragmentCallback) {
        // var token = "h72genrSSNFivOi/cfiX3A=="
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var apiUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CD GETCASHDEPOSITDETAILS")) {
                apiUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }




        viewModelScope.launch {
            state.postValue(State.SUCCESS)
            val result = withContext(Dispatchers.IO) {
                CashDepositApiRepo.getCashDepositDetails(apiUrl, token, siteId)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.SUCCESS
                        mCallback.onSuccessGetCashDepositDetailsApiCall(result.value)
                    } else {
                        state.value = State.ERROR
                        mCallback.onFailureGetCashDepositDetailsApiCall(result.value.message!!)
                    }
                }
                is ApiResult.GenericError -> {
                    command.postValue(result.error?.let {
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

    fun saveCashDepositDetails(
        cashDepositDetailsRequest: CashDepositDetailsRequest,
        mCallback: CashCloserFragmentCallback,
    ) {
        // var token = "h72genrSSNFivOi/cfiX3A=="
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var apiUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CD SAVECASHDEPOSITDETAILS")) {
                apiUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            state.postValue(State.SUCCESS)
            val result = withContext(Dispatchers.IO) {
                CashDepositApiRepo.saveCashDepositDetails(apiUrl, token, cashDepositDetailsRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.SUCCESS
                        mCallback.onSuccessSaveCashDepositDetailsApiCall(result.value)
                    } else {
                        state.value = State.ERROR
                        mCallback.onFailureSaveCashDepositDetailsApiCall(result.value.message!!)
                    }
                }
                is ApiResult.GenericError -> {
                    command.postValue(result.error?.let {
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