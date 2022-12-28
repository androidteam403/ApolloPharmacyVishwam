package com.apollopharmacy.vishwam.dialog.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ItemViewModel : ViewModel() {
    var fixedArrayList = MutableLiveData<ArrayList<String>>()
    var orginalArrayList = ArrayList<String>()

    fun itemArrayList(itemArrayList: ArrayList<String>): ArrayList<String> {
        fixedArrayList.value = itemArrayList
        orginalArrayList = itemArrayList
        return itemArrayList
    }

    fun filterItem(item: String) {
        var data =
            orginalArrayList.filter { i ->
                i.contains(item) || i.contains(item.toUpperCase())
            } as ArrayList<String>
        fixedArrayList.value = data
    }
}