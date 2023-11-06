package com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.fileupload

import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr.RetroQrFileDownloadResponse
import java.io.File

class AttendenceFileUploadModel {
    var file: File? = null
    var id: Int? = null
    var categoryId:String?=null
    var qrCode:String?=null
    var isFileAdded: Boolean = false

    var sensingFileUploadResponse: SensingFileUploadResponse? = null
    var isFileUploaded: Boolean = false
    var isFileDownloaded: Boolean = false
    var fileDownloadResponse: RetroQrFileDownloadResponse? = null
}