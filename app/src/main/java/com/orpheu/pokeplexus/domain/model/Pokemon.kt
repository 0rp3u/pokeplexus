package com.orpheu.pokeplexus.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val favorite: Boolean,
): Parcelable
