package com.apollopharmacy.vishwam.ui.rider.neworder;

import android.content.Context;


import com.apollopharmacy.vishwam.network.ApiClient;
import com.apollopharmacy.vishwam.network.ApiInterface;
import com.apollopharmacy.vishwam.ui.rider.neworder.model.OrderDetailsRequest;
import com.apollopharmacy.vishwam.ui.rider.neworder.model.OrderDetailsResponse;
import com.apollopharmacy.vishwam.ui.rider.service.NetworkUtils;
import com.apollopharmacy.vishwam.util.Utlis;
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewOrderActivityController {
    private Context context;
    private NewOrderActivityCallback mListener;

    public NewOrderActivityController(Context context, NewOrderActivityCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public void orderDetailsApiCall(String token, String orderNumber) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            Utlis.INSTANCE.showLoading(context);
            OrderDetailsRequest orderDetailsRequest = new OrderDetailsRequest();
            orderDetailsRequest.setOrderNumber(orderNumber);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<OrderDetailsResponse> call = apiInterface.ORDER_DETAILS_API_CALL("Bearer " + token, orderNumber);
            call.enqueue(new Callback<OrderDetailsResponse>() {
                @Override
                public void onResponse(@NotNull Call<OrderDetailsResponse> call, @NotNull Response<OrderDetailsResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessOrderDetailsApiCall(response.body());
                    } else {
                        mListener.onFialureMessage("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderDetailsResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }
}
