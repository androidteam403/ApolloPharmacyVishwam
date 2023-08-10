package com.apollopharmacy.vishwam.ui.home.cms.cmsfileupload

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
import com.apollopharmacy.vishwam.util.rijndaelcipher.RijndaelCipherEncryptDecrypt
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CmsFileUpload {
    var context: Context? = null
    var cmsFileUploadCallback: CmsFileUploadCallback? = null
    var cmsFileUploadModelList: List<CmsFileUploadModel>? = null
    var tag: String? = null
    fun uploadFiles(
        context: Context,
        cmsFileUploadCallback: CmsFileUploadCallback,
        cmsFileUploadModelList: List<CmsFileUploadModel>?, tag: String,
    ) {
        this.context = context
        this.cmsFileUploadCallback = cmsFileUploadCallback
        this.cmsFileUploadModelList = cmsFileUploadModelList
        this.tag = tag

        if (NetworkUtils.isNetworkConnected(context)) {
            Utlis.showLoading(context!!)
            uploadFile(cmsFileUploadModelList!!.get(0))
        } else {
            cmsFileUploadCallback.onFailureUpload("Something went wrong.")
        }
    }

    fun uploadFile(cmsFileUploadModel: CmsFileUploadModel) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val apiInterface = ApiClient.getApiServiceVishwam()

            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var sensingFileUploadRequest = SensingFileUploadRequest()
            sensingFileUploadRequest.Filename = cmsFileUploadModel.file

            var baseUrl =
                "" //"https://blbext.apollopharmacy.org:3443/SENSING/Apollo/SensingFileUpload"
            var token = "" //"9f15bdd0fcd5423190cHNK"
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("CMS BLOBUPLOAD")) {//SEN BLOB
                    baseUrl = data.APIS[i].URL
                    token = data.APIS[i].TOKEN
                    break
                }
            }

            val requestBody =
                RequestBody.create("*/*".toMediaTypeOrNull(), cmsFileUploadModel.file!!)
            val fileToUpload = MultipartBody.Part.createFormData(
                "file", cmsFileUploadModel.file!!.name, requestBody
            )

            val call = apiInterface.SENSING_FILE_UPLOAD_API_CALL(
                baseUrl, "multipart/form-data", token, fileToUpload
            )
            call.enqueue(object : Callback<SensingFileUploadResponse?> {


                override fun onResponse(
                    call: Call<SensingFileUploadResponse?>,
                    response: Response<SensingFileUploadResponse?>,
                ) {
                    if (response.body() != null && response.body()!!.status == true) {
                        onSuccessFileUpload(cmsFileUploadModel, response.body()!!)
                    } else {
                        onFailureFileUpload(cmsFileUploadModel, response.body()!!)
                    }
                }

                override fun onFailure(call: Call<SensingFileUploadResponse?>, t: Throwable) {
                    ActivityUtils.hideDialog()
                    cmsFileUploadCallback!!.onFailureUpload(t.message!!)
                }
            })
        } else {
            cmsFileUploadCallback!!.onFailureUpload("Something went wrong.")
        }
    }

    fun onSuccessFileUpload(
        cmsFileUploadModel: CmsFileUploadModel,
        sensingFileUploadResponse: SensingFileUploadResponse,
    ) {
        cmsFileUploadModel.isFileUploaded = true
        cmsFileUploadModel.sensingFileUploadResponse = sensingFileUploadResponse
        var cmsFileUploadModelTemp: CmsFileUploadModel? = null
        for (i in cmsFileUploadModelList!!) {
            if (!i.isFileUploaded) {
                cmsFileUploadModelTemp = i
                break
            }
        }
        if (cmsFileUploadModelTemp != null) {
            uploadFile(cmsFileUploadModelTemp)
        } else {
            Utlis.hideLoading()
//            fileUploadCallback!!.allFilesUploaded(cmsFileUploadModelList, tag!!)
            downloadFiles(context!!, cmsFileUploadCallback!!, cmsFileUploadModelList)
        }
    }

    fun onFailureFileUpload(
        cmsFileUploadModel: CmsFileUploadModel,
        sensingFileUploadResponse: SensingFileUploadResponse,
    ) {
        ActivityUtils.hideDialog()
        Toast.makeText(context, "File upload failed", Toast.LENGTH_SHORT).show()
    }


    fun downloadFiles(
        context: Context,
        cmsFileUploadCallback: CmsFileUploadCallback,
        cmsFileUploadModelList: List<CmsFileUploadModel>?,
    ) {
        this.context = context
        this.cmsFileUploadCallback = cmsFileUploadCallback
        this.cmsFileUploadModelList = cmsFileUploadModelList;

        if (NetworkUtils.isNetworkConnected(context)) {
            Utlis.showLoading(context!!)
            downloadFile(cmsFileUploadModelList!!.get(0))
        } else {
            cmsFileUploadCallback.onFailureUpload("Something went wrong.")
        }
    }

    fun downloadFile(cmsFileUploadModel: CmsFileUploadModel) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val apiInterface = ApiClient.getApiServiceVishwam()

            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var fileDownloadRequest = FileDownloadRequest()
            fileDownloadRequest.RefURL = cmsFileUploadModel.sensingFileUploadResponse!!.referenceurl

            var baseUrl = ""
            // "https://blbext.apollopharmacy.org:3443/SENSING/Apollo/SensingSingleFileDownload"
            var token = "" //"9f15bdd0fcd5423190cHNK"
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("CMS BLOBDOWNLOAD")) {
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
                        onSuccessFileDownload(cmsFileUploadModel, response.body()!!)
                    } else {
                        onFailureFileDownload(cmsFileUploadModel, response.body()!!)
                    }
                }

                override fun onFailure(call: Call<FileDownloadResponse?>, t: Throwable) {
                    ActivityUtils.hideDialog()
                    cmsFileUploadCallback!!.onFailureUpload(t.message!!)
                }
            })
        } else {
            cmsFileUploadCallback!!.onFailureUpload("Something went wrong.")
        }
    }

    fun onSuccessFileDownload(
        cmsFileUploadModel: CmsFileUploadModel,
        fileDownloadResponse: FileDownloadResponse,
    ) {
        cmsFileUploadModel.isFileDownloaded = true
        fileDownloadResponse.decryptedUrl = RijndaelCipherEncryptDecrypt().decrypt(
            fileDownloadResponse.referenceurl, RijndaelCipherEncryptDecrypt().key
        )
        cmsFileUploadModel.fileDownloadResponse = fileDownloadResponse
        var cmsFileUploadModelTemp: CmsFileUploadModel? = null
        for (i in cmsFileUploadModelList!!) {
            if (!i.isFileDownloaded) {
                cmsFileUploadModelTemp = i
                break
            }
        }
        if (cmsFileUploadModelTemp != null) {
            downloadFile(cmsFileUploadModelTemp)
        } else {
            Utlis.hideLoading()
            cmsFileUploadCallback!!.allFilesDownloaded(cmsFileUploadModelList, tag!!)
        }
    }

    fun onFailureFileDownload(
        cmsFileUploadModel: CmsFileUploadModel,
        fileDownloadResponse: FileDownloadResponse,
    ) {
        ActivityUtils.hideDialog()
        Toast.makeText(context, "File download failed", Toast.LENGTH_SHORT).show()
    }
}