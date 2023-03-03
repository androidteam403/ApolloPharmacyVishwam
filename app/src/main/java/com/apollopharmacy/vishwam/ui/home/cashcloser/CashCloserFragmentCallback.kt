package com.apollopharmacy.vishwam.ui.home.cashcloser

import java.io.File

interface CashCloserFragmentCallback {
    fun addImage(imagePosition: Int)

    fun deleteImage(imagePosition: Int)

    fun previewImage(file: File, position: Int)

    fun headrItemClickListener(storeId: String, pos:Int)
}