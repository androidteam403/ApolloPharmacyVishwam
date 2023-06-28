package com.apollopharmacy.vishwam.ui.home.discount.rejected

import com.apollopharmacy.vishwam.data.model.discount.GetDiscountColorResponse

interface RejectedFragmentCallback {
    fun onSuccessgetColorList(value: GetDiscountColorResponse)
}