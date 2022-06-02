package com.apollopharmacy.vishwam.ui.home.adrenalin.history

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.attendance.AttendanceHistoryRes
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.FragmentHistoryBinding
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils

class HistoryFragment : BaseFragment<HistoryViewModel, FragmentHistoryBinding>() {

    val TAG = "HistoryFragment"
    lateinit var userData: LoginDetails
    var employeeID: String = ""

    override val layoutRes: Int
        get() = R.layout.fragment_history


    override fun retrieveViewModel(): HistoryViewModel {
        return HistoryViewModel()
    }

    override fun setup() {
        viewBinding.historyModel = viewModel

        userData = LoginRepo.getProfile()!!
        employeeID = userData.EMPID

        if (NetworkUtil.isNetworkConnected(requireContext())) {
            showLoading()
            viewModel.getAttendanceHistory(employeeID)
        } else {
            Toast.makeText(
                requireContext(),
                context?.resources?.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }

        viewModel.historyData.observe(viewLifecycleOwner, Observer {
            hideLoading()
            if (it.size == 0) {
                viewBinding.emptyList.visibility = View.VISIBLE
            } else {
                viewBinding.emptyList.visibility = View.GONE
                handleHistoryList(it)
            }
        })
    }

    private fun handleHistoryList(historyList: ArrayList<AttendanceHistoryRes>) {
        var index = 1
        for (item in historyList) {
            val view: View = layoutInflater.inflate(R.layout.view_history_table, null, false)
            val rowSerialNumber: TextView = view.findViewById(R.id.rowSerialNumber)
            val rowSignInTime: TextView = view.findViewById(R.id.rowSignInTime)
            val rowSignOutTime: TextView = view.findViewById(R.id.rowSignOutTime)
            val rowDuration: TextView = view.findViewById(R.id.rowDuration)
            rowSerialNumber.text = "" + index++
            rowSignInTime.text = Utils.getHistoryCustomDate(item.signInDate)
            var signOutVal = ""
            if (item.signOutDate.isNullOrEmpty()) {
                signOutVal = "-"
            } else {
                signOutVal = Utils.getHistoryCustomDate(item.signOutDate)
            }
            rowSignOutTime.text = signOutVal
            var durationVal = ""
            if (item.duration.isNullOrEmpty()) {
                durationVal = "-"
            } else {
                durationVal = Utils.getHistoryDurationTimeFormat(item.duration)
            }
            rowDuration.text = durationVal
            viewBinding.tableLayout.addView(view)
        }
    }
}