package com.apollopharmacy.vishwam.ui.home.SwachhApollo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.SwachhListApiResponse
import com.apollopharmacy.vishwam.ui.createpin.Command
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model.*
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SwachhListViewModel : ViewModel() {
    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    var swacchList = MutableLiveData<ApproveRejectListResponse>()
    var swacchImagesList = MutableLiveData<LineImagesResponse>()
    var swachhAproveList = MutableLiveData<ApproveResponse>()

    lateinit var uniqueCategoryList : List<ApproveRejectListResponse>

    fun getSwachhList(approveRejectListRequest: ApproveRejectListRequest) {
        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                SwachhListApiResponse.getSwacchList(approveRejectListRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status?:null == true) {
                        state.value = State.ERROR
                        swacchList.value= result.value
                    }

                    else {
                        state.value = State.ERROR
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        result.error?.let {
                            Command.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }







    fun getApproveList(approveRequest: ArrayList<ApproveRequest>) {
        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                SwachhListApiResponse.getApproveList(approveRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status?:null == true) {
                        state.value = State.ERROR
                        swachhAproveList.value= result.value
                    }

                    else {
                        state.value = State.ERROR
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        result.error?.let {
                            Command.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }




















    fun getImagesList(lineImagesRequest: LineImagesRequest) {
        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                SwachhListApiResponse.getLineImagesList(lineImagesRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status?:null == true) {
                        state.value = State.ERROR
                        swacchImagesList.value= result.value
                    }

                    else {
                        state.value = State.ERROR
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        result.error?.let {
                            Command.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }




}