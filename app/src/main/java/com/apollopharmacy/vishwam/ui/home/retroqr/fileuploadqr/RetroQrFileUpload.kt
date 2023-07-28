package com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr

import android.content.Context
import android.widget.Toast
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.network.ApiClient
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadRequest
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import com.apollopharmacy.vishwam.ui.rider.service.NetworkUtils
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetroQrFileUpload {
    var context: Context? = null
    var fileUploadCallback: RetroQrFileUploadCallback? = null
    var fileUploadModelList: List<RetroQrFileUploadModel>? = null
    fun uploadFiles(
        context: Context,
        fileUploadCallback: RetroQrFileUploadCallback,
        fileUploadModelList: List<RetroQrFileUploadModel>?,
    ) {
        this.context = context
        this.fileUploadCallback = fileUploadCallback
        this.fileUploadModelList = fileUploadModelList;

        if (NetworkUtils.isNetworkConnected(context)) {
            showLoading(context!!)
            uploadFile(fileUploadModelList!!.get(0))
        } else {
            fileUploadCallback.onFailureUpload("Something went wrong.")
        }
    }

    fun uploadFile(fileUploadModel: RetroQrFileUploadModel) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val apiInterface = ApiClient.getApiServiceVishwam()

            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var sensingFileUploadRequest = SensingFileUploadRequest()
            sensingFileUploadRequest.Filename = fileUploadModel.file

            var baseUrl ="https://blbext.apollopharmacy.org:3443/SENSING/Apollo/SensingFileUpload"
            //"https://blbext.apollopharmacy.org:3443/SENSING/Apollo/SensingFileUpload"
            var token = "cTfznn4yhybBR7WSrNJn1gpos"
            //"9f15bdd0fcd5423190cHNK"
//            for (i in data.APIS.indices) {
//                if (data.APIS[i].NAME.equals("SEN BLOB")) {
//                    baseUrl = data.APIS[i].URL
//                    token = data.APIS[i].TOKEN
//                    break
//                }
//            }

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
        fileUploadModel: RetroQrFileUploadModel,
        sensingFileUploadResponse: SensingFileUploadResponse,
    ) {
        fileUploadModel.isFileUploaded = true
        fileUploadModel.sensingFileUploadResponse = sensingFileUploadResponse
        var fileUploadModelTemp: RetroQrFileUploadModel? = null
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
//            fileUploadCallback!!.allFilesUploaded(fileUploadModelList)
            downloadFiles(context!!, fileUploadCallback!!, fileUploadModelList)
        }
    }

    fun onFailureFileUpload(
        fileUploadModel: RetroQrFileUploadModel,
        sensingFileUploadResponse: SensingFileUploadResponse,
    ) {
        ActivityUtils.hideDialog()
        Toast.makeText(context, "File upload failed", Toast.LENGTH_SHORT).show()
    }


    fun downloadFiles(
        context: Context,
        fileUploadCallback: RetroQrFileUploadCallback,
        fileUploadModelList: List<RetroQrFileUploadModel>?,
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

    fun downloadFile(fileUploadModel: RetroQrFileUploadModel) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val apiInterface = ApiClient.getApiServiceVishwam()

            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var fileDownloadRequest = RetroQrFileDownloadRequest()
            fileDownloadRequest.RefURL = fileUploadModel.sensingFileUploadResponse!!.referenceurl

//            var baseUrl="http://172.16.103.116:8449/Apollo/SensingSingleFileDownload"
//            var token = "cTfznn4yhybBR7WSrNJn1gpos"
            var baseUrl="https://blbext.apollopharmacy.org:3443/SENSING/Apollo/SensingSingleFileDownload"
            var token = "cTfznn4yhybBR7WSrNJn1gpos"
            /*for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("SEN BLOB")) {
                    baseUrl = data.APIS[i].URL
                    token = data.APIS[i].TOKEN
                    break
                }
            }*/


            val call = apiInterface.FILE_DOWNLOAD_API_CALL_QR_RETRO(
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
        fileUploadModel: RetroQrFileUploadModel,
        fileDownloadResponse: RetroQrFileDownloadResponse,
    ) {
        fileUploadModel.isFileDownloaded = true
        fileUploadModel.fileDownloadResponse = fileDownloadResponse
        var fileUploadModelTemp: RetroQrFileUploadModel? = null
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
            fileUploadCallback!!.allFilesDownloaded(fileUploadModelList)
        }
    }

    fun onFailureFileDownload(
        fileUploadModel: RetroQrFileUploadModel,
        fileDownloadResponse: RetroQrFileDownloadResponse,
    ) {
        ActivityUtils.hideDialog()
        Toast.makeText(context, "File download failed", Toast.LENGTH_SHORT).show()
    }
}