package com.orpheu.pokeplexus.ui.base

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation

abstract class NavDestinationFragment(
    @IdRes destinationId: Int,
) : Fragment() {

    private val _navController: NavController by lazy { requireView().let { Navigation.findNavController(it) } }

    val navController: SafeNavController by lazy {
        SafeNavController(_navController, destinationId)
    }
}