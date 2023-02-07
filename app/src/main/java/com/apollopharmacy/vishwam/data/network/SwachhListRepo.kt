package com.apollopharmacy.vishwam.data.network

import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.model.GetpendingAndApprovedListRequest
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.model.GetpendingAndApprovedListResponse
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

object SwachhListRepo {

    suspend fun getPendingAndApprovedListRepo(
        url: String, token: String,
        getpendingAndApprovedListRequest: GetpendingAndApprovedListRequest?,
    ): ApiResult<GetpendingAndApprovedListResponse> {
        return try {
            val response = Api.getClient().GET_PENDING_ANDAPPROVED_LIST_API_CALL(url, token,

                getpendingAndApprovedListRequest)
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
}