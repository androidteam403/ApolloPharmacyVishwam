package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model

import java.io.Serializable

class PendingAndApproved : Serializable {
    var swachhid: String? = null
    var storeId: String? = null
    var approvedBy: String? = null
    var reshootBy: String? = null
    var partiallyApprovedBy: String? = null
    var approvedDate: String? = null
    var reshootDate: String? = null
    var partiallyApprovedDate: String? = null
    var isApproved: Boolean? = false
    var uploadedBy: String? = null
    var uploadedDate: String? = null
    var status: String? = null
    var empName:String?=null


}