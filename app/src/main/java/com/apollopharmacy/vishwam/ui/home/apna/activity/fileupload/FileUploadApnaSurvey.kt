package com.apollopharmacy.vishwam.ui.home.apna.activity.fileupload

import android.content.Context
import android.widget.Toast
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.network.ApiClient
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
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

class FileUploadApnaSurvey {

    var context: Context? = null
    var apnaNewSurveyCallBack: ApnaNewSurveyCallBack? = null
    var fileUploadApnaSurveyModelList: List<FileUploadApnaSurveyModel>? = null
    var isImageUpload: Boolean? = null


    fun uploadFiles(
        context: Context,
        apnaNewSurveyCallBack: ApnaNewSurveyCallBack,
        fileUploadApnaSurveyModelList: List<FileUploadApnaSurveyModel>?, isImageUpload: Boolean,
    ) {
        this.context = context
        this.apnaNewSurveyCallBack = apnaNewSurveyCallBack
        this.fileUploadApnaSurveyModelList = fileUploadApnaSurveyModelList;
        this.isImageUpload = isImageUpload

        if (NetworkUtils.isNetworkConnected(context)) {
//            Utlis.showLoading(context!!)
            uploadFile(fileUploadApnaSurveyModelList!!.get(0))
        } else {
            apnaNewSurveyCallBack.onFailureUpload("Something went wrong.")
        }
    }

    fun uploadFile(fileUploadApnaSurveyModel: FileUploadApnaSurveyModel) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val apiInterface = ApiClient.getApiServiceVishwam()

            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var sensingFileUploadRequest = SensingFileUploadRequest()
            sensingFileUploadRequest.Filename = fileUploadApnaSurveyModel.file

            var baseUrl =
                ""//"https://blbext.apollopharmacy.org:3443/SENSING/Apollo/SensingFileUpload"
            var token = ""//"9f15bdd0fcd5423190cHNK"
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("APNA SURVEY BLOBUPLOAD")) {//SEN BLOBUPLOAD
                    baseUrl = data.APIS[i].URL
                    token = data.APIS[i].TOKEN
                    break
                }
            }

            val requestBody =
                RequestBody.create("*/*".toMediaTypeOrNull(), fileUploadApnaSurveyModel.file!!)
            val fileToUpload =
                MultipartBody.Part.createFormData(
                    "file",
                    fileUploadApnaSurveyModel.file!!.name,
                    requestBody
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
                        onSuccessFileUpload(fileUploadApnaSurveyModel, response.body()!!)
                    } else {
                        onFailureFileUpload(fileUploadApnaSurveyModel, response.body()!!)
                    }
                }

                override fun onFailure(call: Call<SensingFileUploadResponse?>, t: Throwable) {
//                    ActivityUtils.hideDialog()
                    apnaNewSurveyCallBack!!.onFailureUpload(t.message!!)
                }
            })
        } else {
            apnaNewSurveyCallBack!!.onFailureUpload("Something went wrong.")
        }
    }

    fun onSuccessFileUpload(
        fileUploadApnaSurveyModel: FileUploadApnaSurveyModel,
        sensingFileUploadResponse: SensingFileUploadResponse,
    ) {
        fileUploadApnaSurveyModel.isFileUploaded = true
        fileUploadApnaSurveyModel.sensingFileUploadResponse = sensingFileUploadResponse
        var fileUploadApnaSurveyModelTemp: FileUploadApnaSurveyModel? = null
        for (i in fileUploadApnaSurveyModelList!!) {
            if (!i.isFileUploaded) {
                fileUploadApnaSurveyModelTemp = i
                break
            }
        }
        if (fileUploadApnaSurveyModelTemp != null) {
            uploadFile(fileUploadApnaSurveyModelTemp)
        } else {
//            Utlis.hideLoading()
//            apnaNewSurveyCallBack!!.allFilesUploaded(fileUploadApnaSurveyModelList, isImageUpload!!)
            downloadFiles(context!!, apnaNewSurveyCallBack!!, fileUploadApnaSurveyModelList)
        }
    }

    fun onFailureFileUpload(
        fileUploadApnaSurveyModel: FileUploadApnaSurveyModel,
        sensingFileUploadResponse: SensingFileUploadResponse,
    ) {
//        ActivityUtils.hideDialog()
        Utlis.hideLoading()
        Toast.makeText(context, "File upload failed", Toast.LENGTH_SHORT).show()
    }


    fun downloadFiles(
        context: Context,
        apnaNewSurveyCallBack: ApnaNewSurveyCallBack,
        fileUploadApnaSurveyModelList: List<FileUploadApnaSurveyModel>?,
    ) {
        this.context = context
        this.apnaNewSurveyCallBack = apnaNewSurveyCallBack
        this.fileUploadApnaSurveyModelList = fileUploadApnaSurveyModelList;

        if (NetworkUtils.isNetworkConnected(context)) {
//            Utlis.showLoading(context!!)
            downloadFile(fileUploadApnaSurveyModelList!!.get(0))
        } else {
            apnaNewSurveyCallBack.onFailureUpload("Something went wrong.")
        }
    }

    fun downloadFile(fileUploadApnaSurveyModel: FileUploadApnaSurveyModel) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val apiInterface = ApiClient.getApiServiceVishwam()

            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var fileDownloadRequest = FileDownloadRequest()
            fileDownloadRequest.RefURL =
                fileUploadApnaSurveyModel.sensingFileUploadResponse!!.referenceurl

            var baseUrl = ""
            //"https://blbext.apollopharmacy.org:3443/SENSING/Apollo/SensingSingleFileDownload"
            var token = "" //"9f15bdd0fcd5423190cHNK"
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("APNA SURVEY BLOBDOWNLOAD")) {
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
                        onSuccessFileDownload(fileUploadApnaSurveyModel, response.body()!!)
                    } else {
                        onFailureFileDownload(fileUploadApnaSurveyModel, response.body()!!)
                    }
                }

                override fun onFailure(call: Call<FileDownloadResponse?>, t: Throwable) {
//                    ActivityUtils.hideDialog()
                    apnaNewSurveyCallBack!!.onFailureUpload(t.message!!)
                }
            })
        } else {
            apnaNewSurveyCallBack!!.onFailureUpload("Something went wrong.")
        }
    }

    fun onSuccessFileDownload(
        fileUploadApnaSurveyModel: FileUploadApnaSurveyModel,
        fileDownloadResponse: FileDownloadResponse,
    ) {
        fileUploadApnaSurveyModel.isFileDownloaded = true
        fileDownloadResponse.decryptedUrl = RijndaelCipherEncryptDecrypt().decrypt(
            fileDownloadResponse.referenceurl,
            RijndaelCipherEncryptDecrypt().key
        )
        fileUploadApnaSurveyModel.fileDownloadResponse = fileDownloadResponse
        var fileUploadModelTemp: FileUploadApnaSurveyModel? = null
        for (i in fileUploadApnaSurveyModelList!!) {
            if (!i.isFileDownloaded) {
                fileUploadModelTemp = i
                break
            }
        }
        if (fileUploadModelTemp != null) {
            downloadFile(fileUploadModelTemp)
        } else {
//            Utlis.hideLoading()
            apnaNewSurveyCallBack!!.allFilesDownloaded(
                fileUploadApnaSurveyModelList,
                isImageUpload!!
            )
        }
    }

    fun onFailureFileDownload(
        fileUploadApnaSurveyModel: FileUploadApnaSurveyModel,
        fileDownloadResponse: FileDownloadResponse,
    ) {
        ActivityUtils.hideDialog()
        Toast.makeText(context, "File download failed", Toast.LENGTH_SHORT).show()
    }
}