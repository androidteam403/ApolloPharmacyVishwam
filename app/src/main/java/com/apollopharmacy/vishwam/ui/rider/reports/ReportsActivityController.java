package com.apollopharmacy.vishwam.ui.rider.reports;

import android.content.Context;


import com.apollopharmacy.vishwam.network.ApiClient;
import com.apollopharmacy.vishwam.network.ApiInterface;
import com.apollopharmacy.vishwam.ui.rider.dashboard.model.RiderActiveStatusRequest;
import com.apollopharmacy.vishwam.ui.rider.dashboard.model.RiderActiveStatusResponse;
import com.apollopharmacy.vishwam.ui.rider.db.SessionManager;
import com.apollopharmacy.vishwam.ui.rider.login.BackSlash;
import com.apollopharmacy.vishwam.ui.rider.login.model.GetDetailsRequest;
import com.apollopharmacy.vishwam.ui.rider.reports.model.OrdersCodStatusResponse;
import com.apollopharmacy.vishwam.ui.rider.service.NetworkUtils;
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

public class ReportsActivityController {

    private Context context;
    private ReportsActivityCallback mListener;

    public ReportsActivityController(Context context, ReportsActivityCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }
    public  final String BASE_URL = "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollo_rider/";

    public void getOrdersCodStatusApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            Utlis.INSTANCE.showLoading(context);
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();

            Gson gson = new Gson();
//    https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollo_rider/api/orders/list/rider-orders-cod-status?page=1&rows=10&from_date=2022-11-30&to_date=2022-12-06

            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(String.format("%sapi/orders/list/rider-orders-cod-status?%s10&%s&%s", BASE_URL, String.valueOf(1), Utlis.INSTANCE.getfromDate(), Utlis.INSTANCE.getCurrentDate("yyyy-MM-dd")));
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setHeadertokenvalue("Bearer " + new SessionManager(context).getLoginToken());
            getDetailsRequest.setRequesttype("GET");
            Call<ResponseBody> call = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    ActivityUtils.hideDialog();
                    Utlis.INSTANCE.hideLoading();
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

                            OrdersCodStatusResponse ordersCodStatusResponse = gson.fromJson(BackSlash.removeSubString(res), OrdersCodStatusResponse.class);
                            if (ordersCodStatusResponse != null && ordersCodStatusResponse.getSuccess()) {
                                mListener.onSuccessOrdersCodStatusApiCall(ordersCodStatusResponse);

                            } else if (response.code() == 401) {
                                Gson tokenGson = new Gson();
                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();

                                String jsonTokenRequest = tokenGson.toJson(refreshTokenRequest);
                                GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
                                getDetailsRequest.setRequesturl(BASE_URL + "refresh-token");
                                getDetailsRequest.setRequestjson(jsonTokenRequest);
                                getDetailsRequest.setHeadertokenkey("");
                                getDetailsRequest.setHeadertokenvalue("");
                                getDetailsRequest.setRequesttype("POST");
                                ActivityUtils.showDialog(context, "Please wait.");
//                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                                refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
                                Call<ResponseBody> call1 = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);

//                                Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                                call1.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NotNull Call<ResponseBody> call1, @NotNull Response<ResponseBody> response) {
                                        if (response.code() == 200 && response.body() != null) {
                                            new SessionManager(context).setLoginToken(response.body().toString());
                                            getOrdersCodStatusApiCall();
                                        } else if (response.code() == 401) {
                                            logout();
//
                                        } else {
                                            mListener.onFailureMessage("Please try again");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<ResponseBody> call1, @NotNull Throwable t) {
                                        ActivityUtils.hideDialog();
                                        Utlis.INSTANCE.hideLoading();
                                        mListener.onFailureMessage("Please try again");
                                        System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
                                    }
                                });

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    Utlis.INSTANCE.hideLoading();

                    mListener.onFailureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }

    public void logout() {
        if (NetworkUtils.isNetworkConnected(context)) {
            Utlis.INSTANCE.showLoading(context);
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
            getDetailsRequest.setRequesturl(BASE_URL + "api/user/save-update/update-rider-available-status");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer "+ new SessionManager(context).getLoginToken() );
            getDetailsRequest.setRequestjson(jsonriderActiveStatusRequest);
            getDetailsRequest.setRequesttype("POST");
            Call<ResponseBody> call = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    ActivityUtils.hideDialog();
                    Utlis.INSTANCE.hideLoading();
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
                    Utlis.INSTANCE.hideLoading();

                    mListener.onFailureMessage("Something went wrong.");
                    System.out.println("RIDER ACTIVE STATUS ==============" + t.getMessage());
                }
            });

        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }


}
