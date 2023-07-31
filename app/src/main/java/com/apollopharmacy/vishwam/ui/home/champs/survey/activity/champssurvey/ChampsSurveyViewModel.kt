package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ChampsApiRepo
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.SaveUpdateRequest
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.SaveUpdateResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.SaveSurveyModelRequest
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.CommandsNewSwachImp
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChampsSurveyViewModel : ViewModel() {
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

    fun getTrainingAndColorDetails(champsSurveyCallBack: ChampsSurveyCallBack, type: String) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getTraingAndColorDetails();
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status) {
                        state.value = State.ERROR
                        if (type.equals("TECH")) {
                            champsSurveyCallBack.onSuccessgetTrainingDetails(result.value)
                        } else {
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

//    fun getCategoryDetailsChampsApi(champsSurveyCallBack: ChampsSurveyCallBack) {
//        state.postValue(State.LOADING)
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                ChampsApiRepo.getCategoryDetailsChampsApi()
//            }
//            when (result) {
//                is ApiResult.Success -> {
//                    if (result.value.status!!) {
//                        state.value = State.ERROR
//                        champsSurveyCallBack.onSuccessgetCategoryDetails(result.value)
//                    } else {
//                        state.value = State.ERROR
//                        champsSurveyCallBack.onFailuregetCategoryDetails(result.value)
//                        commands.value = Command.ShowToast(result.value.message!!)
//                    }
//                }
//
//                is ApiResult.GenericError -> {
//                    commands.postValue(result.error?.let {
//                        Command.ShowToast(it)
//                    })
//                    state.value = State.ERROR
//                }
//
//                is ApiResult.NetworkError -> {
//                    commands.postValue(Command.ShowToast("Network Error"))
//                    state.value = State.ERROR
//                }
//
//                is ApiResult.UnknownError -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//
//                else -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//            }
//        }
//    }

    //    fun getCategoryDetailsChampsApi(champsSurveyCallBack: ChampsSurveyCallBack) {
//        state.postValue(State.LOADING)
//
//        val url = Preferences.getApi()
//        val data = Gson().fromJson(url, ValidateResponse::class.java)
//        var baseUrl = ""
//        var token = ""
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("CHMP GET CATEGORIES LIST")) {
//                baseUrl = data.APIS[i].URL
//                token = data.APIS[i].TOKEN
//                break
//            }
//        }
//
//
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                ChampsApiRepo.getCategoryDetailsApiCall(token,baseUrl)
//            }
//            when (result) {
//                is ApiResult.Success -> {
//                    if (result.value.status ?: null == true && result.value.message == "SUCCESS") {
//                        champsSurveyCallBack.onSuccessgetCategoryDetails(result.value)
//
////                        checkDayWiseAccess.value = result.value
//                    } else {
//                        state.value = State.ERROR
//                        commands.value = Command.ShowToast(result.value.message!!)
//                        champsSurveyCallBack.onFailuregetCategoryDetails(result.value)
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
    fun getCategoryDetailsChampsApi(champsSurveyCallBack: ChampsSurveyCallBack) {
        state.postValue(State.LOADING)

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CHMP GET CATEGORIES LIST")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }


        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getCategoryDetailsApiCall(baseUrl, token)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        champsSurveyCallBack.onSuccessgetCategoryDetails(result.value)
                    } else {
                        champsSurveyCallBack.onFailuregetCategoryDetails(result.value)
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

    fun getSubCategoryDetailsChampsApi(
        champsSurveyCallBack: ChampsSurveyCallBack,
        categoryName: String,
    ) {
        state.postValue(State.LOADING)

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CHMP GET SUB CATEGORY DETAILS")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }


        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getSubCategoryDetailsApiCall(baseUrl, token, categoryName)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        champsSurveyCallBack.onSuccessgetSubCategoryDetails(
                            result.value,
                            categoryName
                        )
                    } else {
                        champsSurveyCallBack.onFailuregetSubCategoryDetails(result.value)
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
//    fun getSubCategoryDetailsChampsApi(
//        champsSurveyCallBack: ChampsSurveyCallBack,
//        categoryName: String,
//    ) {
//        state.postValue(State.LOADING)
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                ChampsApiRepo.getSubCategoryDetailsChampsApi(categoryName)
//            }
//            when (result) {
//                is ApiResult.Success -> {
//                    if (result.value.status!!) {
//                        state.value = State.ERROR
//                        champsSurveyCallBack.onSuccessgetSubCategoryDetails(
//                            result.value,
//                            categoryName
//                        )
//
//                    } else {
//                        state.value = State.ERROR
//                        commands.value = Command.ShowToast(result.value.message!!)
//                        champsSurveyCallBack.onFailuregetSubCategoryDetails(result.value)
//                    }
//                }
//
//                is ApiResult.GenericError -> {
//                    commands.postValue(result.error?.let {
//                        Command.ShowToast(it)
//                    })
//                    state.value = State.ERROR
//                }
//
//                is ApiResult.NetworkError -> {
//                    commands.postValue(Command.ShowToast("Network Error"))
//                    state.value = State.ERROR
//                }
//
//                is ApiResult.UnknownError -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//
//                else -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//            }
//        }
//    }

//    fun getSurveyListApi(
//        champsSurveyCallBack: ChampsSurveyCallBack,
//        startDate: String,
//        endDate: String,
//        id: String,
//    ) {
//        state.postValue(State.LOADING)
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                ChampsApiRepo.getSurveyDetailsApi(startDate, endDate, id, toDate, validatedEmpId);
//            }
//            when (result) {
//                is ApiResult.Success -> {
//                    if (result.value.status!!) {
//                        state.value = State.ERROR
//                        champsSurveyCallBack.onSuccessSurveyList(result.value)
////                        getStoreDetailsChamps.value = result.value
//                    } else {
//                        state.value = State.ERROR
//                        champsSurveyCallBack.onFailureSurveyList(result.value)
//                        commands.value = Command.ShowToast(result.value.message!!)
//                    }
//                }
//
//                is ApiResult.GenericError -> {
//                    commands.postValue(result.error?.let {
//                        Command.ShowToast(it)
//                    })
//                    state.value = State.ERROR
//                }
//
//                is ApiResult.NetworkError -> {
//                    commands.postValue(Command.ShowToast("Network Error"))
//                    state.value = State.ERROR
//                }
//
//                is ApiResult.UnknownError -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//
//                else -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//            }
//        }
//    }

    fun getTrainingAndColorDetailsApi(champsSurveyCallBack: ChampsSurveyCallBack, type: String) {
        state.postValue(State.LOADING)

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CHMP GET TRAINING AND COLOR DETAILS")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }


        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getTraingAndColorDetailsApi(baseUrl, token, type)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        if (type.equals("TECH")) {
                            champsSurveyCallBack.onSuccessgetTrainingDetails(result.value)
                        } else {
                            champsSurveyCallBack.onSuccessgetColorDetails(result.value)
                        }
                    } else {
                        champsSurveyCallBack.onFailuregetTrainingDetails(result.value)
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

    fun getSaveDetailsApi(
        saveSurveyModelRequest: SaveSurveyModelRequest,
        champsSurveyCallBack: ChampsSurveyCallBack,
        type: String,
    ) {
        state.postValue(State.LOADING)

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CHMP SAVE CHAMPS SURVEY")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }


        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.saveChampsApi(baseUrl, token, saveSurveyModelRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        champsSurveyCallBack.onSuccessSaveDetailsApi(result.value, type)
                    } else {
                        champsSurveyCallBack.onFailureSaveDetailsApi(result.value)
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

//    fun getSaveDetailsApi(
//        saveSurveyModelRequest: SaveSurveyModelRequest,
//        champsSurveyCallBack: ChampsSurveyCallBack,
//        type: String,
//    ) {
//        state.postValue(State.LOADING)
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                ChampsApiRepo.saveChampsApi(Config.ATTENDANCE_API_HEADER, saveSurveyModelRequest)
//            }
//            when (result) {
//                is ApiResult.Success -> {
//                    if (result.value.status) {
//                        state.value = State.ERROR
//                        champsSurveyCallBack.onSuccessSaveDetailsApi(result.value, type)
//                    } else {
//                        state.value = State.ERROR
//                        champsSurveyCallBack.onFailureSaveDetailsApi(result.value)
//                        commands.value = Command.ShowToast(result.value.message)
//                    }
//                }
//
//                is ApiResult.GenericError -> {
//                    commands.postValue(result.error?.let {
//                        Command.ShowToast(it)
//                    })
//                    state.value = State.ERROR
//                }
//
//                is ApiResult.NetworkError -> {
//                    commands.postValue(Command.ShowToast("Network Error"))
//                    state.value = State.ERROR
//                }
//
//                is ApiResult.UnknownError -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//
//                else -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//            }
//        }
//    }

    fun getSurveyListByChampsIDApi(champsSurveyCallBack: ChampsSurveyCallBack, champsId: String) {
        state.postValue(State.LOADING)

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CHMP GET SURVEY LIST BY CHAMPID")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }


        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getSurveyDetailsByChampsApi(baseUrl, token, champsId)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        champsSurveyCallBack.onSuccessGetSurveyDetailsByChampsId(result.value)
                    } else {
                        champsSurveyCallBack.onFailureGetSurveyDetailsByChampsId(result.value)
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
//    @SuppressLint("SuspiciousIndentation")
//    fun getSurveyListByChampsIDApi(champsSurveyCallBack: ChampsSurveyCallBack, champsId: String) {
//        state.postValue(State.LOADING)
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                ChampsApiRepo.getSurveyDetailsByChampsApi(champsId)
//            }
//            when (result) {
//                is ApiResult.Success -> {
//                    if (result.value.status) {
//                        state.value = State.ERROR
//
//                        champsSurveyCallBack.onSuccessGetSurveyDetailsByChampsId(result.value)
//
//                    } else {
//                        state.value = State.ERROR
//                        commands.value = Command.ShowToast(result.value.message)
//                        champsSurveyCallBack.onFailureGetSurveyDetailsByChampsId(result.value)
//                    }
//                }
//
//                is ApiResult.GenericError -> {
//                    commands.postValue(result.error?.let {
//                        Command.ShowToast(it)
//                    })
//                    state.value = State.ERROR
//                }
//
//                is ApiResult.NetworkError -> {
//                    commands.postValue(Command.ShowToast("Network Error"))
//                    state.value = State.ERROR
//                }
//
//                is ApiResult.UnknownError -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//
//                else -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//            }
//        }
//    }

    fun getSubCategoryDetailsChamps(
        champsSurveyCallBack: ChampsSurveyCallBack,
        categoryName: String,
    ) {
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

    fun saveUpdateApi(
        champsSurveyCallBack: ChampsSurveyCallBack,
        saveUpdateRequest: SaveUpdateRequest,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)

        var baseUrLProxy = ""
        var tokenProxy = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrLProxy = data.APIS[i].URL
                tokenProxy = data.APIS[i].TOKEN
                break
            }
        }


        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("SW SAVE IMAGE URLS")) {
            baseUrl =
                "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/cms_champs_survey/save-update"//"https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/cms_champs_survey/save-update"
//                token = data.APIS[i].TOKEN
            break
//            }
        }
        val saveUpdateRequestJson = Gson().toJson(saveUpdateRequest)
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrLProxy,
                    tokenProxy,
                    GetDetailsRequest(baseUrl, "POST", saveUpdateRequestJson, "", "")
                )
//                ChampsApiRepo.saveUpdateApi(baseUrl, saveUpdateRequest)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val saveUpdateRequestJsonResponse =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    SaveUpdateResponse::class.java
                                )
                            champsSurveyCallBack.onSuccessSaveUpdateApi(
                                saveUpdateRequestJsonResponse
                            )
                            if (saveUpdateRequestJsonResponse.success ?: null == false) {
                                state.value = State.ERROR
                                CommandsNewSwachImp.ShowToast(saveUpdateRequestJsonResponse.message)
                                champsSurveyCallBack.onFailureSaveUpdateApi(
                                    saveUpdateRequestJsonResponse
                                )
                            }
                        }
                    }

                }

                is ApiResult.GenericError -> {
                    commands.postValue(response.error?.let {
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