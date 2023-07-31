package com.apollopharmacy.vishwam.util.fileupload

import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import java.io.File

class FileUploadModel {
    var file: File? = null
    var id: Int? = null
    var sensingFileUploadResponse: SensingFileUploadResponse? = null
    var isFileUploaded: Boolean = false
    var isFileDownloaded: Boolean = false
    var fileDownloadResponse: FileDownloadResponse? = null
}