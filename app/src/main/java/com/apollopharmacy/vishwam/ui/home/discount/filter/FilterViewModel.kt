package com.apollopharmacy.vishwam.ui.home.discount.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.model.discount.FilterData

class FilterViewModel : ViewModel() {
    val listFilterData = MutableLiveData<List<FilterData>>()

    fun setFilterMenu() {
        listFilterData.value = FilterRepo.filterMenuData()
    }
}