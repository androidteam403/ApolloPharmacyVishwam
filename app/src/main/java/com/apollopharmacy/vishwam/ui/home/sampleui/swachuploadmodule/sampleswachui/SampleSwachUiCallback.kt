package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.sampleswachui

import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.sampleswachui.model.LastUploadedDateResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.CheckDayWiseAccessResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetStorePersonHistoryodelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.OnUploadSwachModelResponse

interface SampleSwachUiCallback {

    fun onClickApproved()

    fun onClickPending()

    fun onSuccessDayWiseAccesss(checkDayWiseAccessResponse: CheckDayWiseAccessResponse)

    fun onBackPressedUpload()

    fun onSuccessLastUploadedDate(value: LastUploadedDateResponse)

    fun onSuccessgetStorePersonHistory(value: GetStorePersonHistoryodelResponse)

    fun onSuccessOnUploadSwach(value: OnUploadSwachModelResponse)
}