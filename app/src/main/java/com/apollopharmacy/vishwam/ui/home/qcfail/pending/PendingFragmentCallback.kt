package com.apollopharmacy.vishwam.ui.home.qcfail.pending

interface PendingFragmentCallback {

    fun onClickReason(headerPos: Int, itemPos: Int, orderId: String?)



    fun onNotify()


    fun onFailureGetPendingAndAcceptAndRejectList(message: String)
}