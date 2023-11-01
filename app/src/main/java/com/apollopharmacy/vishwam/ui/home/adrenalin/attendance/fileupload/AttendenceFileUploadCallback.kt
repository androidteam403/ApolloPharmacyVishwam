package com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.fileupload

import android.app.Dialog
import java.util.ArrayList

interface AttendenceFileUploadCallback {
    fun onFailureUpload(message: String)

    fun allFilesDownloaded(fileUploadModelList: ArrayList<AttendenceFileUploadModel>?,dialog: Dialog)

    fun allFilesUploaded(fileUploadModelList: List<AttendenceFileUploadModel>?)
}