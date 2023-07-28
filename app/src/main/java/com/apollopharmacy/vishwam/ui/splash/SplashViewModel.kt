package com.apollopharmacy.vishwam.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.ValidateRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.repo.SplashRepo
import com.apollopharmacy.vishwam.util.AppConstants
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel : ViewModel() {

    val command = LiveEvent<Command>()
    val validateResponseMutableList = MutableLiveData<ValidateResponse>()

    fun getSplashScreenData(validateResponse: ValidateRequest) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                SplashRepo.getUserValidate(validateResponse)
            }
            when (response) {
                is ApiResult.Success -> {
                    if (response.value.status) {
                        validateResponseMutableList.value = response.value!!
                        Preferences.saveApi(Gson().toJson(response.value))
                        Preferences.saveGlobalResponse(Gson().toJson(response.value))

                        val url = Preferences.getApi()
                        val data = Gson().fromJson(url, ValidateResponse::class.java)
                        for (i in data.APIS.indices) {
                            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                                AppConstants.PROXY_URL = data.APIS[i].URL
                                AppConstants.PROXY_TOKEN = data.APIS[i].TOKEN
                                break
                            }
                        }
                        Utils.printMessage("SplashScreen", response.value.toString())
                        command.value = Command.NavigateTo(response.value)
                        println(39)
                    } else {
                        command.value = Command.ShowToast(response.value.message)
                    }
                }

                is ApiResult.GenericError -> {
                    command.postValue(Command.ShowToast(response.error ?: "Something went wrong"))
                }

                is ApiResult.NetworkError -> {
                    command.postValue(Command.ShowToast("Network Error"))
                }

                is ApiResult.UnknownError -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                }

                is ApiResult.UnknownHostException -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                }

                else -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                }
            }
        }
    }

//    fun getSplashScreenData(validateResponse: ValidateRequest) {
//        viewModelScope.launch {
//            val response = withContext(Dispatchers.IO) {
//                SplashRepo.getUserValidateTest()
//            }
//            when (response) {
//                is ApiResult.Success -> {
//                    if (response.value.status) {
//                        validateResponseMutableList.value = response.value
//                        Preferences.saveApi(Gson().toJson(response.value))
//                        Preferences.saveGlobalResponse(Gson().toJson(response.value))
//                        Utils.printMessage("SplashScreen", response.value.toString())
//                        command.value = Command.NavigateTo(response.value)
//                    } else {
//                        command.value = Command.ShowToast(response.value.message)
//                    }
//                }
//
//                is ApiResult.GenericError -> {
//                    command.postValue(Command.ShowToast(response.error ?: "Something went wrong"))
//                }
//
//                is ApiResult.NetworkError -> {
//                    command.postValue(Command.ShowToast("Network Error"))
//                }
//
//                is ApiResult.UnknownError -> {
//                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                }
//
//                is ApiResult.UnknownHostException -> {
//                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                }
//
//                else -> {
//                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                }
//            }
//        }
//    }
}

sealed class Command {
    data class ShowToast(val message: String) : Command()
    data class NavigateTo(
        val value: ValidateResponse,
    ) : Command()
}