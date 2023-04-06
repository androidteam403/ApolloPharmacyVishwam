package com.apollopharmacy.vishwam.ui.rider.onlinepayment;

import android.content.Context;


import com.apollopharmacy.vishwam.network.ApiClient;
import com.apollopharmacy.vishwam.network.ApiInterface;
import com.apollopharmacy.vishwam.ui.rider.neworder.model.OrderDetailsResponse;
import com.apollopharmacy.vishwam.ui.rider.onlinepayment.model.PhonePeQrCodeRequest;
import com.apollopharmacy.vishwam.ui.rider.onlinepayment.model.PhonePeQrCodeResponse;
import com.apollopharmacy.vishwam.ui.rider.service.NetworkUtils;
import com.apollopharmacy.vishwam.util.Utils;
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlinePaymentController {
    private Context mContext;
    private OnlinePaymentCallback mListener;

    public OnlinePaymentController(Context mContext, OnlinePaymentCallback mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
    }

    public void phonePeQRCodeApiCall(OrderDetailsResponse orderDetailsResponse) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please Wait...");
            PhonePeQrCodeRequest phonePeQrCodeRequest = new PhonePeQrCodeRequest();
            phonePeQrCodeRequest.setStoreId(orderDetailsResponse.getData().getPickupBranch());
            String phonePeQrCodetransactionId = orderDetailsResponse.getData().getOrderNumber().substring(orderDetailsResponse.getData().getOrderNumber().length() - 4) + Utils.getCurrentDateTimeMSUnique();
            phonePeQrCodeRequest.setTransactionId(phonePeQrCodetransactionId);
            phonePeQrCodeRequest.setAmount(Integer.valueOf(String.valueOf(orderDetailsResponse.getData().getPakgValue()).substring(0, String.valueOf(orderDetailsResponse.getData().getPakgValue()).indexOf("."))));
            phonePeQrCodeRequest.setExpiresIn(2000);
            phonePeQrCodeRequest.setRequestType("VIVEKAGAM");

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<PhonePeQrCodeResponse> call = apiInterface.PHONEPE_QRCODE_API_CALL(phonePeQrCodeRequest);
            call.enqueue(new Callback<PhonePeQrCodeResponse>() {
                @Override
                public void onResponse(@NotNull Call<PhonePeQrCodeResponse> call, @NotNull Response<PhonePeQrCodeResponse> response) {
                    if (response.body() != null && response.body().getStatus()) {
                        if (mListener != null) {
                            mListener.onSuccessPhonepeQrCode(response.body(), phonePeQrCodetransactionId);
                        }
                    } else if (response.body() != null && !response.body().getStatus()) {
                        ActivityUtils.hideDialog();
                        mListener.onFailureMessage(response.body().getMessage());
                    } else {
                        ActivityUtils.hideDialog();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<PhonePeQrCodeResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFailureMessage("Something went wrong.");
        }

    }

    public void phoneCheckPaymentStatusApiCall(String phonePeQrCodetransactionId) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please Wait...");
            PhonePeQrCodeRequest phonePeQrCodeRequest = new PhonePeQrCodeRequest();
            phonePeQrCodeRequest.setTransactionId(phonePeQrCodetransactionId);
            phonePeQrCodeRequest.setRequestType("VIVEKAGAM");

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<PhonePeQrCodeResponse> call = apiInterface.PHONEPE_CHECK_PAYMENT_STAUS_API_CALL(phonePeQrCodeRequest);
            call.enqueue(new Callback<PhonePeQrCodeResponse>() {
                @Override
                public void onResponse(@NotNull Call<PhonePeQrCodeResponse> call, @NotNull Response<PhonePeQrCodeResponse> response) {
                    if (response.body() != null && response.body().getStatus()) {
                        if (mListener != null) {
                            mListener.onSuccessPhonepeCheckStatus(response.body());
                        }
                    } else if (response.body() != null && !response.body().getStatus()) {
                        ActivityUtils.hideDialog();
                        mListener.onFailureMessage(response.body().getMessage());
                    } else {
                        ActivityUtils.hideDialog();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<PhonePeQrCodeResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }

    public void phonePePaymentCancelledApiCall(String phonePeQrCodetransactionId) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please Wait...");
            PhonePeQrCodeRequest phonePeQrCodeRequest = new PhonePeQrCodeRequest();
            phonePeQrCodeRequest.setTransactionId(phonePeQrCodetransactionId);
            phonePeQrCodeRequest.setRequestType("VIVEKAGAM");

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<PhonePeQrCodeResponse> call = apiInterface.PHONEPE_PAYMENT_CANCELLED_API_CALL(phonePeQrCodeRequest);
            call.enqueue(new Callback<PhonePeQrCodeResponse>() {
                @Override
                public void onResponse(@NotNull Call<PhonePeQrCodeResponse> call, @NotNull Response<PhonePeQrCodeResponse> response) {
                    if (response.body() != null && response.body().getStatus()) {
                        if (mListener != null) {
                            mListener.onSuccessPhonepePaymentCancelled(response.body());
                        }
                    } else if (response.body() != null && !response.body().getStatus()) {
                        ActivityUtils.hideDialog();
                        mListener.onFailureMessage(response.body().getMessage());
                    } else {
                        ActivityUtils.hideDialog();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<PhonePeQrCodeResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }


    public void generatePhonePePaymentLinkApiCall(String amount, String mobileNumber, String orderNumber, String siteId) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please Wait...");

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<ResponseBody> call = apiInterface.GENERATE_PHONEPE_LINK_API_CALL(siteId, Utils.getCurrentDateTimeMSUnique() + orderNumber.substring(orderNumber.length() - 6), "VEG", amount, mobileNumber, "WITHDRAW");//amount
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null) {
                        if (mListener != null) {
                            try {
                                String responses = response.body().string();
                                mListener.onSuccessGeneratePhonePeLink(responses);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }

    public void phonePeLinkCheckStatusApiCall(String transactionId) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please Wait...");

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<ResponseBody> call = apiInterface.PHONEPE_LINK_CHECK_STATUS_API_CALL("16001",Utils.getCurrentDateTimeMSUnique(), "VEG", transactionId, "CHECKSTATUS");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null) {
                        if (mListener != null) {
                            try {
                                String responses = response.body().string();
                                mListener.onSuccessPhonePeLinkPaymentCheckStatus(responses);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }

    public void phonePeLinkCancelledRequestApiCall(String refereneceId, String transactionId, String mobileNumber) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please Wait...");

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<ResponseBody> call = apiInterface.PHONEPE_LINK_CANCELLED_REQUEST_API_CALL("16001",Utils.getCurrentDateTimeMSUnique(), refereneceId, mobileNumber, transactionId, "CANCELREQUEST");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null) {
                        if (mListener != null) {
                            try {
                                String responses = response.body().string();
                                mListener.onSuccessPhonePeLinkPaymentCancelled(responses);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }

}