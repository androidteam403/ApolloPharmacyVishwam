package com.apollopharmacy.vishwam.ui.home.discount.bill

import com.apollopharmacy.vishwam.data.model.discount.ApprovalOrderRequest
import com.apollopharmacy.vishwam.data.model.discount.BILLDEATILSItem
import com.apollopharmacy.vishwam.data.model.discount.GetDiscountColorResponse

interface BilledFragmentCallback {
    fun onClick(orderdetails: java.util.ArrayList<BILLDEATILSItem>, position: Int)

}