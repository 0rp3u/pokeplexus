package com.orpheu.pokeplexus.extension

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Flow<T>.launchWhenStartedIn(scope: LifecycleCoroutineScope): Job = scope.launchWhenStarted {
    collect() // tail-call
}