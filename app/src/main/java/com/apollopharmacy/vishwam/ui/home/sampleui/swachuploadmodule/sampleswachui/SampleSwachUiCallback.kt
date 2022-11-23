package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.sampleswachui

import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.sampleswachui.model.LastUploadedDateResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.CheckDayWiseAccessResponse

interface SampleSwachUiCallback {

    fun onClickApproved()

    fun onClickPending()

    fun onSuccessDayWiseAccesss(checkDayWiseAccessResponse: CheckDayWiseAccessResponse)

    fun onBackPressedUpload()


}