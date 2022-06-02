package com.apollopharmacy.vishwam.data.network

import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import retrofit2.HttpException

object ApiUtils {
    fun parseHttpError(e: HttpException): ApiResult.GenericError {
        e.response()?.errorBody()?.let {
            val responseString = it.string()
            try {
                val responseJson = JsonParser.parseString(responseString).asJsonObject
                val errorMessage =
                    if (responseJson.has("message")) responseJson.get("message").asString
                    else "Something went wrong"
                return ApiResult.GenericError(e.code(), errorMessage)
            } catch (er: JsonParseException) {
                ApiResult.GenericError(
                    e.code(),
                    "Something went wrong and we are unable to parse the error"
                )

            } catch (er: Throwable) {
                ApiResult.GenericError(
                    e.code(),
                    "Something went wrong and we are unable to parse the error"
                )
            }
        } ?: return ApiResult.GenericError(e.code(), "Something went wrong: Http Error")
        return ApiResult.GenericError(e.code(), "Something went wrong: Http Error")
    }
}