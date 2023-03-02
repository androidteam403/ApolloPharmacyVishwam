package com.apollopharmacy.vishwam.ui.rider.profile;

import com.apollopharmacy.vishwam.ui.rider.login.model.ComplaintReasonsListResponse;
import com.apollopharmacy.vishwam.ui.rider.profile.model.GetRiderProfileResponse;

public interface ProfileFragmentCallback {
    void onFialureMessage(String message);

    void onSuccessGetProfileDetailsApi(GetRiderProfileResponse getRiderProfileResponse);

    void onFailureGetProfileDetailsApi(String message);

    void onItemClickIdentityProof(GetRiderProfileResponse.IdentificationProof identificationProof);

    void onSuccessComplaintSaveUpdate(String message);

    void onSuccessComplaintReasonsListApiCall(ComplaintReasonsListResponse complaintReasonsListResponse);

    void onClickComplaint();

    void onLogout();
}
