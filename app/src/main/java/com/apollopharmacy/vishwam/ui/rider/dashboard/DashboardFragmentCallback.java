package com.apollopharmacy.vishwam.ui.rider.dashboard;


import com.apollopharmacy.vishwam.ui.rider.profile.model.GetRiderProfileResponse;

public interface DashboardFragmentCallback {
    void onSuccessGetProfileDetailsApi(GetRiderProfileResponse getRiderProfileResponse);

    void onFialureMessage(String message);

    void onSuccessGetRiderDashboardCountApiCall(com.apollopharmacy.vishwam.ui.rider.dummy.dashboard.model.RiderDashboardCountResponse riderDashboardCountResponse);

    void onClickCodReciebedorPendingDeposits();

    void onClickTotalDeliveredCancelledOrders();

    void onLogout();

    void onClickNewOrders();

    void onClickIntransitOrders();

    void onClickDeliveredOrders();

    void onClickCancelledOrders();

    boolean isActiveStausSw();
}
