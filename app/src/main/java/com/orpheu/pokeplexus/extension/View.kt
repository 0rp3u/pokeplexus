package com.orpheu.pokeplexus.extension

import android.view.View

var View.toVisibility: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }