package com.apollopharmacy.vishwam.util;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

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

    public static final boolean IS_LOG_ENABLED = true;

    public  static  ArrayList<NewTicketHistoryResponse.Row> historytransferredarray=new ArrayList<>();

    public static void printMessage(String tag, String message) {
        if (IS_LOG_ENABLED) {
            Log.e(tag, message);
        }
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static String getCurrentDate() {
        String currDate = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            currDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        }
        return currDate;
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
            format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            format2 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
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
//        String secsLbl = "";
//        if (elapsedSeconds == 0 || elapsedSeconds == 1) {
//            secsLbl = elapsedSeconds + " Sec";
//        } else {
//            secsLbl = elapsedSeconds + " Secs";
//        }
        resultTime = hrsLbl + " " + minsLbl;
        return resultTime;
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

    public static String getAttendanceCurrentDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        } else {
            return "";
        }
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
                maskedStr = maskedStr + "" + Character.toUpperCase(c);
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
}
