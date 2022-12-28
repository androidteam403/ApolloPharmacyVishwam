package com.apollopharmacy.vishwam.ui.home.champs.reports

import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentChampsReportBinding
import com.apollopharmacy.vishwam.dialog.CalenderNew
import com.apollopharmacy.vishwam.dialog.ComplaintListCalendarDialog

class ChampsReportFragment : BaseFragment<ChampsReportViewModel, FragmentChampsReportBinding>(),
    CalenderNew.DateSelected, ComplaintListCalendarDialog.DateSelected {
    override val layoutRes: Int
        get() = R.layout.fragment_champs_report

    override fun retrieveViewModel(): ChampsReportViewModel {
        return ChampsReportViewModel()
    }

    override fun setup() {
        viewBinding.startDateSelect.setOnClickListener {
            CalenderNew().apply {
                arguments = generateParsedData(
                    viewBinding.startDateSelect.text.toString(),
                    false,
                    viewBinding.startDateSelect.text.toString()
                )
            }.show(childFragmentManager, "")
        }

        viewBinding.endDateSelect.setOnClickListener {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(
                    viewBinding.endDateSelect.text.toString(),
                    false,
                    viewBinding.endDateSelect.text.toString()
                )
            }.show(childFragmentManager, "")
        }
    }

    override fun selectedDate(dateSelected: String, showingDate: String) {
        viewBinding.startDateSelect.setText(showingDate)
    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        viewBinding.endDateSelect.setText(showingDate)
    }

    override fun selectedDatefrom(dateSelected: String, showingDate: String) {
    }
}