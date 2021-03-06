package com.apollopharmacy.vishwam.dialog

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
import java.util.*

class ComplaintListCalendarDialog : DialogFragment() {

    interface DateSelected {
        fun selectedDateTo(dateSelected: String, showingDate: String)
    }

    private lateinit var dateSelectedListner: DateSelected
    private var selectedDate: String = ""

    companion object {
        const val KEY_DATA = "data"
    }

    fun generateParsedData(data: String): Bundle {
        return Bundle().apply {
            putString(KEY_DATA, data)
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

        dateSelectedListner = parentFragment as DateSelected

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
        Utils.printMessage("TAG",
            "Date is :: " + day + "/" + month + "/" + year + ",  DT : " + selectedDate + ", : " + month)

        dataPickerBinding.datePicker.updateDate(year, (month - 1), day)
        dataPickerBinding.datePicker.maxDate = (c.timeInMillis)

        dataPickerBinding.ok.setOnClickListener {
            val _year = dataPickerBinding.datePicker.year
            val _date =
                if (dataPickerBinding.datePicker.dayOfMonth < 10) dataPickerBinding.datePicker.dayOfMonth else dataPickerBinding.datePicker.dayOfMonth
            c[Calendar.MONTH] = dataPickerBinding.datePicker.month
            val monthFormat = SimpleDateFormat("MMM", Locale.ENGLISH)
            val getDate =
                "${checkVal.format(_date)}-${monthFormat.format(c.time)}-${_year}"
            dateSelectedListner.selectedDateTo(getDate, getDate)
            dismiss()
        }
        return dataPickerBinding.root
    }
}