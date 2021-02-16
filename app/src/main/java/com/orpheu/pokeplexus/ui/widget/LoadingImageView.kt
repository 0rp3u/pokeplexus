package com.orpheu.pokeplexus.ui.widget


import android.content.Context
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView


/*
    Image view start when using an animated vector as a src
    starts itś animation according to view'sVisibility
 */
class LoadingImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)

        if(visibility == View.VISIBLE){
            (drawable as? Animatable)?.start()
        }else
            (drawable as? Animatable)?.stop()
    }
}