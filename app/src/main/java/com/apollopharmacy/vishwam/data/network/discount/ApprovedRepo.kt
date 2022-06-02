package com.apollopharmacy.vishwam.data.network.discount

import com.apollopharmacy.vishwam.data.Config.ENCRIPTION_KEY
import com.apollopharmacy.vishwam.data.model.CommonRequest
import com.apollopharmacy.vishwam.data.model.discount.ApprovalOrderRequest
import com.apollopharmacy.vishwam.data.model.discount.FilterDiscountRequest
import com.apollopharmacy.vishwam.data.model.discount.PendingOrderRequest
import com.apollopharmacy.vishwam.data.network.Api
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApiUtils
import com.apollopharmacy.vishwam.util.EncryptionManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

object ApprovedRepo {

    suspend fun getApprovedList(tokeId: String, url: String): ApiResult<ApprovalOrderRequest> {
        return try {
            val encriptData = EncryptionManager.encryptData(
                Gson().toJson(PendingOrderRequest(tokeId)),
                ENCRIPTION_KEY
            )
            val response =
                Api.getClient()
                    .getApprovalAndRejectedList(url, CommonRequest(encriptData.toString()))
            val decriptData = EncryptionManager.decryptData(response, ENCRIPTION_KEY)
            val Responsee = Gson().fromJson(decriptData, ApprovalOrderRequest::class.java)
            if (Responsee.STATUS)
                ApiResult.Success(Responsee)
            else
                ApiResult.GenericError(null, Responsee.MESSAGE)
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

    suspend fun getFilteredList(
        url: String,
        filterDiscReq: FilterDiscountRequest,
    ): ApiResult<ApprovalOrderRequest> {
        return try {
            val encriptData = EncryptionManager.encryptData(Gson().toJson(filterDiscReq),
                ENCRIPTION_KEY)
            val response =
                Api.getClient().getFilteredList(url, CommonRequest(encriptData.toString()))
            val decriptData = EncryptionManager.decryptData(response, ENCRIPTION_KEY)
            val Responsee = Gson().fromJson(decriptData, ApprovalOrderRequest::class.java)
            if (Responsee.STATUS)
                ApiResult.Success(Responsee)
            else
                ApiResult.GenericError(null, Responsee.MESSAGE)
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
