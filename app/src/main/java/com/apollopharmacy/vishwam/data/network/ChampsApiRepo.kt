package com.apollopharmacy.vishwam.data.network

import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetCategoryDetailsResponse
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetSubCategoryDetailsResponse
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.SaveUpdateRequest
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.SaveUpdateResponse
import com.apollopharmacy.vishwam.ui.home.model.*
import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

object ChampsApiRepo {

    suspend fun getStoreDetailsChamps(): ApiResult<StoreDetailsModelResponse> {
        return try {
            val response = Api.getClient().GET_STORE_DETAILS_CHAMPS()
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



    suspend fun getEmailDetailsChamps(): ApiResult<GetEmailAddressModelResponse> {
        return try {
            val response = Api.getClient().GET_EMAIL_DETAILS()
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

    suspend fun getCategoryDetailsChamps(): ApiResult<GetCategoryDetailsModelResponse> {
        return try {
            val response = Api.getClient().GET_CATEGORY_DETAILS()
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

    suspend fun getSubCategoryDetailsChamps(): ApiResult<GetSubCategoryDetailsModelResponse> {
        return try {
            val response = Api.getClient().GET_SUB_CATEGORY_DETAILS()
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

    suspend fun getTraingAndColorDetails(): ApiResult<GetTrainingAndColorDetailsModelResponse> {
        return try {
            val response = Api.getClient().GET_TRAINING_AND_COLOR_DETAILS()
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

    suspend fun getSurveyDetails(): ApiResult<GetSurveyDetailsModelResponse> {
        return try {
            val response = Api.getClient().GET_SURVEY_DETAILS_()
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

    suspend fun getSurveyDetailsByChampsId(): ApiResult<GetSurevyDetailsByChampsIdResponse> {
        return try {
            val response = Api.getClient().GET_SURVEY_DETAILS_BY_CHAMPID()
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

//------------------------------------------------------------------------------------------------------------------------------
//    suspend fun getStoreWiseDetailsChampsApi(): ApiResult<StoreDetailsModelResponse> {
//        return try {
//            val response = Api.getClient().GET_STORE_DETAILS_API(Config.ATTENDANCE_API_HEADER)
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

    suspend fun getStoreWiseDetailsChampsApi(
        url: String,
        token: String,
        storeId: String,
    ): ApiResult<GetStoreWiseEmpIdResponse> {
        return try {
            val response = Api.getClient().GET_STORE_WISE_DETAILS_CHAMPS_API(url, token, storeId)
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

    suspend fun getEmailDetailsChampsApi(
        url: String,
        token: String,
        type: String,
    ): ApiResult<GetEmailAddressModelResponse> {
        return try {
            val response = Api.getClient().GET_EMAIL_DETAILS_API(url, token, type)
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

    suspend fun getSurveyDetailsApi(
        url: String,
        token: String,
        fromDate: String,
        toDate: String,
        validatedEmpId: String,
    ): ApiResult<GetSurveyDetailsModelResponse> {
        return try {
            val response = Api.getClient().GET_SURVEY_DETAILS_API(url, token, fromDate, toDate, validatedEmpId)
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


    suspend fun getCategoryDetailsApiCall(
        baseUrl: String,
        token: String,
        ): ApiResult<GetCategoryDetailsModelResponse> {
        return try {
            val response = Api.getClient().GET_CATEGORY_DETAILS_API_CALL_(baseUrl, token)
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

    suspend fun getSubCategoryDetailsApiCall(
        baseUrl: String,
        token: String,
        categoryName: String,
    ): ApiResult<GetSubCategoryDetailsModelResponse> {
        return try {
//            val response = Api.getClient().GET_SUB_CATEGORY_DETAILS_API_CALL_JSONBLOB(baseUrl)
            val response = Api.getClient().GET_SUB_CATEGORY_DETAILS_API_CALL_(token, baseUrl, categoryName)
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

//    suspend fun getSurveyDetailsApi(startDate: String, endDate: String, id: String): ApiResult<GetSurveyDetailsModelResponse> {
//        return try {
//            val response = Api.getClient().GET_SURVEY_DETAILS_API(Config.ATTENDANCE_API_HEADER, startDate, endDate, id )
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

//    suspend fun getStoreWiseDetailsChampsApi(empId: String): ApiResult<GetStoreWiseEmpIdResponse> {
//        return try {
//            val response = Api.getClient().GET_STORE_WISE_DETAILS_CHAMPS_API(Config.ATTENDANCE_API_HEADER, empId)
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

//    suspend fun getEmailDetailsChampsApi(type: String): ApiResult<GetEmailAddressModelResponse> {
//        return try {
//            val response = Api.getClient().GET_EMAIL_DETAILS_API(Config.ATTENDANCE_API_HEADER, type)
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

    suspend fun getCategoryDetailsChampsApi(): ApiResult<GetCategoryDetailsModelResponse> {
        return try {
            val response = Api.getClient().GET_CATEGORY_DETAILS_API(Config.ATTENDANCE_API_HEADER)
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

    suspend fun getSubCategoryDetailsChampsApi(categoryName: String): ApiResult<GetSubCategoryDetailsModelResponse> {
        return try {
            val response = Api.getClient().GET_SUB_CATEGORY_DETAILS_API(Config.ATTENDANCE_API_HEADER, categoryName)
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



    suspend fun getTraingAndColorDetailsApi(
        baseUrl: String,
        token: String,
        type: String,
    ): ApiResult<GetTrainingAndColorDetailsModelResponse> {
        return try {
//            val response = Api.getClient().GET_SUB_CATEGORY_DETAILS_API_CALL_JSONBLOB(baseUrl)
            val response = Api.getClient().GET_TRAINING_AND_COLOR_DETAILS_API(baseUrl, token, type)
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
//    suspend fun getTraingAndColorDetailsApi(type: String): ApiResult<GetTrainingAndColorDetailsModelResponse> {
//        return try {
//            val response = Api.getClient().GET_TRAINING_AND_COLOR_DETAILS_API(Config.ATTENDANCE_API_HEADER, type)
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
//
//    suspend fun saveChampsApi(
//        token: String,
//        saveSurveyModelRequest: SaveSurveyModelRequest,
//    ): ApiResult<SaveSurveyModelResponse> {
//        return try {
//            val response = Api.getClient().saveChampsApi(token, saveSurveyModelRequest)
////            println(23)
////            println(response)
////            println(25)
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

    suspend fun saveChampsApi(
        baseUrl: String,
        token: String,
        saveSurveyModelRequest: SaveSurveyModelRequest,
    ): ApiResult<SaveSurveyModelResponse> {
        return try {
//            val response = Api.getClient().GET_SUB_CATEGORY_DETAILS_API_CALL_JSONBLOB(baseUrl)
            val response = Api.getClient().saveChampsApi(baseUrl, token, saveSurveyModelRequest)
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

    suspend fun getSurveyDetailsByChampsApi(
        baseUrl: String,
        token: String,
        champId: String,
    ): ApiResult<GetSurevyDetailsByChampsIdResponse> {
        return try {
//            val response = Api.getClient().GET_SUB_CATEGORY_DETAILS_API_CALL_JSONBLOB(baseUrl)
            val response = Api.getClient().GET_SURVEY_DETAILS_BY_CHAMPID(baseUrl, token, champId)
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

//    suspend fun getSurveyDetailsByChampsApi(type: String): ApiResult<GetSurevyDetailsByChampsIdResponse> {
//        return try {
//            val response = Api.getClient().GET_SURVEY_DETAILS_BY_CHAMPID_API(Config.ATTENDANCE_API_HEADER, type)
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
    suspend fun saveUpdateApi(
        baseUrl: String,
        saveUpdateRequest: SaveUpdateRequest,
    ): ApiResult<SaveUpdateResponse> {
        return try {
            val response = Api.getClient().saveUpdateApi(baseUrl, saveUpdateRequest)
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
}