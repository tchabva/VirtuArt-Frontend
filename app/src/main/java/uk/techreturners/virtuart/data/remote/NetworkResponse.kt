package uk.techreturners.virtuart.data.remote

sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class Failed<Nothing>(
        val message: String?,
        val code: Int?
    ) : NetworkResponse<Nothing>()
    data class Exception<Nothing>(val exception: Throwable) : NetworkResponse<Nothing>()
}