package com.apollopharmacy.vishwam.ui.rider.changepassword;


import com.apollopharmacy.vishwam.ui.rider.changepassword.model.ChangePasswordResponse;

public interface ChangePasswordFragmentCallback {
    void onFailureMessage(String message);

    void onClickUpdate();

    void onSuccessChangePasswordApiCall(ChangePasswordResponse changePasswordResponse);

    void onFailureChangePasswordApiCall(String message);

    void onSuccessRiderUpdateStatusApiCall();

    void onFailureRiderUpdateStatusApiCall(String message);

    void onLogout();
}
