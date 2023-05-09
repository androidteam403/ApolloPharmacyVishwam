package com.apollopharmacy.vishwam.ui.rider.reports;


import com.apollopharmacy.vishwam.ui.rider.reports.model.OrdersCodStatusResponse;

public interface ReportsFragmentCallback {

    void onFialureMessage(String message);

    void onSuccessOrdersCodStatusApiCall(OrdersCodStatusResponse ordersCodStatusResponse);

    void onFailureOrdersCodStatusApiCall(String message);

    void onLogout();

    void onSuccessGetRiderDashboardCountApiCall(com.apollopharmacy.vishwam.ui.rider.dummy.dashboard.model.RiderDashboardCountResponse riderDashboardCountResponse);

    void onClickFromDate();

    void onClickToDate();

    void onClickOk();

    String fromDate();

    String toDate();
}
