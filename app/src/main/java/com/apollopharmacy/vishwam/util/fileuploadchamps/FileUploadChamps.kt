package com.apollopharmacy.vishwam.util.fileuploadchamps

import android.content.Context
import android.widget.Toast
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.network.ApiClient
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadRequest
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse
import com.apollopharmacy.vishwam.ui.rider.service.NetworkUtils
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.fileupload.FileDownloadRequest
import com.apollopharmacy.vishwam.util.fileupload.FileDownloadResponse
import com.apollopharmacy.vishwam.util.fileupload.FileUploadCallback
import com.apollopharmacy.vishwam.util.fileupload.FileUploadModel
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FileUploadChamps {
    var context: Context? = null
    var fileUploadCallbackChamps: FileUploadChampsCallback? = null
    var fileUploadModelList: List<FileUploadChampsModel>? = null

    fun uploadFiles(
        context: Context,
        fileUploadCallbackChamps: FileUploadChampsCallback,
        fileUploadModelList: List<FileUploadChampsModel>?,
    ) {
        this.context = context
        this.fileUploadCallbackChamps = fileUploadCallbackChamps
        this.fileUploadModelList = fileUploadModelList;

        if (NetworkUtils.isNetworkConnected(context)) {
            Utlis.showLoading(context!!)
            uploadFile(fileUploadModelList!!.get(0))
        } else {
            fileUploadCallbackChamps.onFailureUpload("Something went wrong.")
        }
    }

    fun uploadFile(fileUploadModel: FileUploadChampsModel) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val apiInterface = ApiClient.getApiServiceVishwam()

            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var sensingFileUploadRequest = SensingFileUploadRequest()
            sensingFileUploadRequest.Filename = fileUploadModel.file

            var baseUrl =
                "http://172.16.103.116:8449/Apollo/SensingFileUpload" //"https://blbext.apollopharmacy.org:3443/SENSING/Apollo/SensingFileUpload"
            var token = "NGT1TNkVP49zz+tgOEwRxw==" //"9f15bdd0fcd5423190cHNK"
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("SEN BLOB")) {
//                    baseUrl = data.APIS[i].URL
//                    token = data.APIS[i].TOKEN
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
                    fileUploadCallbackChamps!!.onFailureUpload(t.message!!)
                }
            })
        } else {
            fileUploadCallbackChamps!!.onFailureUpload("Something went wrong.")
        }
    }

    fun onSuccessFileUpload(
        fileUploadModel: FileUploadChampsModel,
        sensingFileUploadResponse: SensingFileUploadResponse,
    ) {
        fileUploadModel.isFileUploaded = true
        fileUploadModel.sensingFileUploadResponse = sensingFileUploadResponse
        var fileUploadModelTemp: FileUploadChampsModel? = null
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
//           fileUploadCallbackChamps!!.allFilesUploaded(fileUploadModelList!!)
            downloadFiles(context!!, fileUploadCallbackChamps!!, fileUploadModelList)
        }
    }

    fun onFailureFileUpload(
        fileUploadModel: FileUploadChampsModel,
        sensingFileUploadResponse: SensingFileUploadResponse,
    ) {
        ActivityUtils.hideDialog()
        Toast.makeText(context, "File upload failed", Toast.LENGTH_SHORT).show()
    }

    fun downloadFiles(
        context: Context,
        fileUploadCallbackChamps: FileUploadChampsCallback,
        fileUploadModelList: List<FileUploadChampsModel>?,
    ) {
        this.context = context
        this.fileUploadCallbackChamps = fileUploadCallbackChamps
        this.fileUploadModelList = fileUploadModelList;

        if (NetworkUtils.isNetworkConnected(context)) {
            Utlis.showLoading(context!!)
            downloadFile(fileUploadModelList!!.get(0))
        } else {
            fileUploadCallbackChamps.onFailureUpload("Something went wrong.")
        }
    }

    fun downloadFile(fileUploadModel: FileUploadChampsModel) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val apiInterface = ApiClient.getApiServiceVishwam()

            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var fileDownloadRequest = FileDownloadRequest()
            fileDownloadRequest.RefURL = fileUploadModel.sensingFileUploadResponse!!.referenceurl

            var baseUrl =
                "http://172.16.103.116:8449/Apollo/SensingSingleFileDownload"
            var token = "NGT1TNkVP49zz+tgOEwRxw=="
            /*for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("SEN BLOB")) {
                    baseUrl = data.APIS[i].URL
                    token = data.APIS[i].TOKEN
                    break
                }
            }*/


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
                    fileUploadCallbackChamps!!.onFailureUpload(t.message!!)
                }
            })
        } else {
            fileUploadCallbackChamps!!.onFailureUpload("Something went wrong.")
        }
    }

    fun onSuccessFileDownload(
        fileUploadModel: FileUploadChampsModel,
        fileDownloadResponse: FileDownloadResponse,
    ) {
        fileUploadModel.isFileDownloaded = true
        fileUploadModel.fileDownloadResponse = fileDownloadResponse
        var fileUploadModelTemp: FileUploadChampsModel? = null
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
            fileUploadCallbackChamps!!.allFilesDownloaded(fileUploadModelList!!)
        }
    }

    fun onFailureFileDownload(
        fileUploadModel: FileUploadChampsModel,
        fileDownloadResponse: FileDownloadResponse,
    ) {
        ActivityUtils.hideDialog()
        Toast.makeText(context, "File download failed", Toast.LENGTH_SHORT).show()
    }
}