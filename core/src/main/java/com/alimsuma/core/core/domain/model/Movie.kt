package com.alimsuma.core.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val title: String?,
    val description: String?,
    val backdropPath: String?,
    val posterPath: String?,
    val releaseDate: String?,
    val voteAverage: Double?,
    val isFavorite: Boolean
) : Parcelable
