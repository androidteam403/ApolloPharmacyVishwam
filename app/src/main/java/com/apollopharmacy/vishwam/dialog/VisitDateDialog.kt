package com.apollopharmacy.vishwam.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.databinding.DialogDatePickerBinding
import com.apollopharmacy.vishwam.util.Utils
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class VisitDateDialog : DialogFragment() {

    interface DateSelected {
        fun selectedDate(dateSelected: String, showingDate: String)
    }

    private lateinit var dateSelectedListner: DateSelected

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
        calendar.add(Calendar.DATE, -Config.PROBLEM_SINCE_DAYS);

        dataPickerBinding.datePicker.maxDate = System.currentTimeMillis()
        dataPickerBinding.datePicker.minDate = calendar.getTimeInMillis()

        dataPickerBinding.cancel.setOnClickListener { dismiss() }
        val checkVal: NumberFormat = DecimalFormat("00")

        dataPickerBinding.ok.setOnClickListener {
            val c = Calendar.getInstance()
            c[Calendar.MONTH] = dataPickerBinding.datePicker.month
            val monthFormat = SimpleDateFormat("MMM", Locale.ENGLISH)
            val getDate =
                "${checkVal.format(dataPickerBinding.datePicker.dayOfMonth)} ${monthFormat.format(c.time)} ${dataPickerBinding.datePicker.year}"
            dateSelectedListner.selectedDate(getDate, getDate)
            dismiss()
        }
        return dataPickerBinding.root
    }
}