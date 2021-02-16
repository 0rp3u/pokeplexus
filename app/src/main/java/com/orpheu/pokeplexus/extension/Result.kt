package com.orpheu.pokeplexus.extension

import com.orpheu.pokeplexus.core.Resource


fun <T> Result<T>.toResource(): Resource<T> = fold(
    { Resource.Success(it) },
    { Resource.Failure(null, it) }
)