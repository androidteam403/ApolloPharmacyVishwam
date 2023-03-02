package com.apollopharmacy.vishwam.ui.rider.orderdelivery;

import android.graphics.Bitmap;

import com.apollopharmacy.vishwam.ui.rider.neworder.model.OrderDetailsResponse;
import com.apollopharmacy.vishwam.ui.rider.orderdelivery.model.DeliveryFailreReasonsResponse;
import com.apollopharmacy.vishwam.ui.rider.orderdelivery.model.OrderPaymentSelectResponse;
import com.apollopharmacy.vishwam.ui.rider.orderdelivery.model.OrderStatusHitoryListResponse;
import com.apollopharmacy.vishwam.ui.rider.trackmap.model.OrderEndJourneyUpdateResponse;


public interface OrderDeliveryActivityCallback {
    // test

    void onFailureMessage(String message);

    void onClickBackIcon();

    void onSuccessOrderDetailsApiCall(OrderDetailsResponse orderDetailsResponse);

    void onFialureOrderDetailsApiCall();

    void onClickPickupVerifyOtp();

    void onFialureMessage(String message);

    void onSuccessOrderSaveUpdateStatusApi(String status);

    void onFialureOrderSaveUpdateStatusApi(String message);

    void onClickHandoverTheParcelItem(String item);

    void onSuccessOrderHandoverSaveUpdateApi(Bitmap bmp);

    void onFailureOrderHandoverSaveUpdateApi(String message);

    void onClickHandoverTheParcelBtn();

    void onClickCollectPayment();

    void onClickCollectPaymentSave();

    String getCodCardCash();

    void onSuccessOrderPaymentUpdateApiCall();

    void onFailureOrderPaymentApiCall();

    void onClickEyeImage();

    void onSuccessOrderStatusHistoryListApiCall(OrderStatusHitoryListResponse orderStatusHitoryListResponse);

    void onFailureOrderStatusHistoryListApiCall(String message);

    void onClickOrderNotDeliveredCallIcon();

    void onClickNotificationIcon();

    void onSuccessDeliveryReasonApiCall(DeliveryFailreReasonsResponse deliveryFailreReasonsResponse);

    void onFailureDeliveryFailureApiCall();

    void onClickCancelledReachedtheStore();

    void onClickCancelledOtpVerificationParentLayout();

    void onClickCancelledVerifyOtp();

    void onClickProofofHandoverSkip();

    void onClickOrderNotDelivered();

    void onClickPickedLabel();

    void onClickPicked();

    void onClickDeliveredLabel();

    void onClickDelivered();

    void onClickReturnLabel();

    void onClickReturntoStoreShowMap();

    void onSuccessOrderPaymentTypeInCod(OrderPaymentSelectResponse orderPaymentSelectResponse);

    void onFailureOrderPaymentTypeInCod(String message);

    void onSuccessOrderEndJourneyUpdateApiCall(OrderEndJourneyUpdateResponse orderEndJourneyUpdateResponse);

    void onLogout();

    boolean isOrderCancelled();

    void orderCancelled();

    boolean isStatusCancelled();

    void statusCanelled();
}
