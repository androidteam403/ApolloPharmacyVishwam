package com.apollopharmacy.vishwam.ui.login

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.LoginRequest
import com.apollopharmacy.vishwam.data.model.MPinRequest
import com.apollopharmacy.vishwam.data.model.MPinResponse
import com.apollopharmacy.vishwam.data.model.ValidateRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.data.repo.SplashRepo
import com.apollopharmacy.vishwam.util.AppConstants.PROXY_TOKEN
import com.apollopharmacy.vishwam.util.AppConstants.PROXY_URL
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    var command = LiveEvent<Command>()
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
                       /* for (i in response.value.APIS) {
                            var url = i.URL
                            url = url.replace(
                                "https://172.16.103.116",
                                "https://phrmaptestp.apollopharmacy.info"
                            )
                            i.URL = url
                        }*/
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
                            Command.ShowToast(response.value.message)
                    }
                }

                is ApiResult.GenericError -> {
                    command.postValue(
                        Command.ShowToast(
                            response.error ?: "Something went wrong"
                        )
                    )
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


    fun checkLogin(loginRequest: LoginRequest) {

        if (loginRequest.EMPID.isEmpty()) {
            commands.postValue(Command.ShowToast("Please Enter User ID"))
        } else if (loginRequest.PASSWORD.isEmpty()) {
            commands.postValue(Command.ShowToast("Please Enter Password"))
        } else if (loginRequest.COMPANY.isEmpty()) {
            commands.postValue(Command.ShowToast("Please Select company"))
        } else {
            state.postValue(State.LOADING)
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("DISCOUNT LOGIN")) {

//                    https://online.apollopharmacy.org/VISWAMUAT/Apollo/DiscountRequest/Login

                    val loginUrl = data.APIS[i].URL
                    viewModelScope.launch {
                        val result = withContext(Dispatchers.IO) {
                            LoginRepo.checkLoginUse(loginRequest, loginUrl)
                        }
                        when (result) {
                            is ApiResult.Success -> {
                                if (result.value.STATUS) {
                                    state.value = State.ERROR
                                    commands.postValue(Command.ShowToast("Successfully login"))
                                    Preferences.saveAppDesignation(result.value.APPLEVELDESIGNATION)
                                    if (!result.value.STOREDETAILS.isNullOrEmpty()) {
                                        Preferences.saveSiteId(result.value.STOREDETAILS.get(0).SITEID)

                                    }
                                    LoginRepo.saveProfile(result.value, loginRequest.PASSWORD)
                                    Preferences.savingToken(result.value.EMPID)
                                    Preferences.saveDesignation(result.value.DESIGNATION)
                                    Preferences.setAppLevelDesignation(result.value.APPLEVELDESIGNATION)
                                    Preferences.storeLoginJson(Gson().toJson(result.value))
                                    Preferences.setLoginDate(Utils.getCurrentDate())
                                    commands.value = Command.NavigateTo(result.value)
                                } else {
                                    state.value = State.ERROR
                                    commands.value = Command.ShowToast(result.value.MESSAGE)
                                }
                            }

                            is ApiResult.GenericError -> {
                                commands.postValue(result.error?.let {
                                    Command.ShowToast(it)
                                })
                                state.value = State.ERROR
                            }

                            is ApiResult.NetworkError -> {
                                commands.postValue(Command.ShowToast("Network Error"))
                                state.value = State.ERROR
                            }

                            is ApiResult.UnknownError -> {
                                commands.postValue(Command.ShowToast("An error has occurred"))
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
        }
    }

    fun checkMPinLogin(mPinRequest: MPinRequest) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("MPIN")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                LoginRepo.checkMPinDetails(baseUrl, token, mPinRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    commands.value = Command.MpinValidation(result.value)
                }

                is ApiResult.GenericError -> {
                    commands.postValue(result.error?.let {
                        Command.ShowToast(it)
                    })
                    state.value = State.ERROR
                }

                is ApiResult.NetworkError -> {
                    commands.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }

                is ApiResult.UnknownError -> {
                    commands.postValue(Command.ShowToast("An error has occurred"))
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

sealed class Command {
    data class ShowToast(val message: String) : Command()
    data class NavigateTo(
        val value: LoginDetails,
    ) : Command()

    data class MpinValidation(
        val value: MPinResponse,
    ) : Command()

    data class ShowButtonSheet(
        val fragment: Class<out BottomSheetDialogFragment>,
        val arguments: Bundle,
    ) : Command()

    data class ShowQcButtonSheet(
        val fragment: Class<out BottomSheetDialog>,
        val arguments: Bundle,
    ) : Command()

//    data class NavigateTo(
//        val value: ValidateResponse,
//    ) : Command()
}