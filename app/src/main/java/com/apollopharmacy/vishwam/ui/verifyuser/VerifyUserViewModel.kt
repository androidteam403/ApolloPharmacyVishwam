package com.apollopharmacy.vishwam.ui.verifyuser

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.repo.SplashRepo
import com.apollopharmacy.vishwam.util.AppConstants.PROXY_TOKEN
import com.apollopharmacy.vishwam.util.AppConstants.PROXY_URL
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VerifyUserViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    var command = LiveEvent<com.apollopharmacy.vishwam.ui.login.Command>()
    val validateResponseMutableList = MutableLiveData<ValidateResponse>()
    fun getSplashScreenData(validateResponse: ValidateRequest, context: Context) {
        Utlis.showLoading(context)
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                SplashRepo.getUserValidate(validateResponse)
            }
            when (response) {
                is ApiResult.Success -> {
                    Utlis.hideLoading()
                    if (response.value.status) {
                        validateResponseMutableList.value = response.value!!
                        Preferences.saveApi(Gson().toJson(response.value))
                        Preferences.saveGlobalResponse(Gson().toJson(response.value))
                        val url = Preferences.getApi()
                        val data = Gson().fromJson(url, ValidateResponse::class.java)
                        for (i in data.APIS.indices) {
                            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                                PROXY_URL = data.APIS[i].URL
                                PROXY_TOKEN = data.APIS[i].TOKEN
                                break
                            }
                        }
                        Utils.printMessage("SplashScreen", response.value.toString())
//                        command.value =
//                            NavigateTo(response.value)
                    } else {
                        command.value =
                            com.apollopharmacy.vishwam.ui.login.Command.ShowToast(response.value.message)
                    }
                }

                is ApiResult.GenericError -> {
                    command.postValue(
                        com.apollopharmacy.vishwam.ui.login.Command.ShowToast(
                            response.error ?: "Something went wrong"
                        )
                    )
                }

                is ApiResult.NetworkError -> {
                    command.postValue(com.apollopharmacy.vishwam.ui.login.Command.ShowToast("Network Error"))
                }

                is ApiResult.UnknownError -> {
                    command.postValue(com.apollopharmacy.vishwam.ui.login.Command.ShowToast("Something went wrong, please try again later"))
                }

                is ApiResult.UnknownHostException -> {
                    command.postValue(com.apollopharmacy.vishwam.ui.login.Command.ShowToast("Something went wrong, please try again later"))
                }

                else -> {
                    command.postValue(com.apollopharmacy.vishwam.ui.login.Command.ShowToast("Something went wrong, please try again later"))
                }
            }
        }
    }

}

sealed class Command {
    data class ShowToast(val message: String) : Command()
}