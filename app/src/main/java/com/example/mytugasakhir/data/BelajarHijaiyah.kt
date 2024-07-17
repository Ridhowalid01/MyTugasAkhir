package com.example.mytugasakhir.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BelajarHijaiyah(
    val name: String,
    val main_photo: Int,
    val desc1: String,
    val desc2: String,
    val desc3: String,
    val desc4: String,
    val desc5: String,
    val desc6: String,
    val photo1: Int,
    val photo2: Int,
    val photo3: Int,
    val photo4: Int,
    val photo5: Int,
    val photo6: Int,
    val male_audio1: Int,
    val male_audio2: Int,
    val male_audio3: Int,
    val male_audio4: Int,
    val male_audio5: Int,
    val male_audio6: Int,
    val female_audio1: Int,
    val female_audio2: Int,
    val female_audio3: Int,
    val female_audio4: Int,
    val female_audio5: Int,
    val female_audio6: Int,
) : Parcelable