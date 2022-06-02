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

class DiscountCalendarDialog : DialogFragment() {

    interface DateSelected {
        fun selectedDateTo(dateSelected: String, showingDate: String)
    }

    private lateinit var dateSelectedListner: DateSelected
    private var selectedDate: String = ""
    private var calFinalDate: String = ""

    companion object {
        const val KEY_DATA = "data"
        const val KEY_DIFF_DATA = "diff"
    }

    fun generateParsedData(data: String, diff: String): Bundle {
        return Bundle().apply {
            putString(KEY_DATA, data)
            putString(KEY_DIFF_DATA, diff)
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

        selectedDate = arguments?.getString(KEY_DATA).toString()
        calFinalDate = arguments?.getString(KEY_DIFF_DATA).toString()

        dataPickerBinding.cancel.setOnClickListener { dismiss() }
        val checkVal: NumberFormat = DecimalFormat("00")
        val c = Calendar.getInstance()
        val year: Int
        val month: Int
        val day: Int
        var fromYear: Int = 0
        var fromMonth: Int = 0
        var fromDay: Int = 0

        val strs = selectedDate.split("-").toTypedArray()
        year = strs[2].toInt()
        month = Utils.getMonth(selectedDate)
        day = strs[0].toInt()
        if (calFinalDate.isNotEmpty()) {
            val fromStr = calFinalDate.split("-").toTypedArray()
            fromYear = fromStr[2].toInt()
            fromMonth = Utils.getMonth(calFinalDate)
            fromDay = fromStr[0].toInt()
        }

        dataPickerBinding.datePicker.updateDate(year, (month - 1), day)
        if (calFinalDate.isEmpty()) {
            dataPickerBinding.datePicker.maxDate = (c.timeInMillis)
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val datePattern = android.icu.text.SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
                val dateDifference =
                    Utils.getDateDiff(datePattern,
                        calFinalDate, selectedDate)
                        .toInt()
                if (dateDifference <= 14) {
                    c.set(year, month - 1, day)
                    dataPickerBinding.datePicker.maxDate = (c.timeInMillis)
                    c.set(fromYear, fromMonth - 1, fromDay)
                    dataPickerBinding.datePicker.minDate = (c.timeInMillis)
                } else {
                    dataPickerBinding.datePicker.maxDate = (c.timeInMillis)
                    c.add(Calendar.DATE, -14)
                    dataPickerBinding.datePicker.minDate = (c.timeInMillis)
                }
            }
        }

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