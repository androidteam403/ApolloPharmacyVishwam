package com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist

import android.view.View

interface ApproveListcallback {

    fun onClickImage(
        position: Int, imagePath: String, view: View,
        category: String,
        configPositionRes: Int
    )
    fun onClickSubmit()
    fun onClickBack()
    fun onClickStartReiew()
    fun onePendingStatus()
    fun onClickSubmitRatingReview()
    fun onClickSubmitRatingButton()
//    fun ratingComments()
}