package com.orpheu.pokeplexus.extension

import com.orpheu.pokeplexus.core.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

fun <T> Flow<Resource<T>>.filterResultFailure(): Flow<Resource.Failure<T>> =
    mapNotNull { if (it is Resource.Failure) it else null }
