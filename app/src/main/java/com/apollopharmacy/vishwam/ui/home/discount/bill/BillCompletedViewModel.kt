package com.apollopharmacy.vishwam.ui.home.discount.bill

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.discount.BILLDEATILSItem
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.discount.BillRepo
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillCompletedViewModel : ViewModel() {

    val billArrayLiveData = MutableLiveData<ArrayList<BILLDEATILSItem>>()
    val command = LiveEvent<Command>()
    val state = MutableLiveData<State>()

    fun getBillDetailsApiCall() {
        state.value = State.LOADING
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("DISCOUNT BILLINGDETAILS")) {
                val billApi = data.APIS[i].URL
                viewModelScope.launch {
                    val billResponse = withContext(Dispatchers.IO) {
                        BillRepo.callBillListRepo(Preferences.getToken(), billApi)
                    }
                    when (billResponse) {
                        is ApiResult.Success -> {
                            Utils.printMessage("SuccessData", billResponse.value.toString())
                            billArrayLiveData.value = billResponse.value.bILLDEATILS
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            command.value = billResponse.error.let { Command.ShowToast(it!!) }
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
                        is ApiResult.UnknownHostException -> {
                            command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }
}