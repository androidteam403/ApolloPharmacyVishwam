package com.apollopharmacy.vishwam.ui.home.discount.filter

import com.apollopharmacy.vishwam.data.model.discount.FilterData

object FilterRepo {
    fun filterMenuData(): List<FilterData> {
        return listOf(
            FilterData("Date"),
            FilterData("Store"),
            FilterData("Region")
        )
    }
}