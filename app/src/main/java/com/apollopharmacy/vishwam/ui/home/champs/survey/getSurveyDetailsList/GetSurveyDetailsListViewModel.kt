package com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ChampsApiRepo
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.SurveyDetailsViewModel
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GetSurveyDetailsListViewModel:ViewModel() {
    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
//    fun getSurveyListApi(
//        getSurveyDetailsListCallback: GetSurveyDetailsListCallback,
//        startDate: String,
//        endDate: String,
//        id: String
//    ) {
//        state.postValue(State.LOADING)
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                ChampsApiRepo.getSurveyDetailsApi(startDate, endDate, id);
//            }
//            when (result) {
//                is ApiResult.Success -> {
//                    if (result.value.status!!) {
//                        state.value = State.ERROR
//                        getSurveyDetailsListCallback.onSuccessSurveyList(result.value)
////                        getStoreDetailsChamps.value = result.value
//                    } else {
//                        state.value = State.ERROR
//                        getSurveyDetailsListCallback.onFailureSurveyList(result.value)
//                        commands.value = Command.ShowToast(result.value.message!!)
//                    }
//                }
//                is ApiResult.GenericError -> {
//                    commands.postValue(result.error?.let {
//                        Command.ShowToast(it)
//                    })
//                    state.value = State.ERROR
//                }
//                is ApiResult.NetworkError -> {
//                    commands.postValue(Command.ShowToast("Network Error"))
//                    state.value = State.ERROR
//                }
//                is ApiResult.UnknownError -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//                else -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//            }
//        }
//    }

    fun getSurveyListApi(
        surveyDetailsCallback: GetSurveyDetailsListCallback,
        fromDate: String,
        toDate: String,
        validatedEmpId: String
    ) {
        state.postValue(State.LOADING)

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CHMP SURVEY DETAILS")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }


        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getSurveyDetailsApi(baseUrl, token,fromDate, toDate, validatedEmpId)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        surveyDetailsCallback.onSuccessSurveyList(result.value)
                    } else {
                        surveyDetailsCallback.onFailureSurveyList(result.value)
                        state.value = State.ERROR
                        commands.value = Command.ShowToast(result.value.message!!)
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
    sealed class Command {
        data class ShowToast(val message: String) : Command()
    }
}