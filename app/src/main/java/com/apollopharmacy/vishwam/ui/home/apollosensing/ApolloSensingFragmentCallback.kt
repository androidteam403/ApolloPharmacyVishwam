package com.apollopharmacy.vishwam.ui.home.apollosensing

import java.io.File

interface ApolloSensingFragmentCallback {
    fun deleteImage(position: Int, file: File)
}