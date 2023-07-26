package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ChampsApiRepo
import com.apollopharmacy.vishwam.ui.home.champs.survey.fragment.NewSurveyCallback
import com.apollopharmacy.vishwam.ui.home.model.GetEmailAddressModelResponse
import com.apollopharmacy.vishwam.ui.home.champs.survey.fragment.NewSurveyViewModel
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SurveyDetailsViewModel:ViewModel() {
    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    var getEmailDetailsChamps = MutableLiveData<GetEmailAddressModelResponse>()
    fun getEmailDetailsChamps(surveyDetailsCallback: SurveyDetailsCallback) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getEmailDetailsChamps();
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status!!) {
                        state.value = State.ERROR
                        surveyDetailsCallback.onSuccessgetEmailDetails(result.value)
//                        getStoreDetailsChamps.value = result.value
                    } else {
                        state.value = State.ERROR
                        surveyDetailsCallback.onFailuregetEmailDetails(result.value)
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

    fun getStoreWiseDetailsEmpIdChampsApi(newSurveyCallback: SurveyDetailsCallback, empId: String) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getStoreWiseDetailsChampsApi(empId)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status) {
                        state.value = State.ERROR
                        newSurveyCallback.onSuccessgetStoreWiseDetails(result.value)
                    } else {
                        state.value = State.ERROR
                        commands.value = Command.ShowToast(result.value.message)
                        newSurveyCallback.onFailuregetStoreWiseDetails(result.value)
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

    fun getEmailDetailsChampsApi(surveyDetailsCallback: SurveyDetailsCallback, type: String) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getEmailDetailsChampsApi(type)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status!!) {
                        state.value = State.ERROR
                        if(type.equals("RECIPIENTS")){
                            surveyDetailsCallback.onSuccessgetEmailDetails(result.value)
                        }else{
                            surveyDetailsCallback.onSuccessgetEmailDetailsCC(result.value)
                        }

                    } else {
                        state.value = State.ERROR
                        commands.value = Command.ShowToast(result.value.message!!)
                        surveyDetailsCallback.onFailuregetEmailDetails(result.value)
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