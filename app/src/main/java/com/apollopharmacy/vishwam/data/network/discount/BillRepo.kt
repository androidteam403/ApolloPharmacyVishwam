package com.apollopharmacy.vishwam.data.network.discount

import com.apollopharmacy.vishwam.data.Config.ENCRIPTION_KEY
import com.apollopharmacy.vishwam.data.model.CommonRequest
import com.apollopharmacy.vishwam.data.model.discount.BillOrderResponse
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

object BillRepo {

    suspend fun callBillListRepo(
        loginApiRequest: String,
        url: String,
    ): ApiResult<BillOrderResponse> {
        return try {
            val encriptData =
                EncryptionManager.encryptData(loginApiRequest, ENCRIPTION_KEY)
            val encResponse = Api.getClient().billOrder(url, CommonRequest(encriptData.toString()))
            val decriptData = EncryptionManager.decryptData(encResponse, ENCRIPTION_KEY)
            val response = Gson().fromJson(decriptData, BillOrderResponse::class.java)
            if (response.sTATUS) {
                ApiResult.Success(response)
            } else {
                ApiResult.GenericError(null, response.mESSAGE)
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
}