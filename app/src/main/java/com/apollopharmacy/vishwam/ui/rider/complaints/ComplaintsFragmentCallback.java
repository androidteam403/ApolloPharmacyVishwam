package com.apollopharmacy.vishwam.ui.rider.complaints;


import com.apollopharmacy.vishwam.ui.rider.complaints.model.ComplaintsResponse;
import com.apollopharmacy.vishwam.ui.rider.login.model.ComplaintReasonsListResponse;

public interface ComplaintsFragmentCallback {
    void onSuccessGetComplaintsListApiCall(ComplaintsResponse complaintsResponse);

    void onFailureMessage(String message);

    void onLogout();

    void onClickComplaint();

    void onSuccessComplaintSaveUpdate(String message);

    void onSuccessComplaintReasonsListApiCall(ComplaintReasonsListResponse complaintReasonsListResponse);

    void complaintResolvedCallback();
}
