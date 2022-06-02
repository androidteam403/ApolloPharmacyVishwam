package com.apollopharmacy.vishwam.ui.home.discount.pending

import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.discount.*
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.discount.PendingRepo
import com.apollopharmacy.vishwam.ui.home.discount.filter.FilterFragment
import com.apollopharmacy.vishwam.ui.home.discount.filter.FilterFragment.Companion.KEY_PENDING_DATA
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PendingViewModel : ViewModel() {
    val pendingList = MutableLiveData<ArrayList<PendingOrder.PENDINGLISTItem>>()
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()
    val acceptRequest = MutableLiveData<SimpleResponse>()
    val swipeStatus = LiveEvent<Boolean>()
    private var arrayList: List<PendingOrder.PENDINGLISTItem>? = null

    fun getPendingList(isSwipeRequired: Boolean) {
        if (isSwipeRequired) {
            swipeStatus.value = true
        }
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("DISCOUNT PENDING AND ACCEPTED LIST")) {
                val loginUrl = data.APIS[i].URL
                viewModelScope.launch {
                    val result = withContext(Dispatchers.IO) {
                        PendingRepo.getPendingOrderList(
                            PendingOrderRequest(Preferences.getToken()),
                            loginUrl
                        )
                    }
                    when (result) {
                        is ApiResult.Success -> {
                            if (result.value.STATUS) {
                                pendingList.value = result.value.PENDINGLIST
                                arrayList = result.value.PENDINGLIST
                                if (isSwipeRequired) {
                                    swipeStatus.value = false
                                }
                                Utils.printMessage("pending", result.value.toString())
                            } else {
                                if (isSwipeRequired) {
                                    swipeStatus.value = false
                                }
                                command.value = result.value.MESSAGE?.let { Command.ShowToast(it) }
                            }
                        }
                        is ApiResult.UnknownError -> {
                            command.postValue(Command.ShowToast("Api Error"))
                            if (isSwipeRequired) {
                                swipeStatus.value = false
                            }
                            command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            if (isSwipeRequired) {
                                swipeStatus.value = false
                            }
                            command.postValue(Command.ShowToast("Network Error"))
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            if (isSwipeRequired) {
                                swipeStatus.value = false
                            }
                            command.postValue(
                                Command.ShowToast(
                                    result.error!!
                                )
                            )
                            state.value = State.ERROR
                        }
                        else -> {
                            if (isSwipeRequired) {
                                swipeStatus.value = false
                            }
                            command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }

    fun filterClicked() {
        if (arrayList.isNullOrEmpty()) {
            Utils.printMessage("data is empty", "data is empty")
        } else {
            command.value = Command.ShowButtonSheet(
                FilterFragment::class.java, bundleOf(
                    Pair(KEY_PENDING_DATA, arrayList)
                )
            )
        }
    }

    fun callAcceptOrder(acceptOrRejectDiscountOrder: AcceptOrRejectDiscountOrder) {
        viewModelScope.launch {
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("DISCOUNT ACCEPT AND REJECT")) {
                    val loginUrl = data.APIS[i].URL
                    val result = withContext(Dispatchers.IO) {
                        Utils.printMessage("PendingRepo",
                            "Acpt Rej Arr : " + acceptOrRejectDiscountOrder.toString())
                        PendingRepo.acceptTheDiscount(acceptOrRejectDiscountOrder, loginUrl)
                    }
                    when (result) {
                        is ApiResult.Success -> {
                            Utils.printMessage("AcceptOrder", result.toString())
                            acceptRequest.value = result.value
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

    fun callBulkAcceptOrder(acceptRejItem: BulkAcceptOrRejectDiscountOrder) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("DISCOUNT ACCEPT AND REJECT FOR LIST")) {
                val loginUrl = data.APIS[i].URL
                viewModelScope.launch {
                    val url = Preferences.getApi()
                    var data = Gson().fromJson(url, ValidateResponse::class.java)
                    Utils.printMessage("PendingRepo", "Bulk Arr : " + acceptRejItem.toString())
                    val result = withContext(Dispatchers.IO) {
                        PendingRepo.bulkAcceptTheDiscount(loginUrl, acceptRejItem)
                    }
                    when (result) {
                        is ApiResult.Success -> {
                            Utils.printMessage("AcceptOrder", result.toString())
                            acceptRequest.value = result.value
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

    fun filterData(pendingListData: ArrayList<PendingOrder.PENDINGLISTItem>) {
        pendingList.value = pendingListData
    }
}