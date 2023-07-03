package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ChampsApiRepo
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.SaveSurveyModelRequest
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChampsSurveyViewModel: ViewModel() {
    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    var getCategoryDetailsChamps = MutableLiveData<GetCategoryDetailsModelResponse>()

    fun getCategoryDetailsChamps(champsSurveyCallBack: ChampsSurveyCallBack) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getCategoryDetailsChamps();
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status!!) {
                        state.value = State.ERROR
                        champsSurveyCallBack.onSuccessgetCategoryDetails(result.value)
//                        getStoreDetailsChamps.value = result.value
                    } else {
                        state.value = State.ERROR
                        champsSurveyCallBack.onFailuregetCategoryDetails(result.value)
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

    fun getTrainingAndColorDetails(champsSurveyCallBack: ChampsSurveyCallBack, type:String) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getTraingAndColorDetails();
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status) {
                        state.value = State.ERROR
                        if(type.equals("TECH")){
                            champsSurveyCallBack.onSuccessgetTrainingDetails(result.value)
                        }else{
                            champsSurveyCallBack.onSuccessgetColorDetails(result.value)
                        }

//                        getStoreDetailsChamps.value = result.value
                    } else {
                        state.value = State.ERROR
                        champsSurveyCallBack.onFailuregetTrainingDetails(result.value)
                        commands.value = Command.ShowToast(result.value.message)
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

    fun getSurveyList(champsSurveyCallBack: ChampsSurveyCallBack) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getSurveyDetails();
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status!!) {
                        state.value = State.ERROR
                        champsSurveyCallBack.onSuccessSurveyList(result.value)
//                        getStoreDetailsChamps.value = result.value
                    } else {
                        state.value = State.ERROR
                        champsSurveyCallBack.onFailureSurveyList(result.value)
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



    fun getSurveyListByChampsID(champsSurveyCallBack: ChampsSurveyCallBack) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getSurveyDetailsByChampsId();
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status!!) {
                        state.value = State.ERROR
                        champsSurveyCallBack.onSuccessGetSurveyDetailsByChampsId(result.value)
//                        getStoreDetailsChamps.value = result.value
                    } else {
                        state.value = State.ERROR
                        champsSurveyCallBack.onFailureGetSurveyDetailsByChampsId(result.value)
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
//-------------------------------------------------------------------------------------------------------

    fun getCategoryDetailsChampsApi(champsSurveyCallBack: ChampsSurveyCallBack) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getCategoryDetailsChampsApi()
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status!!) {
                        state.value = State.ERROR
                        champsSurveyCallBack.onSuccessgetCategoryDetails(result.value)
                    } else {
                        state.value = State.ERROR
                        champsSurveyCallBack.onFailuregetCategoryDetails(result.value)
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

    fun getSurveyListApi(
        champsSurveyCallBack: ChampsSurveyCallBack,
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
                        champsSurveyCallBack.onSuccessSurveyList(result.value)
//                        getStoreDetailsChamps.value = result.value
                    } else {
                        state.value = State.ERROR
                        champsSurveyCallBack.onFailureSurveyList(result.value)
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

    fun getTrainingAndColorDetailsApi(champsSurveyCallBack: ChampsSurveyCallBack, categoryName: String) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getTraingAndColorDetailsApi(categoryName)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status) {
                        state.value = State.ERROR
                        if(categoryName.equals("TECH")){
                            champsSurveyCallBack.onSuccessgetTrainingDetails(result.value)
                        }else{
                            champsSurveyCallBack.onSuccessgetColorDetails(result.value)
                        }


                    } else {
                        state.value = State.ERROR
                        commands.value = Command.ShowToast(result.value.message)
                        champsSurveyCallBack.onFailuregetTrainingDetails(result.value)
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

    fun getSaveDetailsApi(
        saveSurveyModelRequest: SaveSurveyModelRequest,
        champsSurveyCallBack: ChampsSurveyCallBack,
        type: String
    ) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.saveChampsApi(Config.ATTENDANCE_API_HEADER, saveSurveyModelRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status) {
                        state.value = State.ERROR
                        champsSurveyCallBack.onSuccessSaveDetailsApi(result.value, type)
                    } else {
                        state.value = State.ERROR
                        champsSurveyCallBack.onFailureSaveDetailsApi(result.value)
                        commands.value = Command.ShowToast(result.value.message)
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



    @SuppressLint("SuspiciousIndentation")
    fun getSurveyListByChampsIDApi(champsSurveyCallBack: ChampsSurveyCallBack, champsId: String) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getSurveyDetailsByChampsApi(champsId)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status) {
                        state.value = State.ERROR

                            champsSurveyCallBack.onSuccessGetSurveyDetailsByChampsId(result.value)

                    } else {
                        state.value = State.ERROR
                        commands.value = Command.ShowToast(result.value.message)
                        champsSurveyCallBack.onFailureGetSurveyDetailsByChampsId(result.value)
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
    fun getSubCategoryDetailsChamps(champsSurveyCallBack: ChampsSurveyCallBack, categoryName: String) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getSubCategoryDetailsChamps();
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status!!) {
                        state.value = State.ERROR
                        champsSurveyCallBack.onSuccessgetSubCategoryDetails(
                            result.value,
                            categoryName
                        )
//                        getStoreDetailsChamps.value = result.value
                    } else {
                        state.value = State.ERROR
                        champsSurveyCallBack.onFailuregetSubCategoryDetails(result.value)
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

    fun getSubCategoryDetailsChampsApi(champsSurveyCallBack: ChampsSurveyCallBack, categoryName: String) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getSubCategoryDetailsChampsApi(categoryName)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status!!) {
                        state.value = State.ERROR
                        champsSurveyCallBack.onSuccessgetSubCategoryDetails(result.value, categoryName)

                    } else {
                        state.value = State.ERROR
                        commands.value = Command.ShowToast(result.value.message!!)
                        champsSurveyCallBack.onFailuregetSubCategoryDetails(result.value)
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