package com.apollopharmacy.vishwam.ui.home.qcfail.pending

import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcAcceptRejectResponse

interface PendingActivityCallback {

    fun onClickBack()

    fun accept()
    fun reject()

    fun onSuccessSaveAccept(acceptRejectList:QcAcceptRejectResponse)

}