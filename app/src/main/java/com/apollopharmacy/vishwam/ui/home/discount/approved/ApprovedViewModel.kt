package com.apollopharmacy.vishwam.ui.home.discount.approved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.discount.ApprovalOrderRequest
import com.apollopharmacy.vishwam.data.model.discount.FilterData
import com.apollopharmacy.vishwam.data.model.discount.FilterDiscountRequest
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.discount.ApprovedRepo
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApprovedViewModel : ViewModel() {

    val approvedList = MutableLiveData<ArrayList<ApprovalOrderRequest.APPROVEDLISTItem>>()
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()

    fun getFilterApprovedList(filterDiscReq: FilterDiscountRequest) {
        state.value = State.LOADING
        viewModelScope.launch {
            Utils.printMessage("ApproveViewModel", "Req: " + filterDiscReq.toString())
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("DISCOUNT PENDING AND ACCEPT LIST WITH FILTERS")) {
                    val loginUrl = data.APIS[i].URL
                    val response = withContext(Dispatchers.IO) {
                        ApprovedRepo.getFilteredList(loginUrl, filterDiscReq)
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            if (response.value.STATUS) {
                                approvedList.value = response.value.APPROVEDLIST
                                state.value = State.SUCCESS
                            } else {
                                state.value = State.ERROR
                                command.value =
                                    response.value.MESSAGE?.let { Command.ShowToast(it) }
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
                            command.postValue(Command.ShowToast(response.error
                                ?: "Something went wrong"))
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

    fun filterMenuData(): ArrayList<FilterData> {
        return arrayListOf(
            FilterData("Store ID"),
            FilterData("DC Code"),
            FilterData("Order ID"),
            FilterData("Item Name"),
            FilterData("Item ID")
        )
    }
}