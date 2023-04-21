package com.apollopharmacy.vishwam.ui.home.apna.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApnaSurveyApiRepo
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.LocationListResponse
import com.apollopharmacy.vishwam.ui.login.Command
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApnaNewSurveyViewModel : ViewModel() {

    val locationList = MutableLiveData<LocationListResponse>()
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()

    fun getLocationList(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                break
            }
        }
        var apnaSurveyUrl =
            "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/location/list/location-list-for-survey"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                ApnaSurveyApiRepo.getLocationList(apnaSurveyUrl)
            }
            when (response) {
                is ApiResult.Success -> {
                    if (response.value.success ?: null == true) {
                        state.value = State.SUCCESS
                        mCallBack.onSuccessGetLocationListApiCall(response.value)
                    } else {
                        state.value = State.ERROR
                        mCallBack.onFailureGetLocationListApiCall(response.value.message.toString())
                    }
                }
            }
        }
    }

    fun getTrafficStreetType(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                break
            }
        }
        var apnaSurveyUrl =
            "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/choose-data/traffic_street_types"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                ApnaSurveyApiRepo.getTrafficStreetType(apnaSurveyUrl)
            }
            when (response) {
                is ApiResult.Success -> {
                    if (response.value.success ?: null == true) {
                        state.value = State.SUCCESS
                       mCallBack.onSuccessGetTrafficStreetTypeApiCall(response.value)
                    } else {
                        state.value = State.ERROR
                        mCallBack.onFailureGetTrafficStreetTypeApiCall(response.value.success.toString())
                    }
                }
            }
        }
    }

    fun getTrafficGeneratorsType(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                break
            }
        }
        var apnaSurveyUrl =
            "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/choose-data/traffic_generators"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                ApnaSurveyApiRepo.getTrafficGeneratorsType(apnaSurveyUrl)
            }
            when (response) {
                is ApiResult.Success -> {
                    if (response.value.success ?: null == true) {
                        state.value = State.SUCCESS
                        mCallBack.onSuccessGetTrafficGeneratorsTypeApiCall(response.value)
                    } else {
                        state.value = State.ERROR
                        mCallBack.onFailureGetTrafficGeneratorsTypeApiCall(response.value.success.toString())
                    }
                }
            }
        }
    }

    fun getApartmentType(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                break
            }
        }
        var apnaSurveyUrl = "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/choose-data/apartment_type"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                ApnaSurveyApiRepo.getApartmentType(apnaSurveyUrl)
            }
            when (response) {
                is ApiResult.Success -> {
                    if (response.value.success ?: null == true) {
                        state.value = State.SUCCESS
                        mCallBack.onSuccessGetApartmentTypeApiCall(response.value)
                    } else {
                        state.value = State.ERROR
                        mCallBack.onFailureGetApartmentTypeApiCall(response.value.success.toString())
                    }
                }
            }
        }
    }

    fun getApnaSpeciality(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                break
            }
        }
        var apnaSurveyUrl = "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/choose-data/apna_speciality"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                ApnaSurveyApiRepo.getApnaSpeciality(apnaSurveyUrl)
            }
            when (response) {
                is ApiResult.Success -> {
                    if (response.value.success ?: null == true) {
                        state.value = State.SUCCESS
                        mCallBack.onSuccessGetApnaSpecialityApiCall(response.value)
                    } else {
                        state.value = State.ERROR
                       mCallBack.onFailureGetApnaSpecialityApiCall(response.value.success.toString())
                    }
                }
            }
        }
    }

    fun getParkingType(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                break
            }
        }
        var apnaSurveyUrl = "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/choose-data/YesNo"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                ApnaSurveyApiRepo.getParkingType(apnaSurveyUrl)
            }
            when (response) {
                is ApiResult.Success -> {
                    if (response.value.success ?: null == true) {
                        state.value = State.SUCCESS
                        mCallBack.onSuccessGetParkingTypeApiCall(response.value)
                    } else {
                        state.value = State.ERROR
                        mCallBack.onFailureGetParkingTypeApiCall(response.value.success.toString())
                    }
                }
            }
        }
    }
}