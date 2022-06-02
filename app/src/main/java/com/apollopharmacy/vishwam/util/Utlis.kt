package com.apollopharmacy.vishwam.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*

object Utlis {
    var mProgressDialog: ProgressDialog? = null

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

    fun showLoading(context: Context) {
        hideLoading()
        mProgressDialog = Utils.showLoadingDialog(context)
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
            mProgressDialog!!.dismiss()
        }
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
        val sourceFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH)
        val destinationFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val convertedDate: Date = sourceFormat.parse(dateForFilter)
        return convertedDate
    }

    @SuppressLint("SimpleDateFormat")
    fun convertCmsDate(DatefromServer: String): String {
        val sourceFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val destFormat = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.ENGLISH)
        val convertedDate: Date = sourceFormat.parse(DatefromServer)
        return destFormat.format(convertedDate)
    }

    fun cmsComplaintDateFormat(dateToFormat: String): String {
        val sourceFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val destFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val convertedDate = sourceFormat.parse(dateToFormat)
        return destFormat.format(convertedDate)
    }
}