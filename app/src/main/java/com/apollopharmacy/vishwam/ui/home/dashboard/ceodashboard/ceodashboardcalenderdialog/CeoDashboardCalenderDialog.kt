package com.apollopharmacy.vishwam.ui.home.dashboard.ceodashboard.ceodashboardcalenderdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.DialogDatePickerBinding
import com.apollopharmacy.vishwam.util.Utils
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CeoDashboardCalenderDialog : DialogFragment() {

    interface DateSelected {
        fun selectedDateTo(dateSelected: String, showingDate: String, toDateFormatted: String)
        fun selectedDatefrom(dateSelected: String, showingDate: String)

    }

    private var dateSelectedListner: DateSelected? = null
    private var selectedDate: String = ""

    companion object {
        const val KEY_DATA = "data"
        const val KEY_FROM_DATE = "from_date"
        const val KEY_IS_TO = "is_to_date"
        const val KEY_TO_DATE = "to_date"
    }

    fun generateParsedData(
        data: String,
        isToDate: Boolean,
        fromDate: String,
        toDate: String,
    ): Bundle {
        return Bundle().apply {
            putString(KEY_DATA, data)
            putString(KEY_FROM_DATE, fromDate)
            putBoolean(KEY_IS_TO, isToDate)
            putString(KEY_TO_DATE, toDate)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val dataPickerBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_date_picker,
            container,
            false
        ) as DialogDatePickerBinding
        if (parentFragment != null) {
            dateSelectedListner = parentFragment as DateSelected
        } else {
            dateSelectedListner = activity as DateSelected
        }


        val calendar = Calendar.getInstance()
//        calendar.add(Calendar.DATE, -PROBLEM_SINCE_DAYS);

//        dataPickerBinding.datePicker.maxDate = System.currentTimeMillis()
//        dataPickerBinding.datePicker.minDate = calendar.getTimeInMillis()

        selectedDate = arguments?.getString(KEY_DATA).toString()
        dataPickerBinding.cancel.setOnClickListener { dismiss() }
        val checkVal: NumberFormat = DecimalFormat("00")
        val c = Calendar.getInstance()
        val year: Int
        var month: Int
        var day: Int
        var monthNum: Int = 0
        var mnthFormat: String = ""
        if (selectedDate.isEmpty()) {
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH) + 1
            day = c.get(Calendar.DAY_OF_MONTH)
        } else {
            val strs = selectedDate.split("-").toTypedArray()
            year = strs[2].toInt()
//            val _month = if (_month + 1 < 10) 0 + (_month + 1) else (_month + 1)
            month = Utils.getMonth(selectedDate)
            day = strs[0].toInt()
        }
        Utils.printMessage(
            "TAG",
            "Date is :: " + day + "/" + month + "/" + year + ",  DT : " + selectedDate + ", : " + month
        )


        dataPickerBinding.datePicker.updateDate(year, (month - 1), day)
        if (arguments?.getBoolean(KEY_IS_TO) == true) {

            var fromDate = arguments?.getString(KEY_FROM_DATE).toString()
            val fromCal = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            fromCal.time = sdf.parse(fromDate)
            dataPickerBinding.datePicker.minDate = (fromCal.timeInMillis)

            dataPickerBinding.datePicker.maxDate = (c.timeInMillis)

            /* val date = SimpleDateFormat("dd-MMM-yyyy").parse(arguments?.getString(KEY_FROM_DATE))
             dataPickerBinding.datePicker.minDate = date.time*/
        } else {

            var toDate = arguments?.getString(KEY_TO_DATE).toString()
            val toCal = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            toCal.time = sdf.parse(toDate)
            dataPickerBinding.datePicker.maxDate = (toCal.timeInMillis)//- 1000*60*60*24*30


//            dataPickerBinding.datePicker.minDate = (toCal.timeInMillis - 2592000000)

//            dataPickerBinding.datePicker.maxDate = (c.timeInMillis)

            /*val date = SimpleDateFormat("dd-MMM-yyyy").parse(arguments?.getString(KEY_FROM_DATE))
            dataPickerBinding.datePicker.minDate = date.time*/
        }


        dataPickerBinding.ok.setOnClickListener {
            val _year = dataPickerBinding.datePicker.year
            val _date =
                if (dataPickerBinding.datePicker.dayOfMonth < 10) dataPickerBinding.datePicker.dayOfMonth else dataPickerBinding.datePicker.dayOfMonth
            c[Calendar.MONTH] = dataPickerBinding.datePicker.month
            val monthFormat = SimpleDateFormat("MMM", Locale.ENGLISH)
            val getDate =
                "${checkVal.format(_date)}-${monthFormat.format(c.time)}-${_year}"


            val toDateTemp = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            toDateTemp.time = sdf.parse(getDate)
            var toDateTimeMills = toDateTemp.timeInMillis + 7776000000//2592000000

            var toDateFormatted = getToDateFormatted(toDateTimeMills, "dd-MMM-yyyy")
            dateSelectedListner!!.selectedDateTo(getDate, getDate, toDateFormatted!!)
            dismiss()
        }
        return dataPickerBinding.root
    }

    fun getToDateFormatted(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }
}