package com.apollopharmacy.vishwam.data.model

import java.io.File

data class ImageDataDto(val file: File, val base64Images: String)
data class Image(val file: File, val base64Images: String, var imageType:String)