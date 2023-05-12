package com.apollopharmacy.vishwam.ui.rider.onlinepayment;


import com.apollopharmacy.vishwam.ui.rider.onlinepayment.model.PhonePeQrCodeResponse;

public interface OnlinePaymentCallback {
    void onClickBack();

    void onClickNotificationIcon();

    void onClickPhonePeQRCode();

    void onClickPhonePeLink();

    void onClickHdfcLink();

    void onFailureMessage(String message);

    void onSuccessPhonepeQrCode(PhonePeQrCodeResponse phonePeQrCodeResponse, String phonePeQrCodetransactionId);

    void onSuccessPhonepeCheckStatus(PhonePeQrCodeResponse phonePeQrCodeResponse);

    void onSuccessPhonepePaymentCancelled(PhonePeQrCodeResponse phonePeQrCodeResponse);

    void onClickPhonePeCheckPaymentStatus();

    void onClickPhonePePaymentCancel();

    void onSuccessGeneratePhonePeLink(String response);

    void onSuccessPhonePeLinkPaymentCheckStatus(String response);

    void onSuccessPhonePeLinkPaymentCancelled(String response);
}
