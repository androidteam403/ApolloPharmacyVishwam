package com.apollopharmacy.vishwam.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.data.model.cms.NewTicketHistoryResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Utils {
    public static String ORDER_DETAILS_RESPONSE = "ORDER_DETAILS_RESPONSE";
    public static String CURRENT_SCREEN = "";
    public static int NOTIFICATIONS_COUNT = 0;
    public static boolean is_order_delivery_screen = false;
    public static boolean isIs_order_delivery_or_track_map_screen = false;
    public static final int ONLINE_PAYMENT_ACTIVITY = 151;
    public static String selectedTab = "";
    public static boolean isMyOrdersListApiCall = false;
    public static final boolean IS_LOG_ENABLED = true;

    public static ArrayList<NewTicketHistoryResponse.Row> historytransferredarray = new ArrayList<>();


    public static SpannableString convertSpannableStringSizes(Context context, String defaultStr, String resultCnt, String resultsTxt, String inputSearchTxt,
                                                              float headerSize, float descSize, int font) {
        Typeface typeface = ResourcesCompat.getFont(context, font);
        SpannableString stringDefault = new SpannableString(defaultStr);
        stringDefault.setSpan(new RelativeSizeSpan(descSize), 0, stringDefault.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString stringResultCnt = new SpannableString(resultCnt);
        stringResultCnt.setSpan(new RelativeSizeSpan(headerSize), 0, stringResultCnt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringResultCnt.setSpan(new StyleSpan(typeface.getStyle()), 0, stringResultCnt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString stringResultsTxt = new SpannableString(resultsTxt);
        stringResultsTxt.setSpan(new RelativeSizeSpan(descSize), 0, stringResultsTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString stringSearchTxt = new SpannableString(inputSearchTxt);
        stringSearchTxt.setSpan(new RelativeSizeSpan(headerSize), 0, stringSearchTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringSearchTxt.setSpan(new StyleSpan(typeface.getStyle()), 0, stringSearchTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return new SpannableString(TextUtils.concat(stringDefault, stringResultCnt, stringResultsTxt, stringSearchTxt));
    }

    public static void printMessage(String tag, String message) {
        if (IS_LOG_ENABLED) {
            Log.e(tag, message);
        }
    }

    public static String getCurrentDateTimeMSUnique() {
        Date dNow = new Date();
        java.text.SimpleDateFormat ft = new java.text.SimpleDateFormat("mmss");
        String datetime = ft.format(dNow);
        return datetime;
    }

    public static String getCurrentTime() {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        return dateFormat.format(new Date()).toString();
    }

    public static String getCurrentTimeDate() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);//new ProgressDialog(context);
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        if (context instanceof Activity) {
            if (!((Activity) context).isFinishing())
                progressDialog.show();
        }
        return progressDialog;
    }

    public static void showTextDownAnimation(int targetId, LinearLayout parentLayout, TextView childText) {
        Transition transition = new Slide(Gravity.TOP);
        transition.setDuration(1000);
        transition.addTarget(targetId);
        TransitionManager.beginDelayedTransition(parentLayout, transition);
        childText.setVisibility(View.VISIBLE);
    }

    public static String getCurrentDate() {
        String currDate = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            currDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        }
        return currDate;
    }

    public static String getTodayDate() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public static String getOneWeekBackDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        String currDate = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            currDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(cal.getTime());
        }
        return currDate;
    }

    public static String getHalfMonthBackDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -14);
        String currDate = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            currDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(cal.getTime());
        }
        return currDate;
    }

    public static String getCustomHalfMonthDate(String inputDate) {
        SimpleDateFormat sdf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        }
        Calendar cal = Calendar.getInstance();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                cal.setTime(sdf.parse(inputDate));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DATE, 14);
        String currDate = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(cal.getTime());
        }
        return currDate;
    }

    public static String getCustomDate(String orderedDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(orderedDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String getDurationTimeFormat(String duration) {
        String resultStr = "";
        try {
            String[] s = duration.split(":");
            int hrValue = Integer.parseInt(s[0]);
            int minValue = Integer.parseInt(s[1]);
            int secValue = Integer.parseInt(s[2]);
            String hrsLbl = "";
            if (hrValue == 0 || hrValue == 1) {
                hrsLbl = hrValue + " Hr";
            } else {
                hrsLbl = hrValue + " Hrs";
            }
            String minsLbl = "";
            if (minValue == 0 || minValue == 1) {
                minsLbl = minValue + " Min";
            } else {
                minsLbl = minValue + " Mins";
            }
            String secsLbl = "";
            if (secValue == 1) {
                secsLbl = secValue + " Sec";
            } else {
                secsLbl = secValue + " Secs";
            }
            resultStr = hrsLbl + " " + minsLbl + " " + secsLbl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }
    public static String getHistoryCustomDateNew(String orderedDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(orderedDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
    public static String getHistoryTimeFormat(String orderedDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("hh:mm:ss aa", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(orderedDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String getHistoryCustomDate(String orderedDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("dd-MM-yyyy hh:mma", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(orderedDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String getticketlistfiltersdate(String orderedDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(orderedDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String dateofoccurence(String orderedDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(orderedDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String getHistoryDurationTimeFormat(String duration) {
        String resultStr = "";
        try {
            String[] s = duration.split(":");
            resultStr = Integer.parseInt(s[0]) + "H " + Integer.parseInt(s[1]) + "M " + Integer.parseInt(s[2]) + "S";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    @SuppressLint("NewApi")
    public static String getDurationTime(String currentDate, String previousDate) {
        String resultTime = "";
        DateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH);
        Date dateCurrent = null;
        Date dateBefore = null;
        try {
            dateCurrent = simpleDateFormat.parse(currentDate);
            dateBefore = simpleDateFormat.parse(previousDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert dateCurrent != null;
        assert dateBefore != null;
        long timeDifference = dateCurrent.getTime() - dateBefore.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedHours = timeDifference / hoursInMilli;
        timeDifference = timeDifference % hoursInMilli;
        long elapsedMinutes = timeDifference / minutesInMilli;
        long elapsedSeconds = timeDifference % 60;
        String hrsLbl = "";
        if (elapsedHours == 0 || elapsedHours == 1) {
            hrsLbl = String.valueOf(elapsedHours);
        } else {
            hrsLbl = String.valueOf(elapsedHours);
        }
        String minsLbl = "";
        if (elapsedMinutes == 0 || elapsedMinutes == 1) {
            minsLbl = elapsedMinutes + " Min";
        } else {
            minsLbl = elapsedMinutes + " Mins";
        }
        String secsLbl = "";
        if (elapsedSeconds == 0 || elapsedSeconds == 1) {
            secsLbl = elapsedSeconds + " Sec";
        } else {
            secsLbl = elapsedSeconds + " Secs";
        }
        resultTime = hrsLbl;
        return resultTime;
    }
    public static String getDurationTimMin(String currentDate, String previousDate) {
        String resultTime = "";
        DateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH);
        Date dateCurrent = null;
        Date dateBefore = null;
        try {
            dateCurrent = simpleDateFormat.parse(currentDate);
            dateBefore = simpleDateFormat.parse(previousDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert dateCurrent != null;
        assert dateBefore != null;
        long timeDifference = dateCurrent.getTime() - dateBefore.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedHours = timeDifference / hoursInMilli;
        timeDifference = timeDifference % hoursInMilli;
        long elapsedMinutes = timeDifference / minutesInMilli;
        long elapsedSeconds = timeDifference % 60;
        String hrsLbl = "";
        if (elapsedHours == 0 || elapsedHours == 1) {
            hrsLbl = elapsedHours + " Hr";
        } else {
            hrsLbl = elapsedHours + " Hrs";
        }
        String minsLbl = "";
        if (elapsedMinutes == 0 || elapsedMinutes == 1) {
            minsLbl = String.valueOf(elapsedMinutes);
        } else {
            minsLbl = String.valueOf(elapsedMinutes);
        }
        String secsLbl = "";
        if (elapsedSeconds == 0 || elapsedSeconds == 1) {
            secsLbl = String.valueOf(elapsedSeconds);
        } else {
            secsLbl = String.valueOf(elapsedSeconds);
        }
        resultTime =  minsLbl;
        return resultTime;
    }

    @SuppressLint("NewApi")
    public static String getDurationTimeSec(String currentDate, String previousDate) {
        String resultTime = "";
        DateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH);
        Date dateCurrent = null;
        Date dateBefore = null;
        try {
            dateCurrent = simpleDateFormat.parse(currentDate);
            dateBefore = simpleDateFormat.parse(previousDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert dateCurrent != null;
        assert dateBefore != null;
        long timeDifference = dateCurrent.getTime() - dateBefore.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedHours = timeDifference / hoursInMilli;
        timeDifference = timeDifference % hoursInMilli;
        long elapsedMinutes = timeDifference / minutesInMilli;
        long elapsedSeconds = timeDifference % 60;
        String hrsLbl = "";
        if (elapsedHours == 0 || elapsedHours == 1) {
            hrsLbl = elapsedHours + " Hr";
        } else {
            hrsLbl = elapsedHours + " Hrs";
        }
        String minsLbl = "";
        if (elapsedMinutes == 0 || elapsedMinutes == 1) {
            minsLbl = elapsedMinutes + " Min";
        } else {
            minsLbl = elapsedMinutes + " Mins";
        }
        String secsLbl = "";
        if (elapsedSeconds == 0 || elapsedSeconds == 1) {
            secsLbl = String.valueOf(elapsedSeconds);
        } else {
            secsLbl = String.valueOf(elapsedSeconds);
        }
        resultTime =  secsLbl;
        return resultTime;
    }

    public static String getLastLoginDateNew(String loginDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("dd MMM yyyy, hh:mm aa", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(loginDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String getLastLoginDateNewFormat(String loginDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(loginDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
    public static String getLastLoginDateNewFormatTime(String loginDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("hh:mm:ss aa", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(loginDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }


    public static String getLastLoginDateNewinSec(String loginDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("dd MMM yyyy, hh:mm:ss aa", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(loginDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String getLastLoginDate(String loginDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(loginDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String timeCoversion12to24(String loginDate) throws ParseException {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("hh:mm:ssaa", Locale.ENGLISH);
            format2 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(loginDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;

    }

    public static String dateCoversiontoTime(String twelveHoursTime) throws ParseException {

        //Date/time pattern of input date (12 Hours format - hh used for 12 hours)
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");

        //Date/time pattern of desired output date (24 Hours format HH - Used for 24 hours)
        DateFormat outputformat = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        String output = null;

        //Returns Date object
        date = df.parse(twelveHoursTime);

        //old date format to new date format
        output = outputformat.format(date);
        System.out.println(output);

        return output;
    }


    public static String getAttendanceCurrentDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        } else {
            return "";
        }
    }

    public static String getAttendanceCurrentDateNewFormat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        } else {
            return "";
        }
    }
    public static String getAttendanceCurrentDateNewFormatTime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return new SimpleDateFormat("hh:mm:ss aa", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        } else {
            return "";
        }
    }

    public static String getAttendanceCurrentDateNew() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return new SimpleDateFormat("dd MMM yyyy, hh:mm:ss aa", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        } else {
            return "";
        }
    }
    public static String getAttendanceCustomDateFormat(String orderedDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(orderedDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
    public static String getAttendanceCustomTimeFormat(String orderedDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("hh:mm:ss aa", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(orderedDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String getAttendanceCustomDate(String orderedDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(orderedDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }


    public static int getDateDifference(String fromDate, String toDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = dateFormat.parse(fromDate);
            date2 = dateFormat.parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        if (calendar1.after(calendar2)) {
            Utils.printMessage("Util", "Date1 is after Date2");
            return 0;
        }
        if (calendar1.before(calendar2)) {
            Utils.printMessage("Util", "Date1 is before Date2");
            return 1;
        }
        if (calendar1.equals(calendar2)) {
            Utils.printMessage("Util", "Date1 is equal Date2");
            return 1;
        }
        return 0;
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public static String getCurrentAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String add = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
//            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare();
            Utils.printMessage("IGA", "Address" + add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return add;
        }
        return add;

//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(context, Locale.getDefault());
//        String address = ""; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = "";
//        String state = "";
//        String country = "";
//        String postalCode = "";
//        String knownName = "";
//        try {
//            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            address = addresses.get(0).getAddressLine(0);
//            city = addresses.get(0).getLocality();
//            state = addresses.get(0).getAdminArea();
//            country = addresses.get(0).getCountryName();
//            postalCode = addresses.get(0).getPostalCode();
//            knownName = addresses.get(0).getFeatureName();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return address;
    }

    public static String getCityName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String add = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address obj = addresses.get(0);
            add = obj.getLocality();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return add;
        }
        return add;
    }

    public static String getStateName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String add = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address obj = addresses.get(0);
            add = obj.getAdminArea();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return add;
        }
        return add;
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getMaskedNumber(String mobileNum) {
        String maskedNum = "";
        for (int i = 0; i < mobileNum.length(); i++) {
            if (i < 6) {
                maskedNum = "XXXXXX";
            } else {
                maskedNum = maskedNum + "" + mobileNum.charAt(i);
            }
        }
        return maskedNum;
    }

    public static String getMaskedEmpID(String employeeID) {
        String maskedStr = "";
        for (char c : employeeID.toCharArray()) {
            if (Character.isLetter(c)) {
                //  maskedStr = maskedStr + "" + Character.toUpperCase(c);
            } else {
                maskedStr = maskedStr + "X";
            }
        }
        return maskedStr;
    }

    public static String getNotificationDate(String notifiDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(notifiDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String getNotificationTime(String notifiTime) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            format2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(notifiTime);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String getAttendanceLocation(Context context, Double latitude, Double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1);
        } catch (Exception ioException) {
            printMessage("", "Error in getting address for the location");
            return "No address found";
        }
        if (addresses == null || addresses.size() == 0) {
            return "No address found";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0);
        }
    }

    public static String getAttendanceCity(Context context, Double latitude, Double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1);
        } catch (Exception ioException) {
            return "No city found";
        }
        try {
            if (addresses == null || addresses.size() == 0) {
                return "No city found";
            } else {
                Address address = addresses.get(0);
                return address.getLocality();
            }
        } catch (Exception e) {
            return "No city found";
        }
    }

    public static String getAttendanceState(Context context, Double latitude, Double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1);
        } catch (Exception ioException) {
            return "No state found";
        }
        try {
            if (addresses == null || addresses.size() == 0) {
                return "No state found";
            } else {
                Address address = addresses.get(0);
                return address.getAdminArea();
            }
        } catch (Exception e) {
            return "No state found";
        }
    }

    public static String getFilterDate(String inputDate) {
        SimpleDateFormat format1 = null;
        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        }
        String convertedDate = "";
        try {
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                date = format1.parse(inputDate);
                convertedDate = format2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static int getMonth(String date) throws ParseException {
        Date d = null;
        int monthNum = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            d = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            monthNum = Integer.parseInt(new SimpleDateFormat("MM", Locale.ENGLISH).format(cal.getTime()));
        }
        return monthNum;
    }

    public static String getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//2023-06-30
        System.out.println(sdf.format(cal.getTime()));
        return sdf.format(cal.getTime());
    }

    public static String getCurrentDateCeoDashboard() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//2023-06-30
        System.out.println(sdf.format(cal.getTime()));
        return sdf.format(cal.getTime());
    }

    public static String getConvertedDateFormatddmmmyyyy(String dateString) {
        String dtStart = dateString;//"2010-10-15T09:27:37Z";
        SimpleDateFormat formatBeforeConvert = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //Before convert
            Date dateBeforeConvert = formatBeforeConvert.parse(dtStart);
            System.out.println(dateBeforeConvert);


            //After covert
            SimpleDateFormat formatAfterConvert = new SimpleDateFormat("dd-MMM-yyyy");
            String dateTime = formatAfterConvert.format(dateBeforeConvert);
            return dateTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }


       /* Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");//2023-06-30
        System.out.println(sdf.format(cal.getTime()));
        return sdf.format(cal.getTime());*/
    }

    public static String getConvertedDateFormatyyyymmdd(String dateString) {
        String dtStart = dateString;//"2010-10-15T09:27:37Z";
        SimpleDateFormat formatBeforeConvert = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            //Before convert
            Date dateBeforeConvert = formatBeforeConvert.parse(dtStart);
            System.out.println(dateBeforeConvert);


            //After covert
            SimpleDateFormat formatAfterConvert = new SimpleDateFormat("yyyy-MM-dd");
            String dateTime = formatAfterConvert.format(dateBeforeConvert);
            return dateTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
