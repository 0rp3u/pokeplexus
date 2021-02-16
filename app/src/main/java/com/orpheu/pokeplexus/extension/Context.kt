package com.orpheu.pokeplexus.extension

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun Context.hasActiveInternetConnection(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
}

fun Context.dpFromPx(px: Float): Float {
    return px / resources.displayMetrics.density
}

fun Context. pxFromDp(dp: Float): Float {
    return dp * resources.displayMetrics.density
}
