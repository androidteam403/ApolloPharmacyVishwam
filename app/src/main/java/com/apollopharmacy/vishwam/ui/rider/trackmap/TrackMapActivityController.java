package com.apollopharmacy.vishwam.ui.rider.trackmap;
import android.content.Context;


import com.apollopharmacy.vishwam.network.ApiClient;
import com.apollopharmacy.vishwam.network.ApiInterface;
import com.apollopharmacy.vishwam.ui.rider.dashboard.model.RiderActiveStatusRequest;
import com.apollopharmacy.vishwam.ui.rider.dashboard.model.RiderActiveStatusResponse;
import com.apollopharmacy.vishwam.ui.rider.db.SessionManager;
import com.apollopharmacy.vishwam.ui.rider.login.BackSlash;
import com.apollopharmacy.vishwam.ui.rider.login.model.GetDetailsRequest;
import com.apollopharmacy.vishwam.ui.rider.orderdelivery.model.OrderSaveUpdateStausRequest;
import com.apollopharmacy.vishwam.ui.rider.orderdelivery.model.OrderSaveUpdateStausResponse;
import com.apollopharmacy.vishwam.ui.rider.service.NetworkUtils;
import com.apollopharmacy.vishwam.ui.rider.trackmap.model.OrderEndJourneyUpdateRequest;
import com.apollopharmacy.vishwam.ui.rider.trackmap.model.OrderEndJourneyUpdateResponse;
import com.apollopharmacy.vishwam.ui.rider.trackmap.model.OrderStartJourneyUpdateRequest;
import com.apollopharmacy.vishwam.ui.rider.trackmap.model.OrderStartJourneyUpdateResponse;
import com.apollopharmacy.vishwam.util.AppConstants;
import com.apollopharmacy.vishwam.util.Utlis;
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackMapActivityController {
    private Context context;
    private TrackMapActivityCallback mListener;

    public TrackMapActivityController(Context context, TrackMapActivityCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public void ordersSaveUpdateStatusApiCall(String orderStatus, String orderUid, String orderCancelReason, String comment) {
        if (NetworkUtils.isNetworkConnected(context)) {
            OrderSaveUpdateStausRequest orderSaveUpdateStausRequest = new OrderSaveUpdateStausRequest();
            orderSaveUpdateStausRequest.setUid(orderUid);
            OrderSaveUpdateStausRequest.OrderStatus orderStatus1 = new OrderSaveUpdateStausRequest.OrderStatus();
            orderStatus1.setUid(orderStatus);
            orderSaveUpdateStausRequest.setOrderStatus(orderStatus1);
            OrderSaveUpdateStausRequest.DeliveryFailureReason deliveryFailureReason = new OrderSaveUpdateStausRequest.DeliveryFailureReason();
            deliveryFailureReason.setUid(orderCancelReason);
            orderSaveUpdateStausRequest.setDeliveryFailureReason(deliveryFailureReason);
            orderSaveUpdateStausRequest.setComment(comment);
            Gson gson = new Gson();
            String jsonorderSaveUpdateStausRequest = gson.toJson(orderSaveUpdateStausRequest);
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(AppConstants.BASE_URL + "api/orders/save-update/order-status-update");

            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setRequestjson(jsonorderSaveUpdateStausRequest);
            getDetailsRequest.setHeadertokenvalue("Bearer "+ new SessionManager(context).getLoginToken());
            getDetailsRequest.setRequesttype("POST");

            Call<ResponseBody> call = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null) {
                        String resp = null;
                        try {
                            resp = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (resp != null) {
                            String res = BackSlash.removeBackSlashes(resp);
                            Gson gson = new Gson();
                            OrderSaveUpdateStausResponse orderSaveUpdateStausResponse = gson.fromJson(BackSlash.removeSubString(res), OrderSaveUpdateStausResponse.class);
                            if (orderSaveUpdateStausResponse != null && orderSaveUpdateStausResponse.getData() != null && orderSaveUpdateStausResponse.getSuccess()) {
                                mListener.onSuccessOrderSaveUpdateStatusApi(orderStatus);
                            }

                            else if (response.code() == 401) {
                                ActivityUtils.showDialog(context, "Please wait.");

                                Gson tokenGson = new Gson();
                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();

                                String jsonTokenRequest = tokenGson.toJson(refreshTokenRequest);
                                GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
                                getDetailsRequest.setRequesturl(AppConstants.BASE_URL + "refresh-token");
                                getDetailsRequest.setRequestjson(jsonTokenRequest);
                                getDetailsRequest.setHeadertokenkey("");
                                getDetailsRequest.setHeadertokenvalue("");
                                getDetailsRequest.setRequesttype("POST");
//                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                                refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
                                Call<ResponseBody> call1 = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);

//                                Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                                call1.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NotNull Call<ResponseBody> call1, @NotNull Response<ResponseBody> response) {
                                        if (response.code() == 200 && response.body() != null) {
                                            new SessionManager(context).setLoginToken(response.body().toString());
                                            ordersSaveUpdateStatusApiCall(orderStatus, orderUid, orderCancelReason, comment);
                                        } else if (response.code() == 401) {
                                            logout();
                                        } else {
                                            mListener.onFailureMessage("Please try again");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<ResponseBody> call1, @NotNull Throwable t) {
                                        ActivityUtils.hideDialog();
                                        mListener.onFailureMessage("Please try again");
                                        System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
                                    }
                                });

                            }
                            else {
                                ActivityUtils.hideDialog();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }


    public void orderStartJourneyUpdateApiCall(String uid, String distance) {
        if (NetworkUtils.isNetworkConnected(context)) {
            OrderStartJourneyUpdateRequest orderStartJourneyUpdateRequest = new OrderStartJourneyUpdateRequest();
            orderStartJourneyUpdateRequest.setUid(uid);
            OrderStartJourneyUpdateRequest.OrderRider orderRider = new OrderStartJourneyUpdateRequest.OrderRider();
            orderRider.setActualDistance(distance);
            orderRider.setStartTime(Utlis.INSTANCE.getCurrentTimeDate());
            orderStartJourneyUpdateRequest.setOrderRider(orderRider);
//
            Gson gson = new Gson();
            String jsonorderStartJourneyRequest = gson.toJson(orderStartJourneyUpdateRequest);
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(AppConstants.BASE_URL + "api/orders/save-update/ord-rdr-strt-jrny-update");

            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setRequestjson(jsonorderStartJourneyRequest);
            getDetailsRequest.setHeadertokenvalue("Bearer "+ new SessionManager(context).getLoginToken());
            getDetailsRequest.setRequesttype("POST");

            Call<ResponseBody> call = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null) {
                        String resp = null;
                        try {
                            resp = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (resp != null) {
                            String res = BackSlash.removeBackSlashes(resp);
                            Gson gson = new Gson();
                            OrderStartJourneyUpdateResponse orderStartJourneyUpdateResponse = gson.fromJson(BackSlash.removeSubString(res), OrderStartJourneyUpdateResponse.class);
                            if (orderStartJourneyUpdateResponse != null && orderStartJourneyUpdateResponse.getData() != null && orderStartJourneyUpdateResponse.getSuccess()) {
                                mListener.onSuccessOrderStartJourneyUpdateApiCall(orderStartJourneyUpdateResponse);
                            }

                            else if (response.code() == 401) {
                                ActivityUtils.showDialog(context, "Please wait.");

                                Gson tokenGson = new Gson();
                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();

                                String jsonTokenRequest = tokenGson.toJson(refreshTokenRequest);
                                GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
                                getDetailsRequest.setRequesturl(AppConstants.BASE_URL + "refresh-token");
                                getDetailsRequest.setRequestjson(jsonTokenRequest);
                                getDetailsRequest.setHeadertokenkey("");
                                getDetailsRequest.setHeadertokenvalue("");
                                getDetailsRequest.setRequesttype("POST");
//                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                                refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
                                Call<ResponseBody> call1 = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);

//                                Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                                call1.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NotNull Call<ResponseBody> call1, @NotNull Response<ResponseBody> response) {
                                        if (response.code() == 200 && response.body() != null) {
                                            new SessionManager(context).setLoginToken(response.body().toString());
                                            orderStartJourneyUpdateApiCall(uid, distance);
                                        } else if (response.code() == 401) {
                                            logout();
                                        } else {
                                            mListener.onFailureMessage("Please try again");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<ResponseBody> call1, @NotNull Throwable t) {
                                        ActivityUtils.hideDialog();
                                        mListener.onFailureMessage("Please try again");
                                        System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
                                    }
                                });

                            }
                            else {
                                mListener.onFailureMessage("No data saved.");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }
    public void orderEndJourneyUpdateApiCall(String uid) {
        if (NetworkUtils.isNetworkConnected(context)) {
            OrderEndJourneyUpdateRequest orderEndJourneyUpdateRequest = new OrderEndJourneyUpdateRequest();
            orderEndJourneyUpdateRequest.setUid(uid);
            OrderEndJourneyUpdateRequest.OrderRider orderRider = new OrderEndJourneyUpdateRequest.OrderRider();
            orderRider.setEndTime(Utlis.INSTANCE.getCurrentTimeDate());
            orderEndJourneyUpdateRequest.setOrderRider(orderRider);
//
            Gson gson = new Gson();
            String jsonorderEndJourneyRequest = gson.toJson(orderEndJourneyUpdateRequest);
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(AppConstants.BASE_URL + "api/orders/save-update/ord-rdr-end-jrny-update");

            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setRequestjson(jsonorderEndJourneyRequest);
            getDetailsRequest.setHeadertokenvalue("Bearer "+ new SessionManager(context).getLoginToken());
            getDetailsRequest.setRequesttype("POST");

            Call<ResponseBody> call = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null) {
                        String resp = null;
                        try {
                            resp = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (resp != null) {
                            String res = BackSlash.removeBackSlashes(resp);
                            Gson gson = new Gson();
                            OrderEndJourneyUpdateResponse orderEndJourneyUpdateResponse = gson.fromJson(BackSlash.removeSubString(res), OrderEndJourneyUpdateResponse.class);
                            if (orderEndJourneyUpdateResponse != null && orderEndJourneyUpdateResponse.getData() != null && orderEndJourneyUpdateResponse.getSuccess()) {
                                mListener.onSuccessOrderEndJourneyUpdateApiCall(orderEndJourneyUpdateResponse);
                            }

                            else if (response.code() == 401) {
                                ActivityUtils.showDialog(context, "Please wait.");

                                Gson tokenGson = new Gson();
                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();

                                String jsonTokenRequest = tokenGson.toJson(refreshTokenRequest);
                                GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
                                getDetailsRequest.setRequesturl(AppConstants.BASE_URL + "refresh-token");
                                getDetailsRequest.setRequestjson(jsonTokenRequest);
                                getDetailsRequest.setHeadertokenkey("");
                                getDetailsRequest.setHeadertokenvalue("");
                                getDetailsRequest.setRequesttype("POST");
//                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                                refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
                                Call<ResponseBody> call1 = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);

//                                Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                                call1.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NotNull Call<ResponseBody> call1, @NotNull Response<ResponseBody> response) {
                                        if (response.code() == 200 && response.body() != null) {
                                            new SessionManager(context).setLoginToken(response.body().toString());
                                            orderEndJourneyUpdateApiCall(uid);
                                        } else if (response.code() == 401) {
                                            logout();
                                        } else {
                                            mListener.onFailureMessage("Please try again");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<ResponseBody> call1, @NotNull Throwable t) {
                                        ActivityUtils.hideDialog();
                                        mListener.onFailureMessage("Please try again");
                                        System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
                                    }
                                });

                            }
                            else {
                                mListener.onFailureMessage("No data saved.");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }
    public void logout() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
            riderActiveStatusRequest.setUid(new SessionManager(context).getRiderProfileResponse().getData().getUid());
            RiderActiveStatusRequest.UserAddInfo userAddInfo = new RiderActiveStatusRequest.UserAddInfo();
            RiderActiveStatusRequest.AvailableStatus availableStatus = new RiderActiveStatusRequest.AvailableStatus();
            availableStatus.setUid("Offline");

            userAddInfo.setAvailableStatus(availableStatus);
            riderActiveStatusRequest.setUserAddInfo(userAddInfo);
            Gson gson = new Gson();
            String jsonriderActiveStatusRequest = gson.toJson(riderActiveStatusRequest);
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(AppConstants.BASE_URL + "api/user/save-update/update-rider-available-status");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer "+ new SessionManager(context).getLoginToken() );
            getDetailsRequest.setRequestjson(jsonriderActiveStatusRequest);
            getDetailsRequest.setRequesttype("POST");
            Call<ResponseBody> call = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null) {
                        String resp = null;
                        try {
                            resp = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (resp != null) {
                            String res = BackSlash.removeBackSlashes(resp);
                            Gson gson = new Gson();
                            RiderActiveStatusResponse riderActiveStatusResponse = gson.fromJson(BackSlash.removeSubString(res), RiderActiveStatusResponse.class);
                            if (riderActiveStatusResponse != null && riderActiveStatusResponse.getData() != null && riderActiveStatusResponse.getSuccess()) {
                                new SessionManager(context).setRiderActiveStatus("Offline");
                                mListener.onLogout();
                            } else if (response.code() == 401) {
                                mListener.onLogout();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureMessage("Something went wrong.");
                    System.out.println("RIDER ACTIVE STATUS ==============" + t.getMessage());
                }
            });

        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }

}
