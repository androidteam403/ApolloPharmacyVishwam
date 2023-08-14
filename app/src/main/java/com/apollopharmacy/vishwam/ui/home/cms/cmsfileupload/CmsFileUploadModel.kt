package com.apollopharmacy.vishwam.ui.home.cms.cmsfileupload

import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import com.apollopharmacy.vishwam.util.fileupload.FileDownloadResponse
import java.io.File

class CmsFileUploadModel {
    var file: File? = null
    var id: Int? = null
    var sensingFileUploadResponse: SensingFileUploadResponse? = null
    var isFileUploaded: Boolean = false
    var isFileDownloaded: Boolean = false
    var fileDownloadResponse: FileDownloadResponse? = null
}