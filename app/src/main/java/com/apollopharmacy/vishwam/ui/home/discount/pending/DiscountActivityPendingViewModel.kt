package com.apollopharmacy.vishwam.ui.home.discount.pending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.discount.AcceptOrRejectDiscountOrder
import com.apollopharmacy.vishwam.data.model.discount.GetDiscountColorResponse
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.data.model.discount.SimpleResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.discount.PendingRepo
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscountActivityPendingViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()
    val acceptRequest = MutableLiveData<SimpleResponse>()




    fun callAcceptOrder(acceptOrRejectDiscountOrder: AcceptOrRejectDiscountOrder) {
        viewModelScope.launch {
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("DISCOUNT ACCEPT AND REJECT")) {
                    val loginUrl = data.APIS[i].URL
                    val result = withContext(Dispatchers.IO) {
                        Utils.printMessage(
                            "PendingRepo",
                            "Acpt Rej Arr : " + acceptOrRejectDiscountOrder.toString()
                        )
                        PendingRepo.acceptTheDiscount(acceptOrRejectDiscountOrder, loginUrl)
                    }
                    when (result) {
                        is ApiResult.Success -> {
                            Utils.printMessage("AcceptOrder", result.toString())
                            acceptRequest.value = result.value!!
//                            getPendingList(false)
                        }

                        is ApiResult.GenericError -> {
                            command.postValue(Command.ShowToast("Network Error"))
                        }

                        is ApiResult.UnknownError -> {
                            command.postValue(Command.ShowToast("Something Went wrong"))
                            state.value = State.ERROR
                        }

                        is ApiResult.NetworkError -> {
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


}