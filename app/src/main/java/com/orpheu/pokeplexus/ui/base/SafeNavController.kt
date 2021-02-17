package com.orpheu.pokeplexus.ui.base

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator


//Needed because jetPack navigation does not handle repeated navigation requests,
// as in a double click on a button that invokes navigation
class SafeNavController(
    private val navController: NavController,
    @IdRes private val destinationId: Int
) {
    fun navigate(@IdRes resId: Int) {
        if (navController.currentDestination?.id != destinationId) {
            Log.d(
                "SafeNavController",
                "cannot navigate, since the calling fragment id is not the same as the one currently on the backstack"
            )
            return
        }

        navController.navigate(resId)
    }

    fun navigate(@IdRes resId: Int, args: Bundle?) {
        if (navController.currentDestination?.id != destinationId) {
            Log.d(
                "SafeNavController",
                "cannot navigate, since the calling fragment id is not the same as the one currently on the backstack"
            )
            return
        }

        navController.navigate(resId, args)
    }

    fun navigate(@IdRes resId: Int, args: Bundle?, navOptions: NavOptions?) {
        if (navController.currentDestination?.id != destinationId) {
            Log.d(
                "SafeNavController",
                "cannot navigate, since the calling fragment id is not the same as the one currently on the backstack"
            )
            return
        }

        navController.navigate(resId, args, navOptions)
    }

    fun navigate(
        @IdRes resId: Int,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        if (navController.currentDestination?.id != destinationId) {
            Log.d(
                "SafeNavController",
                "cannot navigate, since the calling fragment id is not the same as the one currently on the backstack"
            )
            return
        }

        navController.navigate(resId, args, navOptions, navigatorExtras)
    }

    fun navigate(deepLink: Uri) {
        if (navController.currentDestination?.id != destinationId) {
            Log.d(
                "SafeNavController",
                "cannot navigate, since the calling fragment id is not the same as the one currently on the backstack"
            )
            return
        }

        navController.navigate(deepLink)
    }

    fun navigate(deepLink: Uri, navOptions: NavOptions?) {
        if (navController.currentDestination?.id != destinationId) {
            Log.d(
                "SafeNavController",
                "cannot navigate, since the calling fragment id is not the same as the one currently on the backstack"
            )
            return
        }

        navController.navigate(deepLink, navOptions)
    }

    fun navigate(deepLink: Uri, navOptions: NavOptions?, navigatorExtras: Navigator.Extras?) {
        if (navController.currentDestination?.id != destinationId) {
            Log.d(
                "SafeNavController",
                "cannot navigate, since the calling fragment id is not the same as the one currently on the backstack"
            )
            return
        }

        navController.navigate(deepLink, navOptions, navigatorExtras)
    }

    fun navigate(directions: NavDirections) {
        if (navController.currentDestination?.id != destinationId) {
            Log.d(
                "SafeNavController",
                "cannot navigate, since the calling fragment id is not the same as the one currently on the backstack"
            )
            return
        }

        navController.navigate(directions)
    }

    fun navigate(directions: NavDirections, navOptions: NavOptions?) {
        if (navController.currentDestination?.id != destinationId) {
            Log.d(
                "SafeNavController",
                "cannot navigate, since the calling fragment id is not the same as the one currently on the backstack"
            )
            return
        }

        navController.navigate(directions, navOptions)
    }

    fun navigate(directions: NavDirections, navigatorExtras: Navigator.Extras) {
        if (navController.currentDestination?.id != destinationId) {
            Log.d(
                "SafeNavController",
                "cannot navigate, since the calling fragment id is not the same as the one currently on the backstack"
            )
            return
        }

        navController.navigate(directions, navigatorExtras)
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun getUnsafeNavController(): NavController = navController
}