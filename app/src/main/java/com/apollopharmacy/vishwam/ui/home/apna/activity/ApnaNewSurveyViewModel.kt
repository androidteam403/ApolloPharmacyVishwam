package com.apollopharmacy.vishwam.ui.home.apna.activity

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.ApartmentTypeResponse
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.ApnaSpecialityResponse
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.CityListResponse
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.DimensionTypeResponse
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.LocationListResponse
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.NeighbouringLocationResponse
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.ParkingTypeResponse
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.RegionListResponse
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.StateListResponse
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateResponse
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.TrafficGeneratorsResponse
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.TrafficStreetTypeResponse
import com.apollopharmacy.vishwam.ui.home.apna.utils.ConnectApnaAzure
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URLEncoder

class ApnaNewSurveyViewModel : ViewModel() {
    // Dev base url : https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/
    val locationList = MutableLiveData<LocationListResponse>()
    val state = MutableLiveData<State>()
    val command = LiveEvent<CommandsNew>()

    fun getRegionList(mCallBack: ApnaNewSurveyCallBack, empId: String) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }

        var apnaSurveyUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/region/list/survey-region-for-select?"
        apnaSurveyUrl = apnaSurveyUrl + "emp_id=$empId"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL, token, GetDetailsRequest(apnaSurveyUrl, "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val regionResponse = Gson().fromJson(
                        BackShlash.removeSubString(res), RegionListResponse::class.java
                    )
                    if (regionResponse.success!!) {
                        mCallBack.onSuccessGetRegionListApiCall(regionResponse)
                    } else {
                        mCallBack.onFailureGetRegionListApiCall(regionResponse.message.toString())
                    }
                }

                else -> {}
            }
        }
    }

    fun getLocationList(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }

        val apnaSurveyUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/location/list/location-list-for-survey"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL, token, GetDetailsRequest(apnaSurveyUrl, "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val locationResponse = Gson().fromJson(
                        BackShlash.removeSubString(res), LocationListResponse::class.java
                    )
                    if (locationResponse.success!!) {
                        mCallBack.onSuccessGetLocationListApiCall(locationResponse)
                    } else {
                        mCallBack.onFailureGetLocationListApiCall(locationResponse.message.toString())
                    }
                }

                else -> {}
            }
        }
    }

    fun getDimensionType(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        val apnaSurveyUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/choose-data/dimension_type"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
//                ApnaSurveyApiRepo.getDimensionType(apnaSurveyUrl)
                RegistrationRepo.getDetails(
                    baseUrL, token, GetDetailsRequest(apnaSurveyUrl, "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val dimensionTypeResponse = Gson().fromJson(
                        BackShlash.removeSubString(res), DimensionTypeResponse::class.java
                    )
                    if (dimensionTypeResponse.success!!) {
                        mCallBack.onSuccessGetDimensionTypeApiCall(dimensionTypeResponse)
                    } else {
                        mCallBack.onFailureGetDimensionTypeApiCall(dimensionTypeResponse.success.toString())
                    }
//                    if (response.value.success ?: null == true) {
//                        state.value = State.SUCCESS
//                        mCallBack.onSuccessGetDimensionTypeApiCall(response.value)
//                    } else {
//                        state.value = State.ERROR
//                        mCallBack.onFailureGetDimensionTypeApiCall(response.value.success.toString())
//                    }
                }

                else -> {}
            }
        }
    }

    fun getParkingType(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        val apnaSurveyUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/choose-data/YesNo"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
//                ApnaSurveyApiRepo.getParkingType(apnaSurveyUrl)
                RegistrationRepo.getDetails(
                    baseUrL, token, GetDetailsRequest(apnaSurveyUrl, "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val parkingTypeResponse = Gson().fromJson(
                        BackShlash.removeSubString(res), ParkingTypeResponse::class.java
                    )
                    if (parkingTypeResponse.success!!) {
                        mCallBack.onSuccessGetParkingTypeApiCall(parkingTypeResponse)
                    } else {
                        mCallBack.onFailureGetParkingTypeApiCall(parkingTypeResponse.success.toString())
                    }

//                    if (response.value.success ?: null == true) {
//                        state.value = State.SUCCESS
//                        mCallBack.onSuccessGetParkingTypeApiCall(response.value)
//                    } else {
//                        state.value = State.ERROR
//                        mCallBack.onFailureGetParkingTypeApiCall(response.value.success.toString())
//                    }
                }

                else -> {}
            }
        }
    }

    fun getTrafficStreetType(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        val apnaSurveyUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/choose-data/traffic_street_types"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
//                ApnaSurveyApiRepo.getTrafficStreetType(apnaSurveyUrl)
                RegistrationRepo.getDetails(
                    baseUrL, token, GetDetailsRequest(apnaSurveyUrl, "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val trafficStreetTypeResponse = Gson().fromJson(
                        BackShlash.removeSubString(res), TrafficStreetTypeResponse::class.java
                    )
                    if (trafficStreetTypeResponse.success!!) {
                        mCallBack.onSuccessGetTrafficStreetTypeApiCall(trafficStreetTypeResponse)
                    } else {
                        mCallBack.onFailureGetTrafficStreetTypeApiCall(trafficStreetTypeResponse.success.toString())
                    }

//                    if (response.value.success ?: null == true) {
//                        state.value = State.SUCCESS
//                        mCallBack.onSuccessGetTrafficStreetTypeApiCall(response.value)
//                    } else {
//                        state.value = State.ERROR
//                        mCallBack.onFailureGetTrafficStreetTypeApiCall(response.value.success.toString())
//                    }
                }

                else -> {}
            }
        }
    }

    fun getNeighbouringLocation(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }

        val apnaSurveyUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/choose-data/neighbouring_location"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
//                ApnaSurveyApiRepo.getNeighbouringLocation(apnaSurveyUrl)
                RegistrationRepo.getDetails(
                    baseUrL, token, GetDetailsRequest(apnaSurveyUrl, "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val neighbouringLocationResponse = Gson().fromJson(
                        BackShlash.removeSubString(res), NeighbouringLocationResponse::class.java
                    )
                    if (neighbouringLocationResponse.success!!) {
                        mCallBack.onSuccessGetNeighbouringLocationApiCall(
                            neighbouringLocationResponse
                        )
                    } else {
                        mCallBack.onFailureGetNeighbouringLocationApiCall(
                            neighbouringLocationResponse.success.toString()
                        )
                    }

//                    if (response.value.success ?: null == true) {
//                        state.value = State.SUCCESS
//                        mCallBack.onSuccessGetNeighbouringLocationApiCall(response.value)
//                    } else {
//                        state.value = State.ERROR
//                        mCallBack.onFailureGetParkingTypeApiCall(response.value.success.toString())
//                    }
                }

                else -> {}
            }
        }
    }

    fun getTrafficGeneratorsType(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        val apnaSurveyUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/choose-data/traffic_generators"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
//                ApnaSurveyApiRepo.getTrafficGeneratorsType(apnaSurveyUrl)
                RegistrationRepo.getDetails(
                    baseUrL, token, GetDetailsRequest(apnaSurveyUrl, "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val trafficGeneratorsResponse = Gson().fromJson(
                        BackShlash.removeSubString(res), TrafficGeneratorsResponse::class.java
                    )
                    if (trafficGeneratorsResponse.success!!) {
                        mCallBack.onSuccessGetTrafficGeneratorsTypeApiCall(trafficGeneratorsResponse)
                    } else {
                        mCallBack.onFailureGetTrafficGeneratorsTypeApiCall(trafficGeneratorsResponse.success.toString())
                    }

//                    if (response.value.success ?: null == true) {
//                        state.value = State.SUCCESS
//                        mCallBack.onSuccessGetTrafficGeneratorsTypeApiCall(response.value)
//                    } else {
//                        state.value = State.ERROR
//                        mCallBack.onFailureGetTrafficGeneratorsTypeApiCall(response.value.success.toString())
//                    }
                }

                else -> {}
            }
        }
    }

    fun getApartmentType(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        val apnaSurveyUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/choose-data/apartment_type"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
//                ApnaSurveyApiRepo.getApartmentType(apnaSurveyUrl)
                RegistrationRepo.getDetails(
                    baseUrL, token, GetDetailsRequest(apnaSurveyUrl, "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val apartmentTypeResponse = Gson().fromJson(
                        BackShlash.removeSubString(res), ApartmentTypeResponse::class.java
                    )
                    if (apartmentTypeResponse.success!!) {
                        mCallBack.onSuccessGetApartmentTypeApiCall(apartmentTypeResponse)
                    } else {
                        mCallBack.onFailureGetApartmentTypeApiCall(apartmentTypeResponse.success.toString())
                    }

//                    if (response.value.success ?: null == true) {
//                        state.value = State.SUCCESS
//                        mCallBack.onSuccessGetApartmentTypeApiCall(response.value)
//                    } else {
//                        state.value = State.ERROR
//                        mCallBack.onFailureGetApartmentTypeApiCall(response.value.success.toString())
//                    }
                }

                else -> {}
            }
        }
    }

    fun createSurvey(surveyCreateRequest: SurveyCreateRequest, mCallBack: ApnaNewSurveyCallBack) {
        val surveyCreateRequestJson = Gson().toJson(surveyCreateRequest)
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        val apnaSurveyUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/apna_project_survey/save-update"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL,
                    token,
                    GetDetailsRequest(apnaSurveyUrl, "POST", surveyCreateRequestJson, "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val surveyCreateResponse = Gson().fromJson(
                        BackShlash.removeSubString(res), SurveyCreateResponse::class.java
                    )
                    if (surveyCreateResponse.success!!) {
                        mCallBack.onSuccessSurveyCreateApiCall(surveyCreateResponse)
                    } else {
                        mCallBack.onFailureSurveyCreateApiCall(surveyCreateResponse.message.toString())
                    }
                }

                else -> {}
            }
        }

    }

    fun getApnaSpeciality(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        val apnaSurveyUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/choose-data/apna_speciality"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
//                ApnaSurveyApiRepo.getApnaSpeciality(apnaSurveyUrl)
                RegistrationRepo.getDetails(
                    baseUrL, token, GetDetailsRequest(apnaSurveyUrl, "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val apnaSpecialityResponse = Gson().fromJson(
                        BackShlash.removeSubString(res), ApnaSpecialityResponse::class.java
                    )
                    if (apnaSpecialityResponse.success!!) {
                        mCallBack.onSuccessGetApnaSpecialityApiCall(apnaSpecialityResponse)
                    } else {
                        mCallBack.onFailureGetApnaSpecialityApiCall(apnaSpecialityResponse.success.toString())
                    }

//                    if (response.value.success ?: null == true) {
//                        state.value = State.SUCCESS
//                        mCallBack.onSuccessGetApnaSpecialityApiCall(response.value)
//                    } else {
//                        state.value = State.ERROR
//                        mCallBack.onFailureGetApnaSpecialityApiCall(response.value.success.toString())
//                    }
                }

                else -> {}
            }
        }
    }

    fun getStateList(mCallBack: ApnaNewSurveyCallBack) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        val apnaSurveyUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/state/list/state-list"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL, token, GetDetailsRequest(apnaSurveyUrl, "GET", "The", "", "")
                )

            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val stateListResponse = Gson().fromJson(
                        BackShlash.removeSubString(res), StateListResponse::class.java
                    )
                    if (stateListResponse.success!!) {
                        mCallBack.onSuccessGetStateListApiCall(stateListResponse)
                    } else {
                        mCallBack.onFailureGetStateListApiCall(stateListResponse.message.toString())
                    }
                }

                else -> {}
            }
        }
    }

    fun getCityList(mCallBack: ApnaNewSurveyCallBack, queryString: String) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
//        var uid = "dependents%5Bstate%5D%5Buid%5D=11FFD5814054DD13E06634029136E461"
        var state_uid = URLEncoder.encode("dependents[state][uid]", "utf-8")
        val apnaSurveyUrl =
            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/city/list/city-list-for-survey?"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL,
                    token,
                    GetDetailsRequest("$apnaSurveyUrl$state_uid=$queryString", "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val cityListResponse = Gson().fromJson(
                        BackShlash.removeSubString(res), CityListResponse::class.java
                    )
                    if (cityListResponse.success!!) {
                        mCallBack.onSuccessGetCityListApiCall(cityListResponse)
                    } else {
                        mCallBack.onFailureGetCityListApiCall(cityListResponse.message.toString())
                    }
                }

                else -> {}
            }
        }
    }

    fun imagesListConnectToAzure(
        imageFileList: List<File>,
        mCallBack: ApnaNewSurveyCallBack,
        surveyCreateRequest: SurveyCreateRequest,
    ) {
        state.value = State.SUCCESS
        viewModelScope.launch(Dispatchers.IO) {
            var imageUrlList = ConnectApnaAzure().connectToAzurListForApna(
                imageFileList!!, Config.CONTAINER_NAME, Config.STORAGE_CONNECTION_FOR_CCR_APP
            )
            if (imageUrlList != null) {
                mCallBack.onSuccessImagesConnectAzure(imageUrlList, surveyCreateRequest)
            } else {
                mCallBack.onFailureImageConnectAzure("Image blob Failed to connect")
            }
        }
    }

    fun videoListConnectToAzure(
        videoFileList: List<File>,
        mCallBack: ApnaNewSurveyCallBack,
        surveyCreateRequest: SurveyCreateRequest,
    ) {
        UploadVideoFileTask(mCallBack, surveyCreateRequest).execute(videoFileList)
    }

    sealed class CommandsNew {
        data class ShowToast(val message: String?) : CommandsNew()
        data class VideoIsUploadedInAzur(
            val videoUrlList: List<SurveyCreateRequest.VideoMb.Video>,
            var surveyCreateRequest: SurveyCreateRequest,
        ) : CommandsNew()
    }

    public class UploadVideoFileTask(
        mCallBack: ApnaNewSurveyCallBack,
        surveyCreateRequest: SurveyCreateRequest,
    ) : AsyncTask<List<File>?, List<SurveyCreateRequest.VideoMb.Video>?, List<SurveyCreateRequest.VideoMb.Video>?>() {
        var mCallback: ApnaNewSurveyCallBack? = mCallBack
        var surveyCreateRequest: SurveyCreateRequest = surveyCreateRequest

        override fun doInBackground(vararg videoFileList: List<File>?): List<SurveyCreateRequest.VideoMb.Video> {
            var videoUrlList = ConnectApnaAzure().videoConnectToAzurListForApna(
                videoFileList.get(0)!!, Config.CONTAINER_NAME, Config.STORAGE_CONNECTION_FOR_CCR_APP
            )
            return videoUrlList
        }

        override fun onProgressUpdate(vararg videoUrlList: List<SurveyCreateRequest.VideoMb.Video>?) {

        }

        override fun onPostExecute(result: List<SurveyCreateRequest.VideoMb.Video>?) {
            if (result != null) {
                mCallback!!.onSuccessVideoConnectAzure(result, surveyCreateRequest)
            } else {
                mCallback!!.onFailureVideoConnectAzure("Video blob failed to connect")
            }
            super.onPostExecute(result)
        }

    }
}