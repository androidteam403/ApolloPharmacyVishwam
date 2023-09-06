package com.apollopharmacy.vishwam.ui.validatepin

import com.apollopharmacy.vishwam.ui.home.swach.model.AppLevelDesignationModelResponse
import com.apollopharmacy.vishwam.ui.login.model.MobileAccessResponse

interface ValidatePinCallBack {
    fun onFailureLoginApi(message: String?)
    fun onFialureMessage(message: String?)
    fun onSuccessAppLevelDesignationApnaRetro(value: AppLevelDesignationModelResponse)
    fun onFailureAppLevelDesignationApnaRetro(value: AppLevelDesignationModelResponse)
    fun onSuccessMobileAccessApiCall(value: MobileAccessResponse)
}