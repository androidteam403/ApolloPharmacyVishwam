package com.apollopharmacy.vishwam.util.fileuploadswach

import android.content.Context
import android.widget.Toast
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.network.ApiClient
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadRequest
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import com.apollopharmacy.vishwam.ui.rider.service.NetworkUtils
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.fileupload.FileDownloadRequest
import com.apollopharmacy.vishwam.util.fileupload.FileDownloadResponse
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FileUploadSwach {
    var context: Context? = null
    var fileUploadSwachCallback: FileUploadSwachCallback? = null
    var fileUploadModelList: List<FileUploadSwachModel>? = null


    fun uploadFiles(
        context: Context,
        fileUploadSwachCallback: FileUploadSwachCallback,
        fileUploadModelList: List<FileUploadSwachModel>?,
    ) {
        this.context = context
        this.fileUploadSwachCallback = fileUploadSwachCallback
        this.fileUploadModelList = fileUploadModelList;

        if (NetworkUtils.isNetworkConnected(context)) {
            Utlis.showLoading(context!!)
            uploadFile(fileUploadModelList!!.get(0))
        } else {
            fileUploadSwachCallback.onFailureUpload("Something went wrong.")
        }
    }

    fun uploadFile(fileUploadModel: FileUploadSwachModel) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val apiInterface = ApiClient.getApiServiceVishwam()

            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var sensingFileUploadRequest = SensingFileUploadRequest()
            sensingFileUploadRequest.Filename = fileUploadModel.file

            var baseUrl =
                "" //"https://blbext.apollopharmacy.org:3443/SENSING/Apollo/SensingFileUpload"
            var token = "" //"9f15bdd0fcd5423190cHNK"
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("SWACHH BLOBUPLOAD")) {
                    baseUrl = data.APIS[i].URL
                    token = data.APIS[i].TOKEN
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
                    fileUploadSwachCallback!!.onFailureUpload(t.message!!)
                }
            })
        } else {
            fileUploadSwachCallback!!.onFailureUpload("Something went wrong.")
        }
    }

    fun onSuccessFileUpload(
        fileUploadModel: FileUploadSwachModel,
        sensingFileUploadResponse: SensingFileUploadResponse,
    ) {
        fileUploadModel.isFileUploaded = true
        fileUploadModel.sensingFileUploadResponse = sensingFileUploadResponse
        var fileUploadModelTemp: FileUploadSwachModel? = null
        for (i in fileUploadModelList!!) {
            if (!i.isFileUploaded) {
                fileUploadModelTemp = i
                break
            }
        }
        if (fileUploadModelTemp != null) {
            uploadFile(fileUploadModelTemp)
        } else {
            Utlis.hideLoading()
//            fileUploadCallbackChamps!!.allFilesUploaded(fileUploadModelList!!)
            downloadFiles(context!!, fileUploadSwachCallback!!, fileUploadModelList)
        }
    }

    fun onFailureFileUpload(
        fileUploadModel: FileUploadSwachModel,
        sensingFileUploadResponse: SensingFileUploadResponse,
    ) {
        ActivityUtils.hideDialog()
        Toast.makeText(context, "File upload failed", Toast.LENGTH_SHORT).show()
    }

    fun downloadFiles(
        context: Context,
        fileUploadSwachCallback: FileUploadSwachCallback,
        fileUploadModelList: List<FileUploadSwachModel>?,
    ) {
        this.context = context
        this.fileUploadSwachCallback = fileUploadSwachCallback
        this.fileUploadModelList = fileUploadModelList;

        if (NetworkUtils.isNetworkConnected(context)) {
            Utlis.showLoading(context!!)
            downloadFile(fileUploadModelList!!.get(0))
        } else {
            fileUploadSwachCallback.onFailureUpload("Something went wrong.")
        }
    }

    fun downloadFile(fileUploadModel: FileUploadSwachModel) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val apiInterface = ApiClient.getApiServiceVishwam()

            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var fileDownloadRequest = FileDownloadRequest()
            fileDownloadRequest.RefURL = fileUploadModel.sensingFileUploadResponse!!.referenceurl

            var baseUrl =
                ""
            var token = ""
           for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("SWACHH BLOBDOWNLOAD")) {
                    baseUrl = data.APIS[i].URL
                    token = data.APIS[i].TOKEN
                    break
                }
            }


            val call = apiInterface.FILE_DOWNLOAD_API_CALL(
                baseUrl, token, fileDownloadRequest
            )
            call.enqueue(object : Callback<FileDownloadResponse?> {


                override fun onResponse(
                    call: Call<FileDownloadResponse?>,
                    response: Response<FileDownloadResponse?>,
                ) {
                    if (response.body() != null && response.body()!!.status == true) {
                        onSuccessFileDownload(fileUploadModel, response.body()!!)
                    } else {
                        onFailureFileDownload(fileUploadModel, response.body()!!)
                    }
                }

                override fun onFailure(call: Call<FileDownloadResponse?>, t: Throwable) {
                    ActivityUtils.hideDialog()
                    fileUploadSwachCallback!!.onFailureUpload(t.message!!)
                }
            })
        } else {
            fileUploadSwachCallback!!.onFailureUpload("Something went wrong.")
        }
    }

    fun onSuccessFileDownload(
        fileUploadModel: FileUploadSwachModel,
        fileDownloadResponse: FileDownloadResponse,
    ) {
        fileUploadModel.isFileDownloaded = true
        fileUploadModel.fileDownloadResponse = fileDownloadResponse
        var fileUploadModelTemp: FileUploadSwachModel? = null
        for (i in fileUploadModelList!!) {
            if (!i.isFileDownloaded) {
                fileUploadModelTemp = i
                break
            }
        }
        if (fileUploadModelTemp != null) {
            downloadFile(fileUploadModelTemp)
        } else {
            Utlis.hideLoading()
            fileUploadSwachCallback!!.allFilesDownloaded(fileUploadModelList!!)
        }
    }

    fun onFailureFileDownload(
        fileUploadModel: FileUploadSwachModel,
        fileDownloadResponse: FileDownloadResponse,
    ) {
        ActivityUtils.hideDialog()
        Toast.makeText(context, "File download failed", Toast.LENGTH_SHORT).show()
    }
}