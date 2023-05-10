package com.apollopharmacy.vishwam.ui.rider.neworder;


import com.apollopharmacy.vishwam.ui.rider.neworder.model.OrderDetailsResponse;

public interface NewOrderActivityCallback {
    void onSuccessOrderDetailsApiCall(OrderDetailsResponse orderDetailsResponse);

    void onFialureOrderDetailsApiCall();

    void onFialureMessage(String message);
}
