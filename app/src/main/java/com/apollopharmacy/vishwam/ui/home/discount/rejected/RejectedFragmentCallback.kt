package com.apollopharmacy.vishwam.ui.home.discount.rejected

import com.apollopharmacy.vishwam.data.model.discount.ApprovalOrderRequest
import com.apollopharmacy.vishwam.data.model.discount.GetDiscountColorResponse
import com.apollopharmacy.vishwam.data.model.discount.RejectedOrderResponse

interface RejectedFragmentCallback {
    fun onSuccessgetColorList(value: GetDiscountColorResponse)

    fun onClick(orderdetails: java.util.ArrayList<RejectedOrderResponse.REJECTEDLISTItem>, position: Int, isMarginRequired: Boolean)

}