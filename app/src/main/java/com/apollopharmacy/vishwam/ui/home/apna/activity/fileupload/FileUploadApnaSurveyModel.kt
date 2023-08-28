package com.apollopharmacy.vishwam.ui.home.apna.activity.fileupload

import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import com.apollopharmacy.vishwam.util.fileupload.FileDownloadResponse
import java.io.File

class FileUploadApnaSurveyModel {
    var file: File? = null
    var id: Int? = null
    var sensingFileUploadResponse: SensingFileUploadResponse? = null
    var isFileUploaded: Boolean = false
    var isFileDownloaded: Boolean = false
    var fileDownloadResponse: FileDownloadResponse? = null
}