package com.apollopharmacy.vishwam.ui.validatepin

import com.apollopharmacy.vishwam.ui.home.notification.model.NotificationModelResponse
import com.apollopharmacy.vishwam.ui.home.swach.model.AppLevelDesignationModelResponse

interface ValidatePinCallBack {

     fun onFailureLoginApi(message: String?)

     fun onFialureMessage(message: String?)


      fun onFailureGetProfileDetailsApi(s: String)

     fun onSuccessAppLevelDesignationApnaRetro(value: AppLevelDesignationModelResponse)
     fun onFailureAppLevelDesignationApnaRetro(value: AppLevelDesignationModelResponse)
     fun onSuccessNotificationDetails(value: NotificationModelResponse)
     fun onFailureNotificationDetails(value: NotificationModelResponse)
    fun onFailureNotificationDetail()
    fun onHandleNextEvent()
}