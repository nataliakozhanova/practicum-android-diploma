package ru.practicum.android.diploma.common.domain

//sealed class Resource<T>(val data: T? = null, val message: String? = null) {
sealed class Resource<T>(val data: T? = null, val error: ErrorType = Success()) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(error: ErrorType) : Resource<T>(null, error)
    //class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}
