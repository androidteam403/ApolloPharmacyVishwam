package com.apollopharmacy.vishwam.ui.home.discount.approved

import com.apollopharmacy.vishwam.data.model.discount.GetDiscountColorResponse

interface ApprovedFragmentCallback {
    fun onSuccessgetColorList(value: GetDiscountColorResponse)

}