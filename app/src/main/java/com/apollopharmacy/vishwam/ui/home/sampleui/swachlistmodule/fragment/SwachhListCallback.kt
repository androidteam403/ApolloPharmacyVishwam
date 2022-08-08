package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment

import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved

interface SwachhListCallback {

    fun onClickUpdate(pendingAndApproved: PendingAndApproved)
}