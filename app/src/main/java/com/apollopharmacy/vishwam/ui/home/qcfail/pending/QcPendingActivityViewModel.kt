package com.apollopharmacy.vishwam.ui.home.qcfail.pending

import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.QcApiRepo
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import com.apollopharmacy.vishwam.ui.login.Command
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QcPendingActivityViewModel : ViewModel() {
    val qcReasonLists = MutableLiveData<QcReasonList>()
    var qcRejectLists: ArrayList<QcReasonList.Remarks>? = null
    val qcAcceptRejectRequestList = MutableLiveData<QcAcceptRejectResponse>()

    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()
    fun getQcRejectionList() {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("QC REMARKS LIST")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            state.postValue(State.SUCCESS)
            val result = withContext(Dispatchers.IO) {
                QcApiRepo.getQcRejectionList(baseUrl)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        qcReasonLists.value = result.value!!
                        qcRejectLists = result.value.remarkslist as ArrayList<QcReasonList.Remarks>?

                    } else {
                        state.value = State.ERROR
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

    fun getAcceptRejectResult(qcAcceptRejectRequest: QcAcceptRejectRequest,pendingActivityCallback: PendingActivityCallback,toastMsg:String) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("QC ACCEPT AND REJECT")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                QcApiRepo.qcAcceptReject(baseUrl, qcAcceptRejectRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                        pendingActivityCallback.onSuccessSaveAccept(result.value)


                    } else {

                        state.value = State.ERROR
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
    fun getReasons(): ArrayList<QcReasonList.Remarks> {

        var names = ArrayList<QcReasonList.Remarks>()
        if (qcRejectLists.isNullOrEmpty()) {
            Toast.makeText(context, "Reject Reasons not available", Toast.LENGTH_LONG)
        } else {
            names = qcRejectLists!!

        }







        return names

    }

}
