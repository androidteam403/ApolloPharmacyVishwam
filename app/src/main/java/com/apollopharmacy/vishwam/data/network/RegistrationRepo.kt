package com.apollopharmacy.vishwam.data.network

import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.CMSCommonRequest
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.cms.*
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.CmsTicketRequest
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.CmsTicketResponse
import com.apollopharmacy.vishwam.ui.home.swach.model.AppLevelDesignationModelResponse
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.ApproveRejectListRequest
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.ApproveRejectListResponse
import com.apollopharmacy.vishwam.util.EncryptionManager
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

object RegistrationRepo {
    suspend fun callDepartmentList(
        token: String,
        baseUrl: String,
        url: String,
    ): ApiResult<DepartmentV2Response> {
        return try {
            val CommonResponse =
                Api.getClient().getDepartmentList(
                    token,
                    baseUrl,
                    CMSCommonRequest(url, "")
                )
            if (CommonResponse.status) {
                return try {
                    val decryptData =
                        EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                    val response = Gson().fromJson(decryptData, DepartmentV2Response::class.java)
                    if (response.status.equals("true"))
                        ApiResult.Success(response)
                    else
                        ApiResult.GenericError(null, "error")
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            } else {
                return try {
                    val decryptData =
                        EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                    ApiResult.UnknownError(decryptData)
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }

    suspend fun submitComplain(
        token: String, baseUrl: String,
        url: String,
        registrationSubmit: SubmitNewV2Response,
    ): ApiResult<SubmitResponse> {
        return try {
            val encriptData = EncryptionManager.encryptData(
                Gson().toJson(registrationSubmit),
                Config.ENCRIPTION_KEY
            )
            val CommonResponse =
                Api.getClient().submitComplain(
                    token,
                    baseUrl,
                    CMSCommonRequest(url, encriptData.toString())
                )
            if (CommonResponse.status) {
                return try {
                    val decryptData =
                        EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                    val response = Gson().fromJson(decryptData, SubmitResponse::class.java)
                    if (response.status)
                        ApiResult.Success(response)
                    else
                        ApiResult.GenericError(null, "error")
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            } else {
                return try {
                    val decryptData =
                        EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                    ApiResult.UnknownError(decryptData)
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }

    suspend fun submitComplainWithImages(
        token: String, baseUrl: String,
        url: String,
        submitRequestWithImages: SubmitNewV2Response,
    ): ApiResult<SubmitResponse> {
        return try {
            val encriptData = EncryptionManager.encryptData(
                Gson().toJson(submitRequestWithImages),
                Config.ENCRIPTION_KEY
            )
            val CommonResponse =
                Api.getClient().submitComplainWithImage(
                    token,
                    baseUrl,
                    CMSCommonRequest(url, encriptData.toString())
                )
            if (CommonResponse.status) {
                return try {
                    val decryptData =
                        EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                    val response = Gson().fromJson(decryptData, SubmitResponse::class.java)
                    if (response.status)
                        ApiResult.Success(response)
                    else
                        ApiResult.GenericError(null, "error")
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            } else {
                return try {
                    val decryptData =
                        EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                    ApiResult.UnknownError(decryptData)
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }

    //New Complaint Sumbit functionality.............................
    suspend fun NewComplaintRegistration(
        baseurl: String,
        header: String,
        requestNewComplaintRegistration: RequestNewComplaintRegistration,
    ): ApiResult<ResponseNewComplaintRegistration> {
        return try {
            val response = Api.getClient()
                .submitNewComplaintreg(baseurl, header, requestNewComplaintRegistration)
            if (response.success)
                ApiResult.Success(response)
            else
                ApiResult.GenericError(null, "error")
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }


    //Reasonslist api...............
    suspend fun getReasonslistmaster(baseUrl: String): ApiResult<ReasonmasterV2Response> {
        return try {
            val response = Api.getClient().getReasonsmaster(baseUrl)
            if (response.success)
                ApiResult.Success(response)
            else
                ApiResult.GenericError(null, "error")
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }


    suspend fun cmsTicketStatusUpdate(
        url: String,
        token: String,
        getDetailsRequest: GetDetailsRequest,
    ): ApiResult<ResponseBody> {
        return try {
            val response = Api.getClient().getDetails(url,token, getDetailsRequest)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }

//    suspend fun cmsTicketStatusUpdate(
//        url: String,
//        token: String,
//        cmsTicketRequest: CmsTicketRequest,
//    ): ApiResult<CmsTicketResponse> {
//        return try {
//            val response = Api.getClient().cmsTicketStatusUpdate(url, token, cmsTicketRequest)
//            ApiResult.Success(response)
//        } catch (e: Exception) {
//            ApiResult.UnknownError(e.message)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            ApiResult.NetworkError
//        } catch (e: Throwable) {
//            e.printStackTrace()
//            ApiResult.UnknownError(e.message)
//        } catch (e: HttpException) {
//            ApiUtils.parseHttpError(e)
//        } catch (e: UnknownError) {
//            ApiResult.UnknownError(e.message)
//        } catch (e: SocketTimeoutException) {
//            ApiResult.UnknownError(e.message)
//        } catch (e: JsonSyntaxException) {
//            ApiResult.UnknownError(e.message)
//        } catch (e: UnknownHostException) {
//            ApiResult.UnknownError(e.message)
//        } catch (e: ConnectException) {
//            ApiResult.UnknownError(e.message)
//        } catch (e: SocketException) {
//            ApiResult.UnknownError(e.message)
//        } catch (e: TimeoutException) {
//            ApiResult.UnknownError(e.message)
//        } catch (e: UnknownHostException) {
//            ApiResult.UnknownHostException(e.message)
//        }
//    }




    //New Complaint Sumbit functionality.............................
    suspend fun getticketresolvedstatus(
        baseurl: String,
    ): ApiResult<ResponseTicktResolvedapi> {
        return try {
            val response = Api.getClient()
                .getresolvedticketstatus(baseurl)
            if (!response.success)
                ApiResult.Success(response)
            else
                ApiResult.GenericError(null, "error")
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }

    //cms Login api..........
    suspend fun getCMSLoginApi(
        baseurl: String, cmsLogin: RequestCMSLogin,
    ): ApiResult<ResponseCMSLogin> {
        return try {
            val response = Api.getClient()
                .cmsLoginapi(baseurl, cmsLogin)
            if (response.success)
                ApiResult.Success(response)
            else
                ApiResult.GenericError(null, "error")
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }


    //ticket Rating api........
    suspend fun getTicketRating(
        baseurl: String,
    ): ApiResult<ResponseticketRatingApi> {
        return try {
            val response = Api.getClient()
                .getTicketRating(baseurl)
            if (response.success)
                ApiResult.Success(response)
            else
                ApiResult.GenericError(null, "error")
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }

    //ticketresolved or closing api.............
    suspend fun getTicketClosingapi(
        baseurl: String,
        contenttype: String,
        autherization: String,
        requestClosedticketApi: RequestClosedticketApi,
    ): ApiResult<ResponseClosedTicketApi> {
        return try {
            val response = Api.getClient()
                .ticketclosingapi(baseurl, contenttype, autherization, requestClosedticketApi)
            if (response.success)
                ApiResult.Success(response)
            else
                ApiResult.GenericError(null, "error")
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }


    //ticket Resolved list api................
    /*  suspend fun getticketresolvedstatus(site : String?,department:String?): ApiResult<ResponseTicktResolvedapi> {
          return try {
              val response = Api.getClient().getresolvedticketstatus(site,department)
              if (response.success)
                  ApiResult.Success(response)
              else
                  ApiResult.GenericError(null, "error")
          } catch (e: Exception) {
              ApiResult.UnknownError(e.message)
          } catch (e: IOException) {
              e.printStackTrace()
              ApiResult.NetworkError
          } catch (e: Throwable) {
              e.printStackTrace()
              ApiResult.UnknownError(e.message)
          } catch (e: HttpException) {
              ApiUtils.parseHttpError(e)
          } catch (e: UnknownError) {
              ApiResult.UnknownError(e.message)
          } catch (e: SocketTimeoutException) {
              ApiResult.UnknownError(e.message)
          } catch (e: JsonSyntaxException) {
              ApiResult.UnknownError(e.message)
          } catch (e: UnknownHostException) {
              ApiResult.UnknownError(e.message)
          } catch (e: ConnectException) {
              ApiResult.UnknownError(e.message)
          } catch (e: SocketException) {
              ApiResult.UnknownError(e.message)
          } catch (e: TimeoutException) {
              ApiResult.UnknownError(e.message)
          } catch (e: UnknownHostException) {
              ApiResult.UnknownHostException(e.message)
          }
      }*/


    /* //Ticket List Api.........
     suspend fun getticketList(proxyApiRequest: ProxyApiRequest): ApiResult<ResponseNewTicketlist> {
         return try {
             val response = Api.getClient().getTicketlist(url,siteid,fromdate,todate)
             if (response.success)
                 ApiResult.Success(response)
             else
                 ApiResult.GenericError(null, "error")
         } catch (e: Exception) {
             ApiResult.UnknownError(e.message)
         } catch (e: IOException) {
             e.printStackTrace()
             ApiResult.NetworkError
         } catch (e: Throwable) {
             e.printStackTrace()
             ApiResult.UnknownError(e.message)
         } catch (e: HttpException) {
             ApiUtils.parseHttpError(e)
         } catch (e: UnknownError) {
             ApiResult.UnknownError(e.message)
         } catch (e: SocketTimeoutException) {
             ApiResult.UnknownError(e.message)
         } catch (e: JsonSyntaxException) {
             ApiResult.UnknownError(e.message)
         } catch (e: UnknownHostException) {
             ApiResult.UnknownError(e.message)
         } catch (e: ConnectException) {
             ApiResult.UnknownError(e.message)
         } catch (e: SocketException) {
             ApiResult.UnknownError(e.message)
         } catch (e: TimeoutException) {
             ApiResult.UnknownError(e.message)
         } catch (e: UnknownHostException) {
             ApiResult.UnknownHostException(e.message)
         }
     }*/


    //Ticket List Api.........
    suspend fun getticketList(
        url: String,
        siteid: String,
        fromdate: String,
        todate: String,
    ): ApiResult<ResponseNewTicketlist> {
        return try {
            val response = Api.getClient().getTicketlist(url, siteid, fromdate, todate)
            if (response.success)
                ApiResult.Success(response)
            else
                ApiResult.GenericError(null, "error")
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }


    //ticket History.....

    suspend fun getticketHistory(
        url: String,
        page: Int,
        rows: Int,
        dependents: String,
    ): ApiResult<ResponseNewTicketlist.NewTicketHistoryResponse> {
        return try {
            val response = Api.getClient().getTicketHistoryApi(url, page, rows, dependents)
            if (response.success)
                ApiResult.Success(response)
            else
                ApiResult.GenericError(null, "error")
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }


    suspend fun selectSiteId(token: String, baseUrl: String): ApiResult<SiteDto> {
        return try {
            val response = Api.getClient().getSiteId(token, baseUrl)
            if (response.status)
                ApiResult.Success(response)
            else
                ApiResult.GenericError(null, "error")
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }

    //called after selection of site id
    suspend fun getListOfAcknowledgement(
        token: String, baseUrl: String,
        url: String,
        trackingListDto: TrackingListDto,
    ): ApiResult<PendingListToAcknowledge> {
        return try {
            val encriptData = EncryptionManager.encryptData(
                Gson().toJson(trackingListDto),
                Config.ENCRIPTION_KEY
            )
            val CommonResponse =
                Api.getClient().getTrackingList(
                    token,
                    baseUrl,
                    CMSCommonRequest(url, encriptData.toString())
                )
            if (CommonResponse.status) {
                val decryptData =
                    EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                return try {
                    val Responsee =
                        Gson().fromJson(decryptData, PendingListToAcknowledge::class.java)
                    if (Responsee.status == "true")
                        ApiResult.Success(Responsee)
                    else
                        ApiResult.GenericError(null, Responsee.message)
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            } else {
                return try {
                    val decryptData =
                        EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                    ApiResult.UnknownError(decryptData)
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }

    //called to get the list of complain
    suspend fun getListOfComplain(
        token: String, baseUrl: String,
        url: String,
        requestComplainList: RequestComplainList,
    ): ApiResult<CmsResposeV2> {
        return try {
            val encriptData = EncryptionManager.encryptData(
                Gson().toJson(requestComplainList),
                Config.ENCRIPTION_KEY
            )
            val CommonResponse =
                Api.getClient().getListOfComplain(
                    token,
                    baseUrl,
                    CMSCommonRequest(url, encriptData.toString())
                )
            if (CommonResponse.status) {
                return try {
                    val decryptData =
                        EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                    val response = Gson().fromJson(decryptData, CmsResposeV2::class.java)
                    if (response.status.equals("true")) {
                        ApiResult.Success(response)
                    } else {
                        if (response.message.equals("No tickets found")) {
                            ApiResult.Success(response)
                        } else {
                            ApiResult.GenericError(null, response.message)
                        }
                    }
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            } else {
                return try {
                    val decryptData =
                        EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                    ApiResult.UnknownError(decryptData)
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }


    //newticket list...........................


    //called to accept and reject the list of  complain
    suspend fun submitAcknowledgement(
        token: String, baseUrl: String,
        url: String,
        submitAcknowledge: SubmitAcknowledge,
    ): ApiResult<SubmitAcknowledgeResponse> {
        return try {
            val encriptData = EncryptionManager.encryptData(
                Gson().toJson(submitAcknowledge),
                Config.ENCRIPTION_KEY
            )
            val CommonResponse =
                Api.getClient().acknowledgeTheList(
                    token,
                    baseUrl,
                    CMSCommonRequest(url, encriptData.toString())
                )
            if (CommonResponse.status) {
                return try {
                    val decryptData =
                        EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                    val response =
                        Gson().fromJson(decryptData, SubmitAcknowledgeResponse::class.java)
                    if (response.status.equals("true"))
                        ApiResult.Success(response)
                    else
                        ApiResult.GenericError(null, "error")
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            } else {
                return try {
                    val decryptData =
                        EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                    ApiResult.UnknownError(decryptData)
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }

    }

    suspend fun getArticleCodeSearch(
        token: String, baseUrl: String,
        url: String,
        articleCodeRequest: ArticleCodeRequest,
    ): ApiResult<ArticleCodeResponse> {
        return try {
            val encriptData = EncryptionManager.encryptData(
                Gson().toJson(articleCodeRequest),
                Config.ENCRIPTION_KEY
            )
            val CommonResponse =
                Api.getClient().fetchArticleCode(
                    token,
                    baseUrl,
                    CMSCommonRequest(url, encriptData.toString())
                )
            if (CommonResponse.status) {
                return try {
                    val decryptData =
                        EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                    val response = Gson().fromJson(decryptData, ArticleCodeResponse::class.java)
                    if (response.status.equals("true"))
                        ApiResult.Success(response)
                    else
                        if (response.status.equals("false") && response.data.isEmpty()) {
                            ApiResult.Success(response)
                        } else
                            ApiResult.GenericError(null, "error")
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            } else {
                var decryptData =
                    EncryptionManager.decryptData(CommonResponse.message, Config.ENCRIPTION_KEY)
                return try {
                    ApiResult.UnknownError(decryptData)
                } catch (e: Exception) {
                    ApiResult.UnknownError(e.message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ApiResult.NetworkError
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ApiResult.UnknownError(e.message)
                } catch (e: HttpException) {
                    ApiUtils.parseHttpError(e)
                } catch (e: UnknownError) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketTimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: JsonSyntaxException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: ConnectException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: SocketException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: TimeoutException) {
                    ApiResult.UnknownError(e.message)
                } catch (e: UnknownHostException) {
                    ApiResult.UnknownHostException(e.message)
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }

    suspend fun submitEmpWithSiteIDReg(
        token: String,
        baseUrl: String,
        userSiteIDRegReqModel: UserSiteIDRegReqModel,
    ): ApiResult<UserSiteIDRegResModel> {
        return try {
            val response =
                Api.getClient().submitEmpWithSiteIDReg(token, baseUrl, userSiteIDRegReqModel)
            if (response.STATUS)
                ApiResult.Success(response)
            else
                ApiResult.GenericError(null, "error")
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }

    fun saveStoreInfo(selectedStoreItem: StoreListItem) {
        Preferences.saveSiteInformation(Gson().toJson(selectedStoreItem))
    }

    fun getStoreInfo(): LoginDetails.StoreData? {
        val storeString = Preferences.getSiteInformation()
        return try {
            Gson().fromJson(storeString, LoginDetails.StoreData::class.java)
        } catch (e: JsonParseException) {
            e.printStackTrace()
            return null
        }
    }

    suspend fun getDetails(
        url: String,
        token: String,
        getDetailsRequest: GetDetailsRequest,
    ): ApiResult<ResponseBody> {
        return try {
            val response = Api.getClient().getDetails(url,token, getDetailsRequest)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }

    suspend fun getApplevelDesignation(
        url: String,
        empId: String,
        appType: String,
    ): ApiResult<AppLevelDesignationModelResponse> {
        return try {
            val response = Api.getClient().appLevelDesignation(url, empId, appType)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }


    suspend fun uploadImage(url: String,
        imageName: String,
        file: GetDetailsRequest,
    ): ApiResult<ResponseBody> {
        return try {
            val response = Api.getClient().getUploadProxImage(url,imageName, file)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.UnknownError(e.message)
        } catch (e: HttpException) {
            ApiUtils.parseHttpError(e)
        } catch (e: UnknownError) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketTimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: JsonSyntaxException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownError(e.message)
        } catch (e: ConnectException) {
            ApiResult.UnknownError(e.message)
        } catch (e: SocketException) {
            ApiResult.UnknownError(e.message)
        } catch (e: TimeoutException) {
            ApiResult.UnknownError(e.message)
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
        }
    }
}