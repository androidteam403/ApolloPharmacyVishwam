package com.apollopharmacy.vishwam.ui.home.qcfail.filter

import com.apollopharmacy.vishwam.data.model.qc.QcFilterData

object QcFilterRepo {
    fun filterMenuData(): List<QcFilterData> {
        return listOf(
            QcFilterData("Date"),
            QcFilterData("Store"),
            QcFilterData("Region")
        )
    }
}