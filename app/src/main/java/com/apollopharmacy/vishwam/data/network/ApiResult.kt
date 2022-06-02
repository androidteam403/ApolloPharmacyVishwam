package com.apollopharmacy.vishwam.data.network

sealed class ApiResult<out T> {
    data class Success<out T>(val value: T) : ApiResult<T>()

    data class GenericError(
        val code: Int? = null,
        val error: String? = null,
        val data: String? = null,
    ) : ApiResult<Nothing>()

    object NetworkError : ApiResult<Nothing>()
    data class UnknownError(val message: String?) : ApiResult<Nothing>()
    data class UnknownHostException(val message: String?) : ApiResult<Nothing>()

    fun extractErrorString(): String {
        return when (this) {
            is Success -> "No error"
            is GenericError -> this.error ?: "Api Error"
            is NetworkError -> "Network Error"
            is UnknownError -> "Something went wrong"
            is UnknownHostException -> "Please Try again"
        }
    }
}