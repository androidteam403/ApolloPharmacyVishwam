package com.apollopharmacy.vishwam.ui.rider.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;


import com.apollopharmacy.vishwam.network.ApiClient;
import com.apollopharmacy.vishwam.network.ApiInterface;
import com.apollopharmacy.vishwam.ui.rider.db.SessionManager;
import com.apollopharmacy.vishwam.ui.rider.login.BackSlash;
import com.apollopharmacy.vishwam.ui.rider.login.model.GetDetailsRequest;
import com.apollopharmacy.vishwam.util.AppConstants;
import com.apollopharmacy.vishwam.util.Utlis;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BatteryLevelLocationService extends Service implements LocationListener {
    protected LocationManager locationManager;
    private Location currentLocation;
    private Handler batterLevelLocationHandler;
    private Runnable batterLevelLocationRunnable;
    private double distanceTravelled;
    public  final String BASE_URL = "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollo_rider/";

    @Override
    public void onCreate() {
        super.onCreate();
        distanceTravelled = Double.parseDouble(getSessionManager().getRiderTravelledDistanceinDay());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 5, this);
        batteryPercentage();

        riderTotalTravelledDistanceinaDay();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void batteryPercentage() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
            int percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            if (currentLocation != null) {
                riderLatlangBatteryStatusApi(new SessionManager(this).getLoginToken(), String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), String.valueOf(percentage));
            }
        }
        batterLevelLocationHandler = new Handler();
        batterLevelLocationRunnable = this::batteryPercentage;
        batterLevelLocationHandler.postDelayed(batterLevelLocationRunnable, 60000);

//        riderTotalTravelledDistanceinaDay();
    }


    Location preLocation = null;

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null)
            this.currentLocation = location;
//        if (preLocation != null)
//            todayTravelledDistance();
//        else
//            preLocation = location;
    }

//    private void todayTravelledDistance() {
//        LatLng latLng1 = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//        LatLng from = new LatLng(preLocation.getLatitude(), preLocation.getLongitude());
//        LatLng to = new LatLng(latLng1.latitude, latLng1.longitude);
//        DrawRouteMaps.getInstance(this, this, this, this).draw(from, to, null, 0);
//    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();
    }

//    public void riderLatlangBatteryStatusApi(String token, String lattitude, String langitude, String batteryPercentage) {
//        if (NetworkUtils.isNetworkConnected(this)) {
//            RiderLatlangBatteryStatusRequest riderLatlangBatteryStatusRequest = new RiderLatlangBatteryStatusRequest();
//            RiderLatlangBatteryStatusRequest.UserAddInfo userAddInfo = new RiderLatlangBatteryStatusRequest.UserAddInfo();
//            userAddInfo.setBatteryStatus(batteryPercentage);
//            userAddInfo.setLat(lattitude);
//            userAddInfo.setLong(langitude);
//            userAddInfo.setLastActive(ActivityUtils.getCurrentTimeDate());
//            riderLatlangBatteryStatusRequest.setUserAddInfo(userAddInfo);
//
//            ApiInterface apiInterface = ApiClient.getApiService();
//            Call<RiderLalangBatteryStatusResponse> call = apiInterface.RIDER_LALANG_BATTERY_STATUS_API_CALL("Bearer " + token, riderLatlangBatteryStatusRequest);
//            call.enqueue(new Callback<RiderLalangBatteryStatusResponse>() {
//                @Override
//                public void onResponse(@NotNull Call<RiderLalangBatteryStatusResponse> call, @NotNull Response<RiderLalangBatteryStatusResponse> response) {
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<RiderLalangBatteryStatusResponse> call, @NotNull Throwable t) {
//                    System.out.println("Battery latlang and last active status api error :::::::::::::::::::::::::::::::" + t.getMessage());
//                }
//            });
//        }
//    }


    public void riderLatlangBatteryStatusApi(String token, String lattitude, String langitude, String batteryPercentage) {
        if (NetworkUtils.isNetworkConnected(this)) {

            ApiInterface apiInterface = ApiClient.getApiService();
            com.apollopharmacy.vishwam.ui.rider.dummy.dashboard.model.RiderLatlangBatteryStatusRequest riderLatlangBatteryStatusRequest = new com.apollopharmacy.vishwam.ui.rider.dummy.dashboard.model.RiderLatlangBatteryStatusRequest();
            com.apollopharmacy.vishwam.ui.rider.dummy.dashboard.model.RiderLatlangBatteryStatusRequest.UserAddInfo userAddInfo = new com.apollopharmacy.vishwam.ui.rider.dummy.dashboard.model.RiderLatlangBatteryStatusRequest.UserAddInfo();
            userAddInfo.setBatteryStatus(batteryPercentage);
            userAddInfo.setLat(lattitude);
            userAddInfo.setLong(langitude);
            userAddInfo.setLastActive(Utlis.INSTANCE.getCurrentTimeDate());
            riderLatlangBatteryStatusRequest.setUserAddInfo(userAddInfo);
            Gson gson = new Gson();
            String jsonLoginRequest = gson.toJson(riderLatlangBatteryStatusRequest);
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(AppConstants.BASE_URL+"api/user/save-update/update-rider-lat-long-battery-status");
            getDetailsRequest.setRequestjson(jsonLoginRequest);
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer " + token);
            getDetailsRequest.setRequesttype("POST");
            Call<ResponseBody> calls = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            calls.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                            com.apollopharmacy.vishwam.ui.rider.dummy.dashboard.model.RiderLalangBatteryStatusResponse riderLalangBatteryStatusResponse = gson.fromJson(BackSlash.removeSubString(res), com.apollopharmacy.vishwam.ui.rider.dummy.dashboard.model.RiderLalangBatteryStatusResponse.class);
                            if (riderLalangBatteryStatusResponse != null && riderLalangBatteryStatusResponse.getData() != null && riderLalangBatteryStatusResponse.getSuccess()) {

                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("Battery latlang and last active status api error :::::::::::::::::::::::::::::::" + t.getMessage());
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        batterLevelLocationHandler.removeCallbacks(batterLevelLocationRunnable);
    }

    Double distances = distanceTravelled;
    Location previousLocation = null;

    private void riderTotalTravelledDistanceinaDay() {
        if (previousLocation != null && currentLocation != null) {
            LatLng previous = new LatLng(previousLocation.getLatitude(), previousLocation.getLongitude());
            LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            getTravelledDistance(previous, current);
        } else {
            if (previousLocation == null)
                previousLocation = currentLocation;
        }
        Handler todayRiderTravelledDistance = new Handler();
        todayRiderTravelledDistance.postDelayed(this::riderTotalTravelledDistanceinaDay, 3000);
    }

    public void getTravelledDistance(LatLng previousLocation, LatLng currentLocationss) {
        Location locationA = new Location("point A");
        locationA.setLatitude(previousLocation.latitude);
        locationA.setLongitude(previousLocation.latitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(currentLocationss.latitude);
        locationB.setLongitude(currentLocationss.latitude);
        distanceTravelled = Double.parseDouble(getSessionManager().getRiderTravelledDistanceinDay());
        distances = distanceTravelled;
        if (locationA.distanceTo(locationB) > 5 && locationA.distanceTo(locationB) < 15) {
//            Toast.makeText(this, "" + locationA.distanceTo(locationB), Toast.LENGTH_LONG).show();
            distances = distances + locationA.distanceTo(locationB);
            getSessionManager().setRiderTravelledDistanceinDay(String.valueOf(distances));
            this.previousLocation = this.currentLocation;
        }
    }

    private SessionManager getSessionManager() {
        return new SessionManager(this);
    }

//    @Override
//    public void onDirectionApi(int colorFlag, JSONArray jsonArray) {
//        String distance;
//        Float removing;
//        try {
//            if (jsonArray != null) {
//                distance = ((JSONObject) jsonArray.get(0)).getJSONObject("distance").getString("text");
//                removing = Float.parseFloat(distance.replace("\"", "").replace("m", ""));//Pattern.compile("\"").matcher(distance).replaceAll("");
//                distances = distances + Double.valueOf(removing);
//                getSessionManager().setRiderTravelledDistanceinDay(String.valueOf(distances));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public Polyline onTaskDone(boolean flag, Object... values) {
//        return null;
//    }
//
//    @Override
//    public Polyline onSecondTaskDone(boolean flag, Object... values) {
//        return null;
//    }
//
//    @Override
//    public void pointsFirst(List<LatLng> pionts) {
//
//    }
//
//    @Override
//    public void pointsSecond(List<LatLng> pionts) {
//
//    }
}