package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist

import android.view.View

interface ApproveListcallback {

    fun onClickImage(position: Int, imagePath: String, view: View,category: String)
    fun onClickSubmit()
    fun onClickBack()
    fun onClickStartReiew()
    fun onePendingStatus()
}