package com.orpheu.pokeplexus.core


sealed class Resource<out DataType>(
    open val data: DataType?
) {
    data class Loading<out DataType>(override val data: DataType?) : Resource<DataType>(data)

    data class Success<out DataType>(override val data: DataType) : Resource<DataType>(data)

    data class Failure<out DataType>(override val data: DataType?, val error: Throwable) :
        Resource<DataType>(data)
}