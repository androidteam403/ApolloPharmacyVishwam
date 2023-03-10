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

public class ReportsFragmentController {
    private Context context;
    private ReportsFragmentCallback mListener;

    public ReportsFragmentController(Context context, ReportsFragmentCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public final String BASE_URL = "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollo_rider/";


    public void getRiderDashboardCountsApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(AppConstants.BASE_URL + "api/user/select/rider-dashboard-counts" + "?" + mListener.fromDate() + "&" + mListener.toDate());
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setHeadertokenvalue("Bearer " + new SessionManager(context).getLoginToken());
            getDetailsRequest.setRequesttype("GET");
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
//                            JsonReader jsonReader=new JsonReader(new StringReader(res));
//                            jsonReader.setLenient(true);

                            com.apollopharmacy.vishwam.ui.rider.dummy.dashboard.model.RiderDashboardCountResponse riderDashboardCountResponse = gson.fromJson(BackSlash.removeSubString(res), com.apollopharmacy.vishwam.ui.rider.dummy.dashboard.model.RiderDashboardCountResponse.class);
                            if (riderDashboardCountResponse != null && riderDashboardCountResponse.getSuccess()) {
                                mListener.onSuccessGetRiderDashboardCountApiCall(riderDashboardCountResponse);
//                                getSessionManager().setCodReceived(riderDashboardCountResponse.getData().getCount().getCodReceived()));
//                        getSessionManager().setCodPendingDeposited(riderDashboardCountResponse.getData().getCount().getCodPending()));
                            } else if (response.code() == 401) {
                                Gson tokenGson = new Gson();
                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();

                                String jsonTokenRequest = tokenGson.toJson(refreshTokenRequest);
                                GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
                                getDetailsRequest.setRequesturl(AppConstants.BASE_URL + "refresh-token");
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
                                            getRiderDashboardCountsApiCall();
                                        } else if (response.code() == 401) {
                                            logout();
//
                                        } else {
                                            mListener.onFialureMessage("Please try again");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<ResponseBody> call1, @NotNull Throwable t) {
                                        ActivityUtils.hideDialog();
                                        Utlis.INSTANCE.hideLoading();
                                        mListener.onFialureMessage("Please try again");
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
                    mListener.onFialureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void getOrdersCodStatusApiCall(int page) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();

            Gson gson = new Gson();
//    https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollo_rider/api/orders/list/rider-orders-cod-status?page=1&rows=10&from_date=2022-11-30&to_date=2022-12-06

            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(String.format("%sapi/orders/list/rider-orders-cod-status?%s10&%s&%s", AppConstants.BASE_URL, String.valueOf(page), mListener.fromDate(), mListener.toDate()));
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setHeadertokenvalue("Bearer " + new SessionManager(context).getLoginToken());
            getDetailsRequest.setRequesttype("GET");
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

                            OrdersCodStatusResponse ordersCodStatusResponse = gson.fromJson(BackSlash.removeSubString(res), OrdersCodStatusResponse.class);
                            if (ordersCodStatusResponse != null && ordersCodStatusResponse.getSuccess()) {
                                mListener.onSuccessOrdersCodStatusApiCall(ordersCodStatusResponse);

                            } else if (response.code() == 401) {
                                Gson tokenGson = new Gson();
                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();

                                String jsonTokenRequest = tokenGson.toJson(refreshTokenRequest);
                                GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
                                getDetailsRequest.setRequesturl(AppConstants.BASE_URL + "refresh-token");
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
                                            getOrdersCodStatusApiCall(page);
                                        } else if (response.code() == 401) {
                                            logout();
//
                                        } else {
                                            mListener.onFialureMessage("Please try again");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<ResponseBody> call1, @NotNull Throwable t) {
                                        ActivityUtils.hideDialog();
                                        Utlis.INSTANCE.hideLoading();
                                        mListener.onFialureMessage("Please try again");
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

                    mListener.onFialureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }


    public void logout() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
            riderActiveStatusRequest.setUid(new SessionManager(context).getRiderProfileResponse().getData().getUid());
            RiderActiveStatusRequest.AvailableStatus availableStatus = new RiderActiveStatusRequest.AvailableStatus();
            RiderActiveStatusRequest.UserAddInfo userAddInfo = new RiderActiveStatusRequest.UserAddInfo();
            availableStatus.setUid("offline");
            userAddInfo.setAvailableStatus(availableStatus);
            riderActiveStatusRequest.setUserAddInfo(userAddInfo);

            Gson gson = new Gson();
            String jsonriderRequest = gson.toJson(riderActiveStatusRequest);
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(AppConstants.BASE_URL + "api/user/save-update/update-rider-available-status");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer " + new SessionManager(context).getLoginToken());
            getDetailsRequest.setRequesttype("POST");
            getDetailsRequest.setRequestjson(jsonriderRequest);

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
                                new SessionManager(context).setRiderActiveStatus("offline");
                                mListener.onLogout();
                            } else if (response.code() == 401) {
                                ActivityUtils.hideDialog();
                                mListener.onLogout();

                            }
                        }
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    Utlis.INSTANCE.hideLoading();

                    System.out.println("RIDER ACTIVE STATUS ===========" + t.getMessage());
                }
            });

        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

}
