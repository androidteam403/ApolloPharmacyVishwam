package com.apollopharmacy.vishwam.ui.home.drugmodule

import com.apollopharmacy.vishwam.ui.home.drugmodule.model.ItemTypeDropDownResponse

interface DrugFragmentCallback {
    fun onSuccessItemTypeApi(itemTypeDropDownResponse: ItemTypeDropDownResponse)
    fun onSuccessDoctorSpecialityApi(doctorSpecialityDropDownResponse: ItemTypeDropDownResponse)
    fun onFailureMessage(ticketId: String, userName: String, message: String)
    fun onFailureUat()

}