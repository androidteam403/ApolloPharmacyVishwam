package com.apollopharmacy.vishwam.data.network.discount

import com.apollopharmacy.vishwam.data.Config.ENCRIPTION_KEY
import com.apollopharmacy.vishwam.data.model.CommonRequest
import com.apollopharmacy.vishwam.data.model.discount.*
import com.apollopharmacy.vishwam.data.network.Api
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApiUtils
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlsModelApnaRequest
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlsModelApnaResponse
import com.apollopharmacy.vishwam.util.EncryptionManager
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

object PendingRepo {

    suspend fun getPendingOrderList(
        loginApiRequest: PendingOrderRequest,
        url: String,
    ): ApiResult<PendingOrder> {
        return try {
           // var temppendingrequest=PendingOrderRequest("APL00002")
            val encriptData =
                EncryptionManager.encryptData(Gson().toJson(loginApiRequest), ENCRIPTION_KEY)
            Utils.printMessage("EncriptPendingList", encriptData.toString().trim())
            val response =
                Api.getClient().getPendingList(url, CommonRequest(encriptData.toString()))
            Utils.printMessage("Check Decrypt Time : ", encriptData.toString().trim())
            val decriptData = EncryptionManager.decryptData(response, ENCRIPTION_KEY)
            val Responsee = Gson().fromJson(decriptData, PendingOrder::class.java)
            Utils.printMessage("Check Response Time : ", Responsee.toString())
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


    suspend fun getDiscountColorDetails(
        url: String,
        token: String,

    ): ApiResult<GetDiscountColorResponse> {
        return try {
            val response = Api.getClient().getDiscountColorDetails(url, token)
            ApiResult.Success(response)
        }
        catch (e: Exception) {
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

    suspend fun acceptTheDiscount(
        acceptOrRejectDiscountOrder: AcceptOrRejectDiscountOrder,
        url: String,
    ): ApiResult<SimpleResponse> {
        return try {
            val encriptData = EncryptionManager.encryptData(
                Gson().toJson(acceptOrRejectDiscountOrder),
                ENCRIPTION_KEY
            )
            val response =
                Api.getClient().acceptOrReject(url, CommonRequest(encriptData.toString()))
            val decriptData = EncryptionManager.decryptData(response, ENCRIPTION_KEY)
            val Responsee = Gson().fromJson(decriptData, SimpleResponse::class.java)
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

    suspend fun bulkAcceptTheDiscount(
        baseUrl: String,
        acceptRejItem: BulkAcceptOrRejectDiscountOrder,
    ): ApiResult<SimpleResponse> {
        val encriptData = EncryptionManager.encryptData(
            Gson().toJson(acceptRejItem),
            ENCRIPTION_KEY
        )
        return try {
            val response =
                Api.getClient().bulkAcceptOrReject(baseUrl, CommonRequest(encriptData.toString()))
            val decriptData = EncryptionManager.decryptData(response, ENCRIPTION_KEY)
            val Responsee = Gson().fromJson(decriptData, SimpleResponse::class.java)
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