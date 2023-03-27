package com.apollopharmacy.vishwam.ui.rider.reports;


import com.apollopharmacy.vishwam.ui.rider.reports.model.OrdersCodStatusResponse;

public interface ReportsActivityCallback {
    void onFailureMessage(String message);

    void onClickNotificationIcon();

    void onSuccessOrdersCodStatusApiCall(OrdersCodStatusResponse ordersCodStatusResponse);

    void onFailureOrdersCodStatusApiCall(String message);

    void onClickBack();

    void onLogout();
}
