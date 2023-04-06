package com.apollopharmacy.vishwam.dialog.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Row
import com.apollopharmacy.vishwam.util.Utils

class SearchManagerDialogViewModel : ViewModel() {
    val TAG = "SiteViewModel"
    var fixedArrayList = MutableLiveData<ArrayList<Row>>()
    var orginalArrayList = ArrayList<Row>()

    fun managerArrayList(siteArrayList: ArrayList<Row>): ArrayList<Row> {
        fixedArrayList.value = siteArrayList
        orginalArrayList = siteArrayList
        return siteArrayList
    }

    fun filterDataByManager(name: String) {
        Utils.printMessage(TAG, "Orginal Data :: " + orginalArrayList.toString())
        var data =
            orginalArrayList.filter { m ->
                m.first_name!!.toUpperCase().contains(name.toUpperCase())
            } as ArrayList<Row>
        Utils.printMessage(TAG, "Filter Data :: " + data.toString())
        fixedArrayList.value = data
    }
}