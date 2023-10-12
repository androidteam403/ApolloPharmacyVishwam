package com.apollopharmacy.vishwam.ui.home.discount.approved

import com.apollopharmacy.vishwam.data.model.discount.ApprovalOrderRequest
import com.apollopharmacy.vishwam.data.model.discount.GetDiscountColorResponse

interface ApprovedFragmentCallback {
    fun onSuccessgetColorList(value: GetDiscountColorResponse)
    fun onClick(orderdetails: java.util.ArrayList<ApprovalOrderRequest.APPROVEDLISTItem>, position: Int,isMarginRequired: Boolean)

}