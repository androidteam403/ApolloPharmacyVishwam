package com.apollopharmacy.vishwam.ui.home.apnarectro.comparisonscreenscreation

interface ComparisonScreenCreationCallBack {

    fun onClickCamera(imageId:String,position:Int,subPos: Int)

    fun onClickBack()

    fun onClickUpload(pos:Int,subPos:Int)

    fun onClickDelete(position: Int)

    fun onClickDeleteforPreRetro(position: Int)
}