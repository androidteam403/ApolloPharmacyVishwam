package com.apollopharmacy.vishwam.ui.home.drugmodule.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.model.cms.ReasonmasterV2Response
import com.apollopharmacy.vishwam.util.Utils
import java.util.*
import kotlin.collections.ArrayList

class ItemTypeViewModel : ViewModel()  {

    val TAG = "DepartmentViewModel"
    var fixedArrayList = MutableLiveData<ArrayList<ItemTypeDropDownResponse.Rows>>()
    var orginalArrayList = ArrayList<ItemTypeDropDownResponse.Rows>()

    fun siteArrayList(siteArrayList: ArrayList<ItemTypeDropDownResponse.Rows>): ArrayList<ItemTypeDropDownResponse.Rows> {
        fixedArrayList.value = siteArrayList
        orginalArrayList = siteArrayList
        return siteArrayList
    }

    fun filterDataBySiteId(siteId: String) {
        Utils.printMessage(TAG, "Orginal Data :: " + orginalArrayList.toString())
        var data =
            orginalArrayList.filter { m ->
                m.name!!.toUpperCase(Locale.getDefault()).contains(siteId.toUpperCase(
                    Locale.getDefault()))
            } as ArrayList<ItemTypeDropDownResponse.Rows>
        Utils.printMessage(TAG, "Filter Data :: " + data.toString())
        fixedArrayList.value = data
    }
}