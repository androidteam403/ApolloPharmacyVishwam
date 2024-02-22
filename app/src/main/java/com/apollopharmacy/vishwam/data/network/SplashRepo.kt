package com.apollopharmacy.vishwam.data.repo

import com.apollopharmacy.vishwam.data.Config.VALIDATEVENDOR_ENCRIPTION_KEY
import com.apollopharmacy.vishwam.data.Config.VALIDATE_VENDOR_TOKEN
import com.apollopharmacy.vishwam.data.model.CommonRequest
import com.apollopharmacy.vishwam.data.model.ValidateRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.Api
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApiUtils
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

object SplashRepo {
    suspend fun getUserValidate(validateApiRequest: ValidateRequest): ApiResult<ValidateResponse> {
        return try {
            val encryptData = EncryptionManager.encryptData(Gson().toJson(validateApiRequest),
                VALIDATEVENDOR_ENCRIPTION_KEY)
            Utils.printMessage("SplashScreen", Gson().toJson(validateApiRequest).toString())
           // System.out.println("Validate Vendor:Request-->:"+Gon().toJson(validateApiRequest))
            val response = Api.getClient()
                .getValidate(VALIDATE_VENDOR_TOKEN, CommonRequest(encryptData.toString()))
            val decryptData =
                EncryptionManager.decryptData(response, VALIDATEVENDOR_ENCRIPTION_KEY)
            val actualResponse = Gson().fromJson(decryptData, ValidateResponse::class.java)
            println(35)
            if (actualResponse.status)
                ApiResult.Success(actualResponse)
            else
                ApiResult.GenericError(null, actualResponse.message, null)
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
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
        }
    }

    suspend fun getUserValidateTest(): ApiResult<ValidateResponse> {
        return try {
            val response = Api.getClient()
                .getValidateTest()
            val actualResponse = response
            if (actualResponse.status)
                ApiResult.Success(actualResponse)
            else
                ApiResult.GenericError(null, actualResponse.message, null)
        } catch (e: Exception) {
            ApiResult.UnknownError(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.NetworkError
        } catch (e: UnknownHostException) {
            ApiResult.UnknownHostException(e.message)
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
        }
    }
}