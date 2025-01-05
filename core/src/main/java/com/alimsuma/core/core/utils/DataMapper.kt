package com.alimsuma.core.core.utils

import com.alimsuma.core.core.data.source.local.entity.MovieEntity
import com.alimsuma.core.core.data.source.remote.response.MovieResponse
import com.alimsuma.core.core.domain.model.Movie

object DataMapper {

    fun mapResponsesToEntities(input: List<MovieResponse>): List<MovieEntity> {
        return input.map { response ->
            MovieEntity(
                id = response.id,
                adult = response.adult,
                backdropPath = response.backdropPath,
                originalLanguage = response.originalLanguage,
                originalTitle = response.originalTitle,
                overview = response.overview,
                popularity = response.popularity,
                posterPath = response.posterPath,
                releaseDate = response.releaseDate,
                title = response.title,
                video = response.video,
                voteAverage = response.voteAverage,
                voteCount = response.voteCount,
                isFavorite = false
            )
        }
    }

    fun mapEntitiesToDomain(input: List<MovieEntity>): List<Movie> =
        input.map { entity ->
            Movie(
                id = entity.id,
                title = entity.title,
                description = entity.overview,
                backdropPath = entity.backdropPath,
                posterPath = entity.posterPath,
                releaseDate = entity.releaseDate,
                voteAverage = entity.voteAverage,
                isFavorite = entity.isFavorite
            )
        }


    fun mapDomainToEntity(input: Movie) = MovieEntity(
        id = input.id,
        adult = false,
        backdropPath = "",
        originalLanguage = "",
        originalTitle = "",
        overview = input.description,
        popularity = 0.0,
        posterPath = input.posterPath,
        releaseDate = input.releaseDate,
        title = input.title,
        video = false,
        voteAverage = input.voteAverage,
        voteCount = 0,
        isFavorite = input.isFavorite
    )
}