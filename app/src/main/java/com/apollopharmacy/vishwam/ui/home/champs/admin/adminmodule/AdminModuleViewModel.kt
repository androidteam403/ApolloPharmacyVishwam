package com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ChampsAdminRepo
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetCategoryDetailsResponse
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.SaveCategoryConfigurationDetailsRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminModuleViewModel : ViewModel() {

    fun getCategoryDetailsApiCall(mCallback: AdminModuleCallBack) {
        val state = MutableLiveData<State>()

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
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ChampsAdminRepo.getCategoryDetailsApiCall(token,
                    baseUrl//"https://172.16.103.116/Apollo/Champs/getCategoryDetails"
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value != null) {
                        if (response.value.status!!) {
                            for(i in response.value.categoryDetails!!){
                                if(i.rating!=null && !i.rating!!.isEmpty()){
                                    i.sumOfSubCategoryRating=i.rating!!.toDouble()
                                }

                            }
                            mCallback.onSuccessGetCategoryDetailsApiCall(response.value)
                        } else {
                            mCallback.onFailureGetCategoryDetailsApiCall(response.value.message!!)
                        }
                    } else {
                    }
                }
                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
    }

    fun getSubCategoryDetailsApiCall(
        mCallback: AdminModuleCallBack,
        categoryName: String
    ) {
        val state = MutableLiveData<State>()

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


//        var subCategoryUrl = "https://172.16.103.116/Apollo/Champs/getSubCategoryDetails"//?categoryName="+categoryName
        // var subCategoryUrl = "https://172.16.103.116/Apollo/Champs/getSubCategoryDetails?categoryName=$categoryName"

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ChampsAdminRepo.getSubCategoryDetailsApiCall(token,
                    baseUrl, categoryName)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value != null) {
                        if (response.value.status!!) {
                            mCallback.onSuccessGetSubCategoryDetailsApiCall(response.value,
                                categoryName)
                        } else {
                            mCallback.onFailureGetSubCategoryDetailsApiCall(response.value.message!!)
                        }
                    } else {
                    }
                }
                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
    }

    fun saveCategoryConfigurationDetailsApiCall(
        mCallback: AdminModuleCallBack,
        saveCategoryConfigurationDetailsRequest: SaveCategoryConfigurationDetailsRequest,
    ) {
        val state = MutableLiveData<State>()

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CHMP SAVE CATEGORY CONFIGURATION DETAILS")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }

//        var subCategoryUrl = "https://172.16.103.116/Apollo/Champs/saveCategoryConfigurationDetails"
        // var subCategoryUrl = "https://172.16.103.116/Apollo/Champs/saveCategoryConfigurationDetails"

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ChampsAdminRepo.saveCategoryConfigurationDetailsApiCall(token,
                    baseUrl,
                    saveCategoryConfigurationDetailsRequest)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value != null) {
                        if (response.value.status!!) {
                            mCallback.onSuccessSaveCategoryConfigurationDetailsApiCAll(response.value)
                        } else {
                            mCallback.onFailureSaveCategoryConfigurationDetails(response.value.message!!)
                        }
                    } else {
                    }
                }
                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
    }
}