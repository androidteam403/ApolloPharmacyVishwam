package com.apollopharmacy.vishwam.ui.home.adrenalin.history

import com.apollopharmacy.vishwam.data.model.attendance.AttendanceHistoryRes

interface HistorryImageClickListener {
    fun onItemClick(position: Int, orderData: ArrayList<AttendanceHistoryRes>)

}