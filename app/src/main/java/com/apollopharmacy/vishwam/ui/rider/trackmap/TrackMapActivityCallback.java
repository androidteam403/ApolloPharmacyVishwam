package com.apollopharmacy.vishwam.ui.rider.trackmap;


import com.apollopharmacy.vishwam.ui.rider.trackmap.model.OrderEndJourneyUpdateResponse;
import com.apollopharmacy.vishwam.ui.rider.trackmap.model.OrderStartJourneyUpdateResponse;

public interface TrackMapActivityCallback {

    void onFailureMessage(String message);

    void onSuccessOrderSaveUpdateStatusApi(String status);

    void onSuccessOrderStartJourneyUpdateApiCall(OrderStartJourneyUpdateResponse orderStartJourneyUpdateResponse);

    void onFailureOrderStartJourneyUpdateApiCall(String message);

    void onSuccessOrderEndJourneyUpdateApiCall(OrderEndJourneyUpdateResponse orderEndJourneyUpdateResponse);

    void onFailureOrderEndJourneyUpdateApiCall(String message);

    void onLogout();
}
