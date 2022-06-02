package com.apollopharmacy.vishwam.ui.home.discount.rejected

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.discount.FilterDiscountRequest
import com.apollopharmacy.vishwam.data.model.discount.PendingOrderRequest
import com.apollopharmacy.vishwam.data.model.discount.RejectedOrderResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.discount.RejectedRepo
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RejectedViewModel : ViewModel() {

    val command = LiveEvent<Command>()
    val rejectedList = MutableLiveData<List<RejectedOrderResponse.REJECTEDLISTItem>>()
    val state = MutableLiveData<State>()

    fun getRejectedData() {
        state.value = State.LOADING
        viewModelScope.launch {
            val getData = Preferences.getApi()
            val data = Gson().fromJson(getData, ValidateResponse::class.java)
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("DISCOUNT PENDING AND ACCEPTED LIST")) {
                    val loginUrl = data.APIS[i].URL
                    val result = withContext(Dispatchers.IO) {
                        RejectedRepo.getRejectedList(
                            PendingOrderRequest(Preferences.getToken()), loginUrl)
                    }
                    when (result) {
                        is ApiResult.Success -> {
                            if (result.value.STATUS) {
                                Utils.printMessage("data", result.value.REJECTEDLIST.toString())
                                rejectedList.postValue(result.value.REJECTEDLIST)
                                state.value = State.ERROR
                            } else {
                                command.value = result.value.MESSAGE?.let { Command.ShowToast(it) }
                                state.value = State.ERROR
                            }
                        }
                        is ApiResult.NetworkError -> {
                            command.postValue(Command.ShowToast("Network Error"))
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            command.postValue(
                                Command.ShowToast(
                                    result.error ?: "Something went wrong"
                                )
                            )
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
    }

    fun getFilterApprovedList(filterDiscReq: FilterDiscountRequest) {
        state.value = State.LOADING
        viewModelScope.launch {
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("DISCOUNT PENDING AND ACCEPT LIST WITH FILTERS")) {
                    val loginUrl = data.APIS[i].URL
                    Utils.printMessage("RejViewModel", "Req: " + filterDiscReq.toString())
                    val response = withContext(Dispatchers.IO) {
                        RejectedRepo.getFilteredList(loginUrl, filterDiscReq)
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            if (response.value.STATUS) {
                                Utils.printMessage("data", response.value.REJECTEDLIST.toString())
                                rejectedList.postValue(response.value.REJECTEDLIST)
                                state.value = State.ERROR
                            } else {
                                command.value =
                                    response.value.MESSAGE?.let { Command.ShowToast(it) }
                                state.value = State.ERROR
                            }
                        }
                        is ApiResult.UnknownError -> {
                            command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            command.postValue(Command.ShowToast("Network Error"))
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            command.postValue(Command.ShowToast(response.error ?: "Error"))
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
    }
}