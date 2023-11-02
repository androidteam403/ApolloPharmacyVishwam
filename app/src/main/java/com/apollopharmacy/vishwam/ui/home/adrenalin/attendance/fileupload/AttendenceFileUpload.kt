package com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.fileupload

import android.content.Context
import android.widget.Toast
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.Api
import android.app.Dialog
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadRequest
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr.RetroQrFileDownloadRequest
import com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr.RetroQrFileDownloadResponse
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils
import com.apollopharmacy.vishwam.util.signaturepad.NetworkUtils
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class AttendenceFileUpload {

    var context: Context? = null
    var fileUploadCallback: AttendenceFileUploadCallback? = null
    var fileUploadModelList: ArrayList<AttendenceFileUploadModel>? = null
    lateinit var dialog:Dialog
    fun uploadFiles(
        context: Context,
        fileUploadCallback: AttendenceFileUploadCallback,
        fileUploadModelList: ArrayList<AttendenceFileUploadModel>?,
        dialog: Dialog
    ) {
        this.context = context
        this.fileUploadCallback = fileUploadCallback
        this.fileUploadModelList = fileUploadModelList
        this.dialog=dialog

        if (NetworkUtils.isNetworkConnected(context)) {
            showLoading(context!!)
            uploadFile(fileUploadModelList!!.get(0))
        } else {
            fileUploadCallback.onFailureUpload("Something went wrong.")
        }
    }

    fun uploadFile(fileUploadModel: AttendenceFileUploadModel) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val apiInterface = Api.getClient()

            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var sensingFileUploadRequest = SensingFileUploadRequest()
            sensingFileUploadRequest.Filename = fileUploadModel.file

            var baseUrl =""
            var token=""
//            var baseUrl ="https://blbext.apollopharmacy.org:3443/SENSING/Apollo/SensingFileUpload"
            //"https://blbext.apollopharmacy.org:3443/SENSING/Apollo/SensingFileUpload"
//            var token = "cTfznn4yhybBR7WSrNJn1gAAI"
            //"9f15bdd0fcd5423190cHNK"
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("RT BLOBUPLOAD")) {
                    baseUrl = data.APIS[i].URL
                    token = "ZQxnRCV19T3SY+UtGdjine"
                    break
                }
            }

            val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), fileUploadModel.file!!)
            val fileToUpload =
                MultipartBody.Part.createFormData("file", fileUploadModel.file!!.name, requestBody)

            val call = apiInterface.SENSING_FILE_UPLOAD_API_CALL(
                baseUrl, "multipart/form-data", token, fileToUpload
            )
            call.enqueue(object : Callback<SensingFileUploadResponse?> {


                override fun onResponse(
                    call: Call<SensingFileUploadResponse?>,
                    response: Response<SensingFileUploadResponse?>,
                ) {
                    if (response.body() != null && response.body()!!.status == true) {
                        onSuccessFileUpload(fileUploadModel, response.body()!!)
                    } else {
                        onFailureFileUpload(fileUploadModel, response.body()!!)
                    }
                }

                override fun onFailure(call: Call<SensingFileUploadResponse?>, t: Throwable) {
                    ActivityUtils.hideDialog()
                    fileUploadCallback!!.onFailureUpload(t.message!!)
                }
            })
        } else {
            fileUploadCallback!!.onFailureUpload("Something went wrong.")
        }
    }

    fun onSuccessFileUpload(
        fileUploadModel: AttendenceFileUploadModel,
        sensingFileUploadResponse: SensingFileUploadResponse,
    ) {
        fileUploadModel.isFileUploaded = true
        fileUploadModel.sensingFileUploadResponse = sensingFileUploadResponse
        var fileUploadModelTemp: AttendenceFileUploadModel? = null
        for (i in fileUploadModelList!!) {
            if (!i.isFileUploaded) {
                fileUploadModelTemp = i

                break
            }
        }
        if (fileUploadModelTemp != null) {
            uploadFile(fileUploadModelTemp)
        } else {
            hideLoading()
            fileUploadCallback!!.allFilesUploaded(fileUploadModelList,dialog)
//            downloadFiles(context!!, fileUploadCallback!!, fileUploadModelList)
        }
    }

    fun onFailureFileUpload(
        fileUploadModel: AttendenceFileUploadModel,
        sensingFileUploadResponse: SensingFileUploadResponse,
    ) {
        ActivityUtils.hideDialog()
        Toast.makeText(context, "File upload failed", Toast.LENGTH_SHORT).show()
    }


    fun downloadFiles(
        context: Context,
        fileUploadCallback: AttendenceFileUploadCallback,
        fileUploadModelList: ArrayList<AttendenceFileUploadModel>?,
    ) {
        this.context = context
        this.fileUploadCallback = fileUploadCallback
        this.fileUploadModelList = fileUploadModelList;

        if (NetworkUtils.isNetworkConnected(context)) {
            showLoading(context!!)
            downloadFile(fileUploadModelList!!.get(0))
        } else {
            fileUploadCallback.onFailureUpload("Something went wrong.")
        }
    }

    fun downloadFile(fileUploadModel: AttendenceFileUploadModel) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val apiInterface = Api.getClient()

            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var fileDownloadRequest = RetroQrFileDownloadRequest()
            fileDownloadRequest.RefURL = fileUploadModel.sensingFileUploadResponse!!.referenceurl
            var baseUrl=""
            var token =""
//            var baseUrl="https://blbext.apollopharmacy.org:3443/SENSING/Apollo/SensingSingleFileDownload"
//            var token = "cTfznn4yhybBR7WSrNJn1gAAI"
           for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("RT BLOBDOWNLOAD")) {
                    baseUrl = data.APIS[i].URL
                    token = "ZQxnRCV19T3SY+UtGdjine"
                    break
                }
            }


            val call = apiInterface.FILE_DOWNLOAD_API_CALL_QR(
                baseUrl, token, fileDownloadRequest
            )
            call.enqueue(object : Callback<RetroQrFileDownloadResponse?> {


                override fun onResponse(
                    call: Call<RetroQrFileDownloadResponse?>,
                    response: Response<RetroQrFileDownloadResponse?>,
                ) {
                    if (response.body() != null && response.body()!!.status == true) {
                        onSuccessFileDownload(fileUploadModel, response.body()!!)
                    } else {
                        onFailureFileDownload(fileUploadModel, response.body()!!)
                    }
                }

                override fun onFailure(call: Call<RetroQrFileDownloadResponse?>, t: Throwable) {
                    ActivityUtils.hideDialog()
                    fileUploadCallback!!.onFailureUpload(t.message!!)
                }
            })
        } else {
            fileUploadCallback!!.onFailureUpload("Something went wrong.")
        }
    }

    fun onSuccessFileDownload(
        fileUploadModel: AttendenceFileUploadModel,
        fileDownloadResponse: RetroQrFileDownloadResponse,
    ) {
        fileUploadModel.isFileDownloaded = true
        fileUploadModel.fileDownloadResponse = fileDownloadResponse
        var fileUploadModelTemp: AttendenceFileUploadModel? = null
        for (i in fileUploadModelList!!) {
            if (!i.isFileDownloaded) {
                fileUploadModelTemp = i
                break
            }
        }
        if (fileUploadModelTemp != null) {
            downloadFile(fileUploadModelTemp)
        } else {
            hideLoading()
            fileUploadCallback!!.allFilesDownloaded(fileUploadModelList,dialog)
        }
    }

    fun onFailureFileDownload(
        fileUploadModel: AttendenceFileUploadModel,
        fileDownloadResponse: RetroQrFileDownloadResponse,
    ) {
        ActivityUtils.hideDialog()
        Toast.makeText(context, "File download failed", Toast.LENGTH_SHORT).show()
    }
}
