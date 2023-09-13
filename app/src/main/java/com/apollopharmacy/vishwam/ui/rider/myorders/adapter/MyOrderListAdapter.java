package com.apollopharmacy.vishwam.ui.rider.myorders.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.databinding.AdapterMyordersListBinding;
import com.apollopharmacy.vishwam.ui.rider.myorders.model.MyOrdersListResponse;
import com.apollopharmacy.vishwam.util.Utlis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.ViewHolder> {
    private Context context;
    private List<MyOrdersListResponse.Row> myOrdersList;
    private MyOrdersListAdapterCallback mListener;

    public MyOrderListAdapter(Context context, List<MyOrdersListResponse.Row> myOrdersList, MyOrdersListAdapterCallback mListener) {
        this.context = context;
        this.myOrdersList = myOrdersList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterMyordersListBinding myordersListBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_myorders_list, parent, false);
        return new ViewHolder(myordersListBinding);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyOrdersListResponse.Row item = myOrdersList.get(position);
        holder.myordersListBinding.orderStatus.setText(" " + item.getOrderStatus().getName());
        holder.myordersListBinding.orderStatus.setTextColor(Color.parseColor(item.getOrderStatus().getOther().getColor()));
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date orderDates = formatter.parse(item.getCreatedTime());
            long orderDateMills = orderDates.getTime();
            holder.myordersListBinding.createdDate.setText(Utlis.INSTANCE.getTimeFormatter(orderDateMills));
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        holder.myordersListBinding.createdDate.setText(item.getOrderSh().get(0).getCreatedTime());
        holder.myordersListBinding.orderId.setText("#" + item.getOrderNumber());
        String orderDate = null;

        String customerAddress = "";
        if (item.getDeliverApartment() != null) {
            customerAddress = item.getDeliverApartment() + ", ";
        }
        if (item.getDeliverStreetName() != null) {
            customerAddress = customerAddress + item.getDeliverStreetName() + ", ";
        }
        if (item.getDeliverCity() != null) {
            customerAddress = customerAddress + item.getDeliverCity() + ", ";
        }
        if (item.getDeliverState() != null) {
            customerAddress = customerAddress + item.getDeliverState() + ", ";
        }
        if (item.getDelPincode() != null) {
            customerAddress = customerAddress + item.getDelPincode() + ", ";
        }
        if (item.getDeliverCountry() != null) {
            customerAddress = customerAddress + item.getDeliverCountry();
        }

        String pickupAddress = "";
        if (item.getPickupApt() != null) {
            pickupAddress = item.getPickupApt() + ", ";
        }
        if (item.getPickupStreetName() != null) {
            pickupAddress = pickupAddress + item.getPickupStreetName() + ", ";
        }
        if (item.getPickupCity() != null) {
            pickupAddress = pickupAddress + item.getPickupCity() + ", ";
        }
        if (item.getPickupState() != null) {
            pickupAddress = pickupAddress + item.getPickupState() + ", ";
        }
        if (item.getPickupPincode() != null) {
            pickupAddress = pickupAddress + item.getPickupPincode() + ", ";
        }
        if (item.getPickupCountry() != null) {
            pickupAddress = pickupAddress + item.getPickupCountry();
        }

        String returnAddress = "";
        if (item.getReturnApartment() != null) {
            returnAddress = item.getReturnApartment() + ", ";
        }
        if (item.getReturnStreetName() != null) {
            returnAddress = returnAddress + item.getReturnStreetName() + ", ";
        }
        if (item.getReturnCity() != null) {
            returnAddress = returnAddress + item.getReturnCity() + ", ";
        }
        if (item.getReturnState() != null) {
            returnAddress = returnAddress + item.getReturnState() + ", ";
        }
        if (item.getReturnPincode() != null) {
            returnAddress = returnAddress + item.getReturnPincode() + ", ";
        }
        if (item.getReturnCountry() != null) {
            returnAddress = returnAddress + item.getReturnCountry();
        }

        if (item.getOrderState().getName().equals("RETURN")) {
            if (item.getOrderStatus().getUid().equals("ORDERACCEPTED") || item.getOrderStatus().getUid().equals("ORDERUPDATE")) {
                holder.myordersListBinding.deliveredOnText.setText("Pickup by: ");
                orderDate = item.getPickupEtWindo();
            } else if (item.getOrderStatus().getUid().equals("RETURNORDERRTO")) {
                holder.myordersListBinding.deliveredOnText.setText("Delivered at: ");
                orderDate = item.getOrderSh().get(0).getCreatedTime();
            } else {
                holder.myordersListBinding.deliveredOnText.setText("Pickup by: ");
                orderDate = item.getPickupEtWindo();
            }
            // Pickup Address
            holder.myordersListBinding.pickupAccountName.setText(item.getPickupAccName());
            holder.myordersListBinding.pickupAddress.setText(pickupAddress);
            holder.myordersListBinding.pickupLandMark.setText(item.getPickupLndmrk());
            // Delivery Address
            holder.myordersListBinding.orderTxt.setText("R");
            holder.myordersListBinding.deliveryAccountName.setText(item.getReturnAccName());
            holder.myordersListBinding.deliveryAddress.setText(returnAddress);
            holder.myordersListBinding.deliveryLandMark.setText(item.getReturnLandmark());
        } else {
            if (item.getOrderStatus().getUid().equals("ORDERACCEPTED") || item.getOrderStatus().getUid().equals("ORDERUPDATE")) {
                holder.myordersListBinding.deliveredOnText.setText("Deliver by: ");
                orderDate = item.getDelEtWindo();
            } else if (item.getOrderStatus().getUid().equals("DELIVERED")) {
                holder.myordersListBinding.deliveredOnText.setText("Delivered at: ");
                orderDate = item.getOrderSh().get(0).getCreatedTime();
            } else {
                holder.myordersListBinding.deliveredOnText.setText("Deliver by: ");
                orderDate = item.getDelEtWindo();
            }
            // Pickup Address
            holder.myordersListBinding.pickupAccountName.setText(item.getPickupAccName());
            holder.myordersListBinding.pickupAddress.setText(pickupAddress);
            holder.myordersListBinding.pickupLandMark.setText(item.getPickupLndmrk());
            // Delivery Address
            holder.myordersListBinding.deliveryAccountName.setText(item.getDelAccName());
            holder.myordersListBinding.deliveryAddress.setText(customerAddress);
            holder.myordersListBinding.deliveryLandMark.setText(item.getDeliverLandmark());
        }
//        holder.summaryBinding.pharmaUserAddressLayout.setVisibility(View.VISIBLE);
//        holder.summaryBinding.userAddressLayout.setVisibility(View.GONE);
//        holder.summaryBinding.cancelledReasonLayout.setVisibility(View.GONE);
//        holder.summaryBinding.dividerViewFour.setBackgroundColor(context.getColor(R.color.order_item_divider_color));
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            String currentDate = Utlis.INSTANCE.getCurrentTimeDate();
            Date currentDates = formatter.parse(currentDate);
            Date orderDates = formatter.parse(orderDate);
            long orderDateMills = orderDates.getTime();
            holder.myordersListBinding.deliverBy.setText(Utlis.INSTANCE.getTimeFormatter(orderDateMills));
            if (currentDates.compareTo(orderDates) < 0) {
                System.out.println("orderDates is Greater than currentDates");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClickOrder(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myOrdersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterMyordersListBinding myordersListBinding;

        public ViewHolder(@NonNull AdapterMyordersListBinding myordersListBinding) {
            super(myordersListBinding.getRoot());
            this.myordersListBinding = myordersListBinding;
        }
    }

    public interface MyOrdersListAdapterCallback {
        void onClickOrder(MyOrdersListResponse.Row order);
    }
}