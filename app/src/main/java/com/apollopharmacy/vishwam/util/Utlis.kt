package com.apollopharmacy.vishwam.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.res.ResourcesCompat
import com.apollopharmacy.vishwam.util.signaturepad.DialogManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import dmax.dialog.SpotsDialog
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utlis {
    @kotlin.jvm.JvmField
    var ORDER_DETAILS_RESPONSE: String= "ORDER_DETAILS_RESPONSE"

    @kotlin.jvm.JvmField
    var NOTIFICATIONS_COUNT: Int=0

    @kotlin.jvm.JvmField
    var CURRENT_SCREEN: String=" "
    var mProgressDialog: ProgressDialog? = null
    var spotsDialog: SpotsDialog? = null

    fun getTimeFormatter(neededTimeMilis: Long): String? {
        val nowTime = Calendar.getInstance()
        val neededTime = Calendar.getInstance()
        neededTime.timeInMillis = neededTimeMilis
        return if (neededTime[Calendar.YEAR] == nowTime[Calendar.YEAR]) {
            if (neededTime[Calendar.MONTH] == nowTime[Calendar.MONTH]) {
                if (neededTime[Calendar.DATE] - nowTime[Calendar.DATE] == 1) {
                    //here return like "Tomorrow at 12:00"
                    SimpleDateFormat("MMM dd, yyyy hh:mm aa",
                        Locale.ENGLISH).format(neededTime.time) //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(neededTime.getTime());
                } else if (nowTime[Calendar.DATE] == neededTime[Calendar.DATE]) {
                    //here return like "Today at 12:00"
                    val time =
                        SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(neededTime.time)
                    val ampm: String
                    var times: String? = null
                    if (time.endsWith("a.m.") || time.endsWith("am")) {
                        ampm = "AM"
                        times =
                            if (time.endsWith("am")) time.substring(0,
                                time.length - 2) + ampm else time.substring(0,
                                time.length - 4) + ampm
                    } else {
                        ampm = "PM"
                        times =
                            if (time.endsWith("pm")) time.substring(0,
                                time.length - 2) + ampm else time.substring(0,
                                time.length - 4) + ampm
                    }
                    "Today at " + SimpleDateFormat("hh:mm a",
                        Locale.ENGLISH).format(neededTime.time) //times;
                } else if (nowTime[Calendar.DATE] - neededTime[Calendar.DATE] == 1) {
                    //here return like "Yesterday at 12:00"
                    SimpleDateFormat("MMM dd, yyyy hh:mm aa",
                        Locale.ENGLISH).format(neededTime.time) //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(neededTime.getTime());
                } else {
                    //here return like "May 31, 12:00"
                    SimpleDateFormat("MMM dd, yyyy hh:mm aa",
                        Locale.ENGLISH).format(neededTime.time) // new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(neededTime.getTime());
                }
            } else {
                //here return like "May 31, 12:00"
                SimpleDateFormat("MMM dd, yyyy hh:mm aa",
                    Locale.ENGLISH).format(neededTime.time) //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(neededTime.getTime());
            }
        } else {
            //here return like "May 31 2010, 12:00" - it's a different year we need to show it
            SimpleDateFormat("MMM dd, yyyy hh:mm aa",
                Locale.ENGLISH).format(neededTime.time) //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(neededTime.getTime());
        }
    }
    fun getfromDate(): String? {
        return SimpleDateFormat("yyyy-MM", Locale.ENGLISH).format(Date())
    }

    fun getDateFormatForSummaryEditText(c: Long): String? {
        val neededTime = Calendar.getInstance()
        neededTime.timeInMillis = c
        return SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(neededTime.time)
    }

    @Throws(ParseException::class)
    fun getSqlDateFormat(c: Calendar): String? {
        return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.time)
    }

    fun isFromDateBeforeToDate(fromDate: String?, toDate: String?): Boolean {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val dateFrom = formatter.parse(fromDate)
            val dateTo = formatter.parse(toDate)
            if (dateFrom.compareTo(dateTo) < 0 || dateFrom == dateTo) {
                println("date2 is Greater than my date1")
                true
            } else {
                false
            }
        } catch (e1: ParseException) {
            e1.printStackTrace()
            false
        }
    }
    fun hideKeyPad(activity: Activity) {
        activity.let {
            val imm: InputMethodManager =
                it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = it.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    fun getScreenSize(context: Context): DisplayMetrics? {
        val displayMetrics = DisplayMetrics()
        val mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mWindowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }


    fun showLoading(context: Context) {
        hideLoading()
        mProgressDialog = null
        mProgressDialog = Utils.showLoadingDialog(context)
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
            mProgressDialog!!.dismiss()
        }
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String? {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getDeviceName(): String? {
        return Build.MODEL
    }

//    @SuppressLint("SimpleDateFormat")
//    fun convertDate(DatefromServer: String): String {
//        val sourceFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
//        val destFormat = SimpleDateFormat("dd-MMM-yyyy")
//        val convertedDate: Date = sourceFormat.parse(DatefromServer)
//        return destFormat.format(convertedDate)
//    }

    fun convertDate(DatefromServer: String): String {
        // val sourceFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val sourceFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH)
        val destFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        // val destFormat = SimpleDateFormat("MMM-dd-yyyy")
        val convertedDate: Date = sourceFormat.parse(DatefromServer)
        return destFormat.format(convertedDate)
    }

    fun approvedConvertDate(DatefromServer: String): String {
        // val sourceFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val sourceFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH)
        val destFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        // val destFormat = SimpleDateFormat("MMM-dd-yyyy")
        val convertedDate: Date = sourceFormat.parse(DatefromServer)
        return destFormat.format(convertedDate)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateAddedTimeZone(DateFromServer: String): String {
        try {
            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val destFormat = SimpleDateFormat("dd-MMM-yyyy")
            val convertedDate: Date = sourceFormat.parse(DateFromServer)
            return destFormat.format(convertedDate)
        } catch (e: Exception) {
            return ""
        }
    }

    fun formatTheDate(dateToFormat: String): String {
        val sourceFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val destFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH)
        val convertedDate = sourceFormat.parse(dateToFormat)
        return destFormat.format(convertedDate)
    }

    fun filterDateFormate(dateForFilter: String): Date {
        val sourceFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        val destinationFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)

//        val dtStart = "2010-10-15T09:27:37Z"
        val format = SimpleDateFormat("dd-MMM-yyyy")
        try {
            val date = format.parse(dateForFilter)
            System.out.println(date)
            return date
        } catch (e: ParseException) {
            e.printStackTrace()
            return Date()
        }

        /*val convertedDate: Date = sourceFormat.parse(dateForFilter)
        return convertedDate*/
    }

    @SuppressLint("SimpleDateFormat")
    fun convertCmsDate(DatefromServer: String): String {
        val sourceFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val destFormat = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.ENGLISH)
        val convertedDate: Date = sourceFormat.parse(DatefromServer)
        return destFormat.format(convertedDate)
    }

    fun convertCmsExparyDate(DatefromServer: String): String {
        val sourceFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val destFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val convertedDate: Date = sourceFormat.parse(DatefromServer)
        return destFormat.format(convertedDate)
    }

    fun cmsComplaintDateFormat(dateToFormat: String): String {
        val sourceFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val destFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val convertedDate = sourceFormat.parse(dateToFormat)
        return destFormat.format(convertedDate)
    }
    fun getBeforeSevenDaysDate(): String? {
        return SimpleDateFormat("yyyy-MM-dd",
            Locale.ENGLISH).format(Date().time - 518400000L) //604800000L
    }
    fun getDateSevenDaysEarlier(pattern: String?): String? {
        val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)

//        Date date = new Date();
//        String todate = sdf.format(date);
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)
        val todate1 = cal.time
        return sdf.format(todate1)
    }


    fun getDatethirtyDays(pattern: String?): String? {
        val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)

//        Date date = new Date();
//        String todate = sdf.format(date);
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, 30)
        val todate1 = cal.time
        return sdf.format(todate1)
    }

    fun getCurrentDate(pattern: String?): String? {
        val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
        return sdf.format(Date())
    }
    fun getTodayDate(): String? {
        return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date())
    }
    fun convertSpannableStrings(
        header: String?,
        description: String?,
        headerSize: Float,
        descSize: Float,
        headerColor: Int,
        descColor: Int,
    ): SpannableString? {
        val stringHeader = SpannableString(header)
        stringHeader.setSpan(RelativeSizeSpan(headerSize),
            0,
            stringHeader.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        stringHeader.setSpan(ForegroundColorSpan(headerColor),
            0,
            stringHeader.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val StringDesc = SpannableString(description)
        StringDesc.setSpan(RelativeSizeSpan(descSize),
            0,
            StringDesc.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        StringDesc.setSpan(ForegroundColorSpan(descColor),
            0,
            StringDesc.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return SpannableString(TextUtils.concat(stringHeader, "\n", StringDesc))
    }
    fun getCurrentTimeDate(): String? {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(Date())
    }

    fun hideDialog() {
        try {
            if (spotsDialog!!.isShowing()) spotsDialog!!.dismiss()
        } catch (e: java.lang.Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    fun convertSpannableStringSizes(
        context: Context?,
        defaultStr: String?,
        resultCnt: String?,
        resultsTxt: String?,
        inputSearchTxt: String?,
        headerSize: Float,
        descSize: Float,
        font: Int,
    ): SpannableString? {
        val typeface = ResourcesCompat.getFont(context!!, font)
        val stringDefault = SpannableString(defaultStr)
        stringDefault.setSpan(RelativeSizeSpan(descSize),
            0,
            stringDefault.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val stringResultCnt = SpannableString(resultCnt)
        stringResultCnt.setSpan(RelativeSizeSpan(headerSize),
            0,
            stringResultCnt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        stringResultCnt.setSpan(StyleSpan(typeface!!.style),
            0,
            stringResultCnt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val stringResultsTxt = SpannableString(resultsTxt)
        stringResultsTxt.setSpan(RelativeSizeSpan(descSize),
            0,
            stringResultsTxt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val stringSearchTxt = SpannableString(inputSearchTxt)
        stringSearchTxt.setSpan(RelativeSizeSpan(headerSize),
            0,
            stringSearchTxt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        stringSearchTxt.setSpan(StyleSpan(typeface.style),
            0,
            stringSearchTxt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return SpannableString(TextUtils.concat(stringDefault,
            stringResultCnt,
            stringResultsTxt,
            stringSearchTxt))
    }

    fun checkPlayServices(context: Context?): Boolean {
        val result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context!!)
        if (result == ConnectionResult.SUCCESS) {
            return true
        } else if (ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED == result) {
            DialogManager.showToast(context, "Update google play services for better performance")
        } else if (ConnectionResult.SERVICE_MISSING == result) {
            DialogManager.showToast(context,
                "google play services missing install/update for better performance")
        } else if (ConnectionResult.SERVICE_DISABLED == result) {
            DialogManager.showToast(context,
                "google play services disabled enable for better performance")
        } else if (ConnectionResult.SERVICE_INVALID == result) {
            DialogManager.showToast(context,
                "google play services invalid install/update for better performance")
        }

        /*if (GooglePlayServicesUtil.isUserRecoverableError(result)) {

         * GooglePlayServicesUtil.getErrorDialog(resultCode, this,
         *
         * PLAY_SERVICES_RESOLUTION_REQUEST).show();

    } else {
        BuildLog.i("Tag", "This device is not supported.");
        Utility.showAlert(context, "", "This device is not supported better change device for better performance");

    }*/return false
    }


    fun getCurrent30Date(pattern: String?): String? {
        val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -30)
        val todate1 = cal.time
        return sdf.format(todate1)
    }

    fun formatdate(fdate: String?): String? {
        var datetime: String? = null
        val d: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
        try {
            val convertedDate: Date = inputFormat.parse(fdate)
            datetime = d.format(convertedDate)
        } catch (e: ParseException) {
        }
        return datetime
    }

    fun getCurrentTimeStamp(): String {
        val tsLong = System.currentTimeMillis() / 1000
        return tsLong.toString()
    }
}