package com.apollopharmacy.vishwam.data.network.discount

import com.apollopharmacy.vishwam.data.network.Api
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApiUtils
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.RatingModelRequest
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.RatingModelResponse
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

object PreviewLastImageRepo {


    suspend fun submitRatingBar(
        ratingModelRequest: RatingModelRequest?
    ): ApiResult<RatingModelResponse> {
        return try {
            val response =
                Api.getClient().RATING_BAR_API(
                    "h72genrSSNFivOi/cfiX3A==",
                    ratingModelRequest
                )
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