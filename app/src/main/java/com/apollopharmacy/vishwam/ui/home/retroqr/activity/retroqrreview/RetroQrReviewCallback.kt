package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrreview

import java.io.File

interface RetroQrReviewCallback {
    fun onClickCamera(position: Int)
    fun onClickDelete(position: Int)
    fun onClickCompare(position: Int, file: File?, url: String?)
    fun onClickSubmit()
}