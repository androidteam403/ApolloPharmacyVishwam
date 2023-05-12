package com.apollopharmacy.vishwam.ui.rider.summary;


import com.apollopharmacy.vishwam.ui.rider.myorders.model.MyOrdersListResponse;

public interface SummaryFragmentCallback {

    void onLogout();

    void onSuccessMyOrdersListApi(MyOrdersListResponse myOrdersListResponse);

    void onFailureMessage(String message);

    void onClickFromDate();

    void onClickToDate();

    void onClickOk();

    String fromDate();

    String toDate();
}
