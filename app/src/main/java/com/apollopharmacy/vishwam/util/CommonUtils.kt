package com.apollopharmacy.vishwam.util

import android.app.ProgressDialog
import android.content.Context
import com.apollopharmacy.vishwam.data.model.cms.NewTicketHistoryResponse

class CommonUtils {
    protected var mProgressDialog: ProgressDialog? = null

    protected   var historydatatranseferredarray=ArrayList<NewTicketHistoryResponse.Row>()

    fun showLoading(context: Context) {
        hideLoading()
        mProgressDialog = Utils.showLoadingDialog(context)
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
            mProgressDialog!!.cancel()
        }
    }
}