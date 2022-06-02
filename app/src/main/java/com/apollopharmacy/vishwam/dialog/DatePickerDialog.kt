package com.apollopharmacy.vishwam.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.DatepickerLayoutBinding
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis.formatTheDate

class DatePickerDialog : DialogFragment() {
    val TAG = "DatePickerDialog"

    interface DateSelected {
        fun selectDateFrom(dateSelected: String, showingDate: String)
        fun selectedDateTo(dateSelected: String, showingDate: String)
    }

    private lateinit var dateSelectedListner: DateSelected
    private var tagSelected: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val dataPickerBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.datepicker_layout,
            container,
            false
        ) as DatepickerLayoutBinding
        dateSelectedListner = targetFragment as DateSelected
        tagSelected = requireArguments().getString("tag")!!
        Utils.printMessage(TAG, "Bundle :: " + tagSelected.toString())
        dataPickerBinding.cancel.setOnClickListener { }
        dataPickerBinding.ok.setOnClickListener {
            var monthToString = (dataPickerBinding.datePicker.month + 1).toString()
            if (monthToString.length == 1) {
                monthToString = "0$monthToString"
            }
            val getDate =
                "${dataPickerBinding.datePicker.dayOfMonth}/${monthToString}/${dataPickerBinding.datePicker.year}"
            val datePicker = formatTheDate(getDate)
            if (tagSelected.equals("from"))
                dateSelectedListner.selectDateFrom(
                    datePicker,
                    getDate
                ) else dateSelectedListner.selectedDateTo(datePicker, getDate)
            dismiss()
        }
        return dataPickerBinding.root
    }
}