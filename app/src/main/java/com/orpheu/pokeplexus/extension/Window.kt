package com.orpheu.pokeplexus.extension

import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat


fun Window.changeStatusBarColor(@ColorInt color: Int){
    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    statusBarColor = color
}