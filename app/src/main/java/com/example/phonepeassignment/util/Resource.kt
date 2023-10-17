package com.example.phonepeassignment.util

sealed class Resource<T>(
    data: T? = null,
    error: String? = null
) {

    class Empty<T>() : Resource<T>()
    class Success<T>(val data: T) : Resource<T>(data = data)
    class Error<T>(val error: String?) : Resource<T>(error = error)

}