package com.apollopharmacy.vishwam.util.fileuploadchamps

import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse
import com.apollopharmacy.vishwam.util.fileupload.FileDownloadResponse
import java.io.File

class FileUploadChampsModel {
    var file: File? = null
    var id: Int? = null
    var categoryName:String?=null
    var sensingFileUploadResponse: SensingFileUploadResponse? = null
    var isFileUploaded: Boolean = false
    var isFileDownloaded: Boolean = false
    var fileDownloadResponse: FileDownloadResponse? = null
}