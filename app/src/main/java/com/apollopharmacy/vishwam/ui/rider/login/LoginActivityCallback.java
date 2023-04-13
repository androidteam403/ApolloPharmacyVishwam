package com.apollopharmacy.vishwam.ui.rider.login;


import com.apollopharmacy.vishwam.ui.rider.login.model.LoginResponse;
import com.apollopharmacy.vishwam.ui.rider.orderdelivery.model.DeliveryFailreReasonsResponse;
import com.apollopharmacy.vishwam.ui.rider.profile.model.GetRiderProfileResponse;

public interface LoginActivityCallback {

    void onFialureMessage(String message);

    void onSuccessLoginApi(LoginResponse loginResponse);

    void onFailureLoginApi(String message);

    void onSuccessGetProfileDetailsApi(GetRiderProfileResponse getRiderProfileResponse);

    void onSuccessDeliveryReasonApiCall(DeliveryFailreReasonsResponse deliveryFailreReasonsResponse);

    void onFailureGetProfileDetailsApi(String message);

    void onClickSignin();

    void onClickForgotPasswordText();
}
