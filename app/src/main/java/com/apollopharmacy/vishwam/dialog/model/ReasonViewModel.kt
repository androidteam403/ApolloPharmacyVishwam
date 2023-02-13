package com.apollopharmacy.vishwam.dialog.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.model.cms.ReasonmasterV2Response
import com.apollopharmacy.vishwam.util.Utils
import java.util.*
import kotlin.collections.ArrayList

class ReasonViewModel  : ViewModel()  {

    val TAG = "DepartmentViewModel"
    var fixedArrayList = MutableLiveData<ArrayList<ReasonmasterV2Response.Row>>()
    var orginalArrayList = ArrayList<ReasonmasterV2Response.Row>()

    fun siteArrayList(siteArrayList: ArrayList<ReasonmasterV2Response.Row>): ArrayList<ReasonmasterV2Response.Row> {
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
            } as ArrayList<ReasonmasterV2Response.Row>
        Utils.printMessage(TAG, "Filter Data :: " + data.toString())
        fixedArrayList.value = data
    }
}