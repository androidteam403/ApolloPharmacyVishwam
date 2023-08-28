package com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr

import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import java.io.File

class RetroQrFileUploadModel {
    var file: File? = null
    var id: Int? = null
    var rackNo:String?=null
    var qrCode:String?=null

    var sensingFileUploadResponse: SensingFileUploadResponse? = null
    var isFileUploaded: Boolean = false
    var isFileDownloaded: Boolean = false
    var fileDownloadResponse: RetroQrFileDownloadResponse? = null
}