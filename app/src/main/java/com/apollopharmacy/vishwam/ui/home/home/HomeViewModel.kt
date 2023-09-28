package com.apollopharmacy.vishwam.ui.home.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ChampsAdminRepo
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.AdminModuleCallBack
import com.apollopharmacy.vishwam.util.AppConstants
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils
import com.apollopharmacy.vishwam.util.signaturepad.NetworkUtils
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HomeViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    var command = LiveEvent<CmsCommand>()

/*
    fun test(callback: HomeFragmentCallback) {
        val state = MutableLiveData<State>()

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = "https://live.machs.edu.sa/zc-v3.1-user-svc-boot/2.0/machs/general?data=hi"//""https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/machs/zc-setting"//"https://live.machs.edu.sa/zc-v3.1-user-svc-boot/2.0/machs/general?data=hi"//"https://live.machs.edu.sa/zc-v3.1-user-svc-boot/2.0/machs/zc-setting"
        var token = ""
       */
/* for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CHMP GET CATEGORIES LIST")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }*//*


        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ChampsAdminRepo.test(token,
                    baseUrl//"https://172.16.103.116/Apollo/Champs/getCategoryDetails"
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value != null) {
                        callback.onsuccessTest()
                        */
/*if (response.value.status!!) {
                            for(i in response.value.categoryDetails!!){
                                if(i.rating!=null && !i.rating!!.isEmpty()){
                                    i.sumOfSubCategoryRating=i.rating!!.toDouble()
                                }

                            }
                            callback.onsuccessTest()
//                            mCallback.onSuccessGetCategoryDetailsApiCall(response.value)
                        } else {
//                            mCallback.onFailureGetCategoryDetailsApiCall(response.value.message!!)
                        }*//*

                    } else {
                    }
                }
                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
    }
*/


    sealed class CmsCommand {
        data class ShowToast(val message: String) : CmsCommand()
    }

    //    public void getRiderProfileDetailsApi(String token) {
    //        if (NetworkUtils.isNetworkConnected(context)) {
    //            ActivityUtils.showDialog(context, "Please wait.");
    //            ApiInterface apiInterface = ApiClient.getApiService();
    //            Call<GetRiderProfileResponse> call = apiInterface.GET_RIDER_PROFILE_API_CALL("Bearer " + token);
    //            call.enqueue(new Callback<GetRiderProfileResponse>() {
    //                @Override
    //                public void onResponse(@NotNull Call<GetRiderProfileResponse> call, @NotNull Response<GetRiderProfileResponse> response) {
    //                    ActivityUtils.hideDialog();
    //                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
    //                        mListener.onSuccessGetProfileDetailsApi(response.body());
    //                    } else if (response.code() == 401) {
    //                        ActivityUtils.showDialog(context, "Please wait.");
    //                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
    //                        refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
    //                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
    //                        call1.enqueue(new Callback<LoginResponse>() {
    //                            @Override
    //                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
    //                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
    //                                    new SessionManager(context).setLoginToken(response.body().getData().getToken());
    //                                    getRiderProfileDetailsApi(response.body().getData().getToken());
    //                                } else if (response.code() == 401) {
    //                                    logout();
    //                                } else {
    //                                    mListener.onFialureMessage("Please try again");
    //                                }
    //                            }
    //
    //                            @Override
    //                            public void onFailure(@NotNull Call<LoginResponse> call1, @NotNull Throwable t) {
    //                                ActivityUtils.hideDialog();
    //                                mListener.onFialureMessage("Please try again");
    //                                System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
    //                            }
    //                        });
    //                    } else {
    //                        mListener.onFialureMessage("No data found.");
    //                    }
    //                }
    //
    //                @Override
    //                public void onFailure(@NotNull Call<GetRiderProfileResponse> call, @NotNull Throwable t) {
    //                    ActivityUtils.hideDialog();
    //                    mListener.onFialureMessage(t.getMessage());
    //                }
    //            });
    //        } else {
    //            mListener.onFialureMessage("Something went wrong.");
    //        }
    //    }
    //    public void riderUpdateStauts(String token, String activieStatus) {
    //        if (NetworkUtils.isNetworkConnected(context)) {
    //            if (!ActivityUtils.isLoadingShowing())
    //                ActivityUtils.showDialog(context, "Please wait.");
    //            ApiInterface apiInterface = ApiClient.getApiService();
    //
    //            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
    //            riderActiveStatusRequest.setUid(new SessionManager(context).getRiderProfileResponse().getData().getUid());
    //            RiderActiveStatusRequest.UserAddInfo userAddInfo = new RiderActiveStatusRequest.UserAddInfo();
    //            RiderActiveStatusRequest.AvailableStatus availableStatus = new RiderActiveStatusRequest.AvailableStatus();
    //            availableStatus.setUid(activieStatus);
    //            userAddInfo.setAvailableStatus(availableStatus);
    //            riderActiveStatusRequest.setUserAddInfo(userAddInfo);
    //
    //            Call<RiderActiveStatusResponse> call = apiInterface.RIDER_ACTIVE_STATUS_API_CALL("Bearer " + token, riderActiveStatusRequest);
    //            call.enqueue(new Callback<RiderActiveStatusResponse>() {
    //                @Override
    //                public void onResponse(@NotNull Call<RiderActiveStatusResponse> call, @NotNull Response<RiderActiveStatusResponse> response) {
    //                    ActivityUtils.hideDialog();
    //                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
    //                        new SessionManager(context).setRiderActiveStatus(activieStatus);
    //                    } else if (response.code() == 401) {
    //                        ActivityUtils.showDialog(context, "Please wait.");
    //                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
    //                        refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
    //                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
    //                        call1.enqueue(new Callback<LoginResponse>() {
    //                            @Override
    //                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
    //                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
    //                                    new SessionManager(context).setLoginToken(response.body().getData().getToken());
    //                                    riderUpdateStauts(response.body().getData().getToken(), activieStatus);
    //                                } else if (response.code() == 401) {
    //                                    logout();
    //                                } else {
    //                                    mListener.onFialureMessage("Please try again");
    //                                }
    //                            }
    //
    //                            @Override
    //                            public void onFailure(@NotNull Call<LoginResponse> call1, @NotNull Throwable t) {
    //                                ActivityUtils.hideDialog();
    //                                mListener.onFialureMessage("Please try again");
    //                                System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
    //                            }
    //                        });
    //                    }
    //                }
    //
    //                @Override
    //                public void onFailure(@NotNull Call<RiderActiveStatusResponse> call, @NotNull Throwable t) {
    //                    ActivityUtils.hideDialog();
    //                    System.out.println("RIDER ACTIVE STATUS ==============" + t.getMessage());
    //                }
    //            });
    //        } else {
    //            mListener.onFialureMessage("Something went wrong.");
    //        }
    //    }
    //    public void getRiderDashboardCountsApiCall() {
    //        if (NetworkUtils.isNetworkConnected(context)) {
    //            ActivityUtils.showDialog(context, "Please wait.");
    //            ApiInterface apiInterface = ApiClient.getApiService();
    //
    //            Call<RiderDashboardCountResponse> call = apiInterface.GET_RIDER_DASHBOARD_COUNTS_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), CommonUtils.getBeforeSevenDaysDate(), CommonUtils.getCurrentDate());//CommonUtils.getfromDate() + "-01"
    //            call.enqueue(new Callback<RiderDashboardCountResponse>() {
    //                @Override
    //                public void onResponse(@NotNull Call<RiderDashboardCountResponse> call, @NotNull Response<RiderDashboardCountResponse> response) {
    //                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
    //                        mListener.onSuccessGetRiderDashboardCountApiCall(response.body());
    //                        getSessionManager().setCodReceived(String.valueOf(response.body().getData().getCount().getCodReceived()));
    //                        getSessionManager().setCodPendingDeposited(String.valueOf(response.body().getData().getCount().getCodPending()));
    //                    } else if (response.code() == 401) {
    //                        ActivityUtils.showDialog(context, "Please wait.");
    //                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
    //                        refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
    //                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
    //                        call1.enqueue(new Callback<LoginResponse>() {
    //                            @Override
    //                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
    //                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
    //                                    new SessionManager(context).setLoginToken(response.body().getData().getToken());
    //                                    getRiderDashboardCountsApiCall();
    //                                } else if (response.code() == 401) {
    //                                    logout();
    //                                } else {
    //                                    mListener.onFialureMessage("Please try again");
    //                                }
    //                            }
    //
    //                            @Override
    //                            public void onFailure(@NotNull Call<LoginResponse> call1, @NotNull Throwable t) {
    //                                ActivityUtils.hideDialog();
    //                                mListener.onFialureMessage("Please try again");
    //                                System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
    //                            }
    //                        });
    //                    } else {
    //                        ActivityUtils.hideDialog();
    //                        mListener.onFialureMessage("No Data Found");
    //                    }
    //                }
    //
    //                @Override
    //                public void onFailure(@NotNull Call<RiderDashboardCountResponse> call, @NotNull Throwable t) {
    //                    ActivityUtils.hideDialog();
    //                    mListener.onFialureMessage("No Data Found");
    //                    System.out.println("GET RIDER DASHBOARD COUNTS ==============" + t.getMessage());
    //                }
    //            });
    //        } else {
    //            mListener.onFialureMessage("Something went wrong.");
    //        }
    //    }
    //    public void logout() {
    //        if (NetworkUtils.isNetworkConnected(context)) {
    //            ApiInterface apiInterface = ApiClient.getApiService();
    //
    //            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
    //            riderActiveStatusRequest.setUid(new SessionManager(context).getRiderProfileResponse().getData().getUid());
    //            RiderActiveStatusRequest.UserAddInfo userAddInfo = new RiderActiveStatusRequest.UserAddInfo();
    //            RiderActiveStatusRequest.AvailableStatus availableStatus = new RiderActiveStatusRequest.AvailableStatus();
    //            availableStatus.setUid("Offline");
    //            userAddInfo.setAvailableStatus(availableStatus);
    //            riderActiveStatusRequest.setUserAddInfo(userAddInfo);
    //
    //            Call<RiderActiveStatusResponse> call = apiInterface.RIDER_ACTIVE_STATUS_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), riderActiveStatusRequest);
    //            call.enqueue(new Callback<RiderActiveStatusResponse>() {
    //                @Override
    //                public void onResponse(@NotNull Call<RiderActiveStatusResponse> call, @NotNull Response<RiderActiveStatusResponse> response) {
    //                    ActivityUtils.hideDialog();
    //                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
    //                        new SessionManager(context).setRiderActiveStatus("Offline");
    //                        mListener.onLogout();
    //                    } else if (response.code() == 401) {
    //                        mListener.onLogout();
    //                    }
    //                }
    //
    //                @Override
    //                public void onFailure(@NotNull Call<RiderActiveStatusResponse> call, @NotNull Throwable t) {
    //                    ActivityUtils.hideDialog();
    //                    System.out.println("RIDER ACTIVE STATUS ==============" + t.getMessage());
    //                }
    //            });
    //        } else {
    //            mListener.onFialureMessage("Something went wrong.");
    //        }
    //    }

}

