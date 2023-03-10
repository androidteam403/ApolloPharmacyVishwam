package com.apollopharmacy.vishwam.ui.rider.reports;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.databinding.FragmentReportsBinding;
import com.apollopharmacy.vishwam.ui.home.MainActivity;
import com.apollopharmacy.vishwam.ui.rider.base.BaseFragment;
import com.apollopharmacy.vishwam.ui.rider.db.SessionManager;
import com.apollopharmacy.vishwam.ui.rider.login.LoginActivity;
import com.apollopharmacy.vishwam.ui.rider.reports.adapter.OrdersCodStatusAdapter;
import com.apollopharmacy.vishwam.ui.rider.reports.model.OrdersCodStatusResponse;
import com.apollopharmacy.vishwam.util.Utils;
import com.apollopharmacy.vishwam.util.Utlis;
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ReportsFragment extends BaseFragment implements ReportsFragmentCallback {
    private Activity mActivity;
    private FragmentReportsBinding reportsBinding;
    private OrdersCodStatusAdapter ordersCodStatusAdapter;
    private boolean isLoading = false;
    private List<OrdersCodStatusResponse.Row> ordersCodStatusList;
    private List<OrdersCodStatusResponse.Row> ordersCodStatusListLoad;
    private String fromDate = Utlis.INSTANCE.getBeforeSevenDaysDate();//CommonUtils.getfromDate() + "-01";
    private String toDate = Utils.getTodayDate();
    private int page = 1;
    private boolean isLastRecord = false;

    public static ReportsFragment newInstance() {
        return new ReportsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        reportsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reports, container, false);
        return reportsBinding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reportsBinding.setCallback(this);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date fromDates = formatter.parse(fromDate);
            long fromDateMills = fromDates.getTime();
            reportsBinding.fromDate.setText(Utlis.INSTANCE.getDateFormatForSummaryEditText(fromDateMills));
            Date toDates = formatter.parse(toDate);
            long toDateMills = toDates.getTime();
            reportsBinding.toDate.setText(Utlis.INSTANCE.getDateFormatForSummaryEditText(toDateMills));
        } catch (Exception e) {
            System.out.println("onViewCreated:::::::::::::::::" + e.getMessage());
        }
        page = 1;
        getController().getRiderDashboardCountsApiCall();
//        Double codReceived = Double.parseDouble(getSessionManager().getCodReceived());
//        Double codPendingDeposited = Double.parseDouble(getSessionManager().getCodPendingDeposited());
//        DecimalFormat decim = new DecimalFormat("#,###.##");
//        reportsBinding.codReceivedVal.setText(getActivity().getResources().getString(R.string.label_rupee_symbol) + " " + decim.format(codReceived));
//        reportsBinding.codPendingVal.setText(getActivity().getResources().getString(R.string.label_rupee_symbol) + " " + decim.format(codPendingDeposited));
    }

    @Override
    public void onFialureMessage(String message) {

    }

    @Override
    public void onSuccessOrdersCodStatusApiCall(OrdersCodStatusResponse ordersCodStatusResponse) {
        if (ordersCodStatusResponse.getData().getListData().getRows()!=null){
            this.ordersCodStatusListLoad=ordersCodStatusResponse.getData().getListData().getRows();
        }
        if (page == 1) {
            ActivityUtils.hideDialog();
            if (ordersCodStatusResponse != null && ordersCodStatusResponse.getData() != null
                    && ordersCodStatusResponse.getData().getListData() != null
                    && ordersCodStatusResponse.getData().getListData().getRows() != null
                    && ordersCodStatusResponse.getData().getListData().getRows().size() > 0) {
                page++;

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                reportsBinding.reportsRecycler.setLayoutManager(mLayoutManager);
                reportsBinding.reportsRecycler.setItemAnimator(new DefaultItemAnimator());
                reportsBinding.reportsRecycler.setVisibility(View.VISIBLE);
                reportsBinding.codreceivedCodpending.setVisibility(View.VISIBLE);
                reportsBinding.noReportsFoundLayout.setVisibility(View.GONE);
                initAdapter();
                initScrollListener();
            } else {
                reportsBinding.reportsRecycler.setVisibility(View.GONE);
                reportsBinding.codreceivedCodpending.setVisibility(View.GONE);
                reportsBinding.noReportsFoundLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (ordersCodStatusResponse != null && ordersCodStatusResponse.getData() != null
                    && ordersCodStatusResponse.getData().getListData() != null
                    && ordersCodStatusResponse.getData().getListData().getRows() != null
                    && ordersCodStatusResponse.getData().getListData().getRows().size() > 0) {
                page++;
                if (ordersCodStatusResponse.getData().getListData().getRows().size() < 10)
                    this.isLastRecord = true;
            } else if (ordersCodStatusResponse != null && ordersCodStatusResponse.getData() != null
                    && ordersCodStatusResponse.getData().getListData() != null
                    && ordersCodStatusResponse.getData().getListData().getRows() != null
                    && ordersCodStatusResponse.getData().getListData().getRows().size() == 0) {
                this.isLastRecord = true;
            }
            ordersCodStatusListLoad.remove(ordersCodStatusListLoad.size() - 1);
            int scrollPosition = ordersCodStatusListLoad.size();
            ordersCodStatusAdapter.notifyItemRemoved(scrollPosition);
            int currentSize = scrollPosition;
            int nextLimit = 10;

            ordersCodStatusListLoad.addAll(ordersCodStatusResponse.getData().getListData().getRows());
            ordersCodStatusAdapter.notifyDataSetChanged();
            isLoading = false;

        }
    }

    private void initAdapter() {
        ordersCodStatusAdapter = new OrdersCodStatusAdapter(getContext(), ordersCodStatusListLoad, this);
        reportsBinding.reportsRecycler.setAdapter(ordersCodStatusAdapter);
    }

    private void initScrollListener() {
        reportsBinding.reportsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == ordersCodStatusListLoad.size() - 1) {
                        //bottom of list!
                        if (!isLastRecord) {
                            loadMore();
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    private void loadMore() {
        ordersCodStatusListLoad.add(null);
        ordersCodStatusAdapter.notifyItemInserted(ordersCodStatusListLoad.size() - 1);
        getController().getOrdersCodStatusApiCall(page);
    }

    @Override
    public void onFailureOrdersCodStatusApiCall(String message) {

    }

    @Override
    public void onLogout() {
        getSessionManager().clearAllSharedPreferences();
       MainActivity.mInstance.stopBatteryLevelLocationService();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onSuccessGetRiderDashboardCountApiCall(com.apollopharmacy.vishwam.ui.rider.dummy.dashboard.model.RiderDashboardCountResponse riderDashboardCountResponse) {
        DecimalFormat decim = new DecimalFormat("#,###.##");
        reportsBinding.codReceivedVal.setText(getActivity().getResources().getString(R.string.label_rupee_symbol) + " " + decim.format(riderDashboardCountResponse.getData().getCount().getCodReceived()));
        reportsBinding.codPendingVal.setText(getActivity().getResources().getString(R.string.label_rupee_symbol) + " " + decim.format(riderDashboardCountResponse.getData().getCount().getCodPending()));
        getController().getOrdersCodStatusApiCall(page);
    }

    @Override
    public void onClickFromDate() {
        Calendar c = Calendar.getInstance(Locale.ENGLISH);
        int mYear;
        int mMonth;
        int mDay;
        //Objects.requireNonNull(summaryBinding.fromDate).getText().toString().isEmpty()
        if (fromDate.isEmpty()) {
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        } else {
            String selectedBirthDate = fromDate;
            String[] expDate = selectedBirthDate.split("-");
            mYear = Integer.parseInt(expDate[0]);
            mMonth = Integer.parseInt(expDate[1]) - 1;
            mDay = Integer.parseInt(expDate[2]);
        }
        final DatePickerDialog dialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            String selectedDate;
            c.set(year, monthOfYear, dayOfMonth);
            selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.getTime());
//            requiredDOBFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(c.getTime());
            try {
                reportsBinding.fromDate.setText(Utlis.INSTANCE.getDateFormatForSummaryEditText(c.getTimeInMillis()));
                this.fromDate = Utlis.INSTANCE.getSqlDateFormat(c);
                if (Utlis.INSTANCE.isFromDateBeforeToDate(selectedDate, toDate)) {
                    reportsBinding.dateComparisonErrorText.setVisibility(View.GONE);
                    onClickOk();
                } else {
                    reportsBinding.dateComparisonErrorText.setVisibility(View.VISIBLE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }, mYear, mMonth, mDay);
        dialog.getDatePicker().setMinDate((long) (System.currentTimeMillis()-2592000000L));
        dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis()));// - (1000 * 60 * 60 * 24 * 365.25 * 18)
        dialog.show();
    }

    @Override
    public void onClickToDate() {
        Calendar c = Calendar.getInstance(Locale.ENGLISH);
        int mYear;
        int mMonth;
        int mDay;
        //Objects.requireNonNull(summaryBinding.toDate).getText().toString().isEmpty()
        if (toDate.isEmpty()) {
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        } else {
            String selectedBirthDate = toDate;
            String[] expDate = selectedBirthDate.split("-");
            mYear = Integer.parseInt(expDate[0]);
            mMonth = Integer.parseInt(expDate[1]) - 1;
            mDay = Integer.parseInt(expDate[2]);
        }
        final DatePickerDialog dialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            String selectedDate;
            c.set(year, monthOfYear, dayOfMonth);
            selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.getTime());
//            requiredDOBFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(c.getTime());
            try {
                reportsBinding.toDate.setText(Utlis.INSTANCE.getDateFormatForSummaryEditText(c.getTimeInMillis()));
                this.toDate = Utlis.INSTANCE.getSqlDateFormat(c);
                if (Utlis.INSTANCE.isFromDateBeforeToDate(fromDate, selectedDate)) {
                    reportsBinding.dateComparisonErrorText.setVisibility(View.GONE);
                    onClickOk();
                } else {
                    reportsBinding.dateComparisonErrorText.setVisibility(View.VISIBLE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }, mYear, mMonth, mDay);
        dialog.getDatePicker().setMinDate((long) (System.currentTimeMillis()-2592000000L));
        dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis()));// - (1000 * 60 * 60 * 24 * 365.25 * 18)
        dialog.show();
    }

    @Override
    public void onClickOk() {
        if (Utlis.INSTANCE.isFromDateBeforeToDate(fromDate, toDate)) {
            reportsBinding.dateComparisonErrorText.setVisibility(View.GONE);
            page = 1;
            this.isLastRecord = false;
            getController().getRiderDashboardCountsApiCall();
        } else {
            reportsBinding.dateComparisonErrorText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public String fromDate() {
        return fromDate;
    }

    @Override
    public String toDate() {
        return toDate;
    }

    public SessionManager getSessionManager() {
        return new SessionManager(getContext());
    }

    private ReportsFragmentController getController() {
        return new ReportsFragmentController(getContext(), this);
    }
}
