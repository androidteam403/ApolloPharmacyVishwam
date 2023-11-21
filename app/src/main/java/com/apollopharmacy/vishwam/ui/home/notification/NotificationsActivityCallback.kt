package com.apollopharmacy.vishwam.ui.home.notification

import com.apollopharmacy.vishwam.ui.home.notification.model.NotificationModelResponse

interface NotificationsActivityCallback {
    fun onclickHelpIcon()
    fun onclickBackArrow()
    fun onSuccessNotificationDetails(saveUpdateRequestJsonResponse: NotificationModelResponse?)
    fun onFailureNotificationDetails(saveUpdateRequestJsonResponse: NotificationModelResponse?)
    fun onClickParentLayout(get: NotificationModelResponse.Data.ListData.Row)

}