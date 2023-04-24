package com.apollopharmacy.vishwam.data.network

import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveImagesUrlsRequest
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.GetStoreWiseCatDetailsApnaResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.OnUploadSwachModelRequest
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.OnUploadSwachModelResponse
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

object ApnaRectroApiRepo {
    suspend fun getStoreWiseCatDetailsApna(
        url: String,
        token: String,
        storeId: String,
    ): ApiResult<GetStoreWiseCatDetailsApnaResponse> {
        return try {
            val response = Api.getClient().getStoreWiseCatDetailsApna(url, token, storeId)
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


    suspend fun saveImageUrlsApna(
        url: String,
        token: String,
        saveImagesUrlsRequest: SaveImagesUrlsRequest,
    ): ApiResult<SaveImageUrlsResponse> {
        return try {
            val response = Api.getClient().saveImageUrlsApna(url, token, saveImagesUrlsRequest)
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