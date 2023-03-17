package com.apollopharmacy.vishwam.dialog.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.model.cms.ReasonmasterV2Response
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.util.Utils
import java.util.*
import kotlin.collections.ArrayList

class DepartmentViewModel : ViewModel()  {

    val TAG = "DepartmentViewModel"
    var fixedArrayList = MutableLiveData<ArrayList<ReasonmasterV2Response.Department>>()
    var orginalArrayList = ArrayList<ReasonmasterV2Response.Department>()

    fun siteArrayList(siteArrayList: ArrayList<ReasonmasterV2Response.Department>): ArrayList<ReasonmasterV2Response.Department> {
        fixedArrayList.value = siteArrayList
        orginalArrayList = siteArrayList
        return siteArrayList
    }

    fun filterDataBySiteId(siteId: String) {
        Utils.printMessage(TAG, "Orginal Data :: " + orginalArrayList.toString())
        var data =
            orginalArrayList.filter { m ->
                m.code!!.contains(siteId) || m.name!!.toUpperCase(Locale.getDefault()).contains(siteId.toUpperCase(
                    Locale.getDefault()))
            } as ArrayList<ReasonmasterV2Response.Department>
        Utils.printMessage(TAG, "Filter Data :: " + data.toString())
        fixedArrayList.value = data
    }
}