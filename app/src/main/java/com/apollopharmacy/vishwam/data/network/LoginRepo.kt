package com.apollopharmacy.vishwam.data.network

import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Config.ENCRIPTION_KEY
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.*
import com.apollopharmacy.vishwam.data.model.cms.StoreData
import com.apollopharmacy.vishwam.util.EncryptionManager
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

object LoginRepo {
    suspend fun checkLoginUse(
        loginApiRequest: LoginRequest,
        loginUrl: String,
    ): ApiResult<LoginDetails> {
        return try {
            Utils.printMessage("Encriptvalue", loginApiRequest.toString())
            val encriptData = EncryptionManager.encryptData(
                Gson().toJson(loginApiRequest),
                ENCRIPTION_KEY
            )
            Utils.printMessage("checkLoginUse", encriptData.toString())

            val response =
                Api.getClient().loginUser(loginUrl, CommonRequest(encriptData.toString()))
            Utils.printMessage("checkLoginUse", response.toString())
            val decriptData = EncryptionManager.decryptData(response, ENCRIPTION_KEY)
            val Responsee = Gson().fromJson(decriptData, LoginDetails::class.java)
            Utils.printMessage("LoginResponse", Responsee.toString())
            if (Responsee.STATUS)
                ApiResult.Success(Responsee)
            else
                ApiResult.GenericError(null, Responsee.MESSAGE, null)
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

    suspend fun checkMPinDetails(mPinRequest: MPinRequest): ApiResult<MPinResponse> {
        return try {
            val response =
                Api.getClient().handleMPinService(Config.ATTENDANCE_API_HEADER, mPinRequest)
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

    fun saveProfile(loginDetails: LoginDetails, password: String) {
        Preferences.saveProfile(Gson().toJson(loginDetails))
        Preferences.savePassword(password)

    }

    fun getProfile(): LoginDetails? {
        val storeString = Preferences.getProfile()
        return try {
            Gson().fromJson(storeString, LoginDetails::class.java)
        } catch (e: JsonParseException) {
            e.printStackTrace()
            return null
        }
    }

    fun getPassword(): String{
        return Preferences.getUserPassword()
    }


    fun saveStoreData(storedata: StoreData) {
        Preferences.savingStoreData(Gson().toJson(storedata))
    }

    fun getStoreData(): StoreData? {
        val storeString = Preferences.getStoreData()
        return try {
            Gson().fromJson(storeString, StoreData::class.java)
        } catch (e: JsonParseException) {
            e.printStackTrace()
            return null
        }
    }


    fun getUserID(): String {
        val storeString = LoginRepo.getProfile()
        return storeString!!.EMPID
    }

    fun signOutUser() {
        Preferences.getToken()
    }
}