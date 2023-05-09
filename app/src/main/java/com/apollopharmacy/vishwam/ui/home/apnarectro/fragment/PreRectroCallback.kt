package com.apollopharmacy.vishwam.ui.home.apnarectro.fragment


interface PreRectroCallback {

    fun onClickContinue()
    fun onClickCardView()
    fun onClickPreRetroPending(s: String)
    fun onClickPostRetroPending(s: String, postRetostatus: String)

}