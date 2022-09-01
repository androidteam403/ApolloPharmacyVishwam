package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist

import android.view.View
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse

interface ApproveListcallback {

    fun onClickImage(
        position: Int, imagePath: String, view: View,
        category: String
    )
    fun onClickSubmit()
    fun onClickBack()
    fun onClickStartReiew()
    fun onePendingStatus()
}