package com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.fileupload

import android.app.Dialog

interface AttendenceFileUploadCallback {
    fun onFailureUpload(message: String)

    fun allFilesDownloaded(fileUploadModelList: ArrayList<AttendenceFileUploadModel>?,dialog: Dialog)

    fun allFilesUploaded(fileUploadModelList: java.util.ArrayList<AttendenceFileUploadModel>?,dialog: Dialog)
}