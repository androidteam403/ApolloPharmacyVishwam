package com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ChampsApiRepo
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.ChampsSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.ChampsSurveyViewModel
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GetSurveyDetailsListViewModel:ViewModel() {
    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    fun getSurveyListApi(
        getSurveyDetailsListCallback: GetSurveyDetailsListCallback,
        startDate: String,
        endDate: String,
        id: String
    ) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getSurveyDetailsApi(startDate, endDate, id);
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status!!) {
                        state.value = State.ERROR
                        getSurveyDetailsListCallback.onSuccessSurveyList(result.value)
//                        getStoreDetailsChamps.value = result.value
                    } else {
                        state.value = State.ERROR
                        getSurveyDetailsListCallback.onFailureSurveyList(result.value)
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