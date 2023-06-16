package com.apollopharmacy.vishwam.ui.home.discount.pending

import com.apollopharmacy.vishwam.data.model.discount.GetDiscountColorResponse

interface PendingFragmentCallback {
    fun onSuccessgetColorList(value: GetDiscountColorResponse)

}