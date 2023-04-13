package com.apollopharmacy.vishwam.ui.home.qcfail.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.model.discount.FilterData
import com.apollopharmacy.vishwam.data.model.qc.QcFilterData

class QcFilterViewModel : ViewModel() {
    val listFilterData = MutableLiveData<List<QcFilterData>>()

    fun setFilterMenu() {
        listFilterData.value = QcFilterRepo.filterMenuData()
    }
}