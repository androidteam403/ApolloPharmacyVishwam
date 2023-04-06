package com.apollopharmacy.vishwam.ui.validatepin

import com.apollopharmacy.vishwam.ui.rider.login.model.LoginResponse
import com.apollopharmacy.vishwam.ui.rider.orderdelivery.model.DeliveryFailreReasonsResponse
import com.apollopharmacy.vishwam.ui.rider.profile.model.GetRiderProfileResponse

interface ValidatePinCallBack {
     fun onSuccessLoginApi(loginResponse: LoginResponse)

     fun onFailureLoginApi(message: String?)

     fun onFialureMessage(message: String?)

      fun onSuccessGetProfileDetailsApi(riderProfileResponse: GetRiderProfileResponse?)

      fun onFailureGetProfileDetailsApi(s: String)

     fun onSuccessDeliveryReasonApiCall(deliveryFailreReasonsResponse: DeliveryFailreReasonsResponse)
}