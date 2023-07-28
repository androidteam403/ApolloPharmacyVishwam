package com.apollopharmacy.vishwam.util.fileuploadswach

import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import com.apollopharmacy.vishwam.util.fileupload.FileDownloadResponse
import java.io.File

class FileUploadSwachModel {
    var file: File? = null
    var id: Int? = null
    var categoryName:String?=null
    var sensingFileUploadResponse: SensingFileUploadResponse? = null
    var isFileUploaded: Boolean = false
    var isFileDownloaded: Boolean = false
    var fileDownloadResponse: FileDownloadResponse? = null
    var reshootCategoryPos:Int?=null
    var reshootUrlPos:Int?=null
    var imageUrlUsed:Boolean=false
}