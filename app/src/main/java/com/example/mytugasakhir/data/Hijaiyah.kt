package com.example.mytugasakhir.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hijaiyah(
    val index: Int,
    val name: String,
    val photo: Int,
    val text: String,
    val audio: Int
) : Parcelable
