package com.apollopharmacy.vishwam.data.network

import com.apollopharmacy.vishwam.ui.home.apollosensing.model.CheckScreenStatusResponse
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SaveImageUrlsRequest
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SaveImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SendGlobalSmsRequest
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SendGlobalSmsResponse
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import com.google.gson.JsonSyntaxException
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

object ApolloSensingRepo {
    suspend fun sendGlobalSmsApiCall(
        url: String, token: String,
        sendGlobalSmsRequest: SendGlobalSmsRequest?,
    ): ApiResult<SendGlobalSmsResponse> {
        return try {
            val response =
                Api.getClient().SEND_GLOBAL_SMS_API_CALL(url, token, sendGlobalSmsRequest)
            if (response.status == true) ApiResult.Success(response)
            else ApiResult.GenericError(null, "error")
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

    suspend fun getLinkApolloSensingApiCall(
        url: String,
    ): ApiResult<ResponseBody> {
        return try {
            val response = Api.getClient().GET_APOLLO_SENSING_LINK_API_CALL(url)
            if (response != null) ApiResult.Success(response)
            else ApiResult.GenericError(null, "error")
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

    suspend fun saveImageUrlsApiCall(
        url: String, token: String,
        saveImageUrlsRequest: SaveImageUrlsRequest,
    ): ApiResult<SaveImageUrlsResponse> {
        return try {
            val response =
                Api.getClient().SAVE_IMAGE_URLS_API_CALL(url, token, saveImageUrlsRequest)
            if (response.status == true) ApiResult.Success(response)
            else ApiResult.GenericError(null, "error")
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

    suspend fun checkScreenStatus(
        url: String,
    ): ApiResult<CheckScreenStatusResponse> {
        return try {
            val response = Api.getClient().CHECK_SCREEN_STATUS_API_CALL(url)
            if (response.status == true) ApiResult.Success(response)
            else ApiResult.GenericError(null, "error")
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

/*
    suspend fun sensingFileUpload(
        url: String,
        type: String,
        token: String,
        file: MultipartBody.Part,
    ): ApiResult<SensingFileUploadResponse> {
        return try {
            val response = Api.getClient()
                .SENSING_FILE_UPLOAD_API_CALL(url, type, token, file)
            if (response.status == true) ApiResult.Success(response)
            else ApiResult.GenericError(null, "error")
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
*/

}