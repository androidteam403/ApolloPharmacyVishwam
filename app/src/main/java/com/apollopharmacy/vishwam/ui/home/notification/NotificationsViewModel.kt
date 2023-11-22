package com.apollopharmacy.vishwam.ui.home.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApnaRectroApiRepo
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationsViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    var command = LiveEvent<CmsCommand>()
    fun getNotificationDetailsApi(
        notificationsActivityCallback: NotificationsActivityCallback,
        page: Int,
        rows: Int
    ) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = "https://apis.v35.apollodev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/vishwam_notifications/list/vishwam-notifications-list-for-mobile?page=${page}&rows=${rows}&zcFetchListTotal=true"
        var token = ""
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("RT PENDING APPROVED LIST")) {
//                baseUrl = data.APIS[i].URL
//                token = data.APIS[i].TOKEN
//                break
//            }
//        }
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                ApnaRectroApiRepo.getNotificationList(
                    baseUrl
                )

            }
            when (response) {

                is ApiResult.Success -> {


                    if (response.value.success ?: null == true) {
                        state.value = State.ERROR
                        notificationsActivityCallback.onSuccessNotificationDetails(response.value)
                    } else {
                        state.value = State.ERROR
                        notificationsActivityCallback.onFailureNotificationDetails(response.value)
                    }
                }

                is ApiResult.GenericError -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    command.postValue(response.error?.let {
                        CmsCommand.ShowToast(it)
                    })
                    state.value = State.ERROR
                }

                is ApiResult.NetworkError -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    command.postValue(CmsCommand.ShowToast("Network Error"))
                    state.value = State.ERROR

                }

                is ApiResult.UnknownError -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    command.postValue(CmsCommand.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR

                }

                else -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    command.postValue(CmsCommand.ShowToast("Something went wrong, please try again later"))

                    state.value = State.ERROR
                }
            }
        }

    }
    sealed class CmsCommand {
        data class ShowToast(val message: String) : CmsCommand()
    }

}