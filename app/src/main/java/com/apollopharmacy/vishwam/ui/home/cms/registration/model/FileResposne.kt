package com.apollopharmacy.vishwam.ui.home.cms.registration.model


data class FileResposne(
    val `data`: List<Data>,
    val success: Boolean,
    val zcServerDateTime: String,
    val zcServerHost: String,
    val zcServerIp: String
)

data class Data(
    val contentType: String,
    val extension: String,
    val fullPath: String,
    val name: String,
    val size: Int,
    val uuid: String
)