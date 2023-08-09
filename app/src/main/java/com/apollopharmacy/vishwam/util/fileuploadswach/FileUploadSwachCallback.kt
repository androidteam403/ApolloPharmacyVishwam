package com.apollopharmacy.vishwam.util.fileuploadswach

interface FileUploadSwachCallback {
     fun onFailureUpload(s: String)
     fun allFilesDownloaded(fileUploadModelList: List<FileUploadSwachModel>)
}