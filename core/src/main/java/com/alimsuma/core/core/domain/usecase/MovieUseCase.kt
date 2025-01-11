package com.alimsuma.core.core.domain.usecase

import com.alimsuma.core.core.data.source.Resource
import com.alimsuma.core.core.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
    fun getAllMovie(): Flow<Resource<List<Movie>>>
    fun searchMovies(query: String): Flow<Resource<List<Movie>>>
    fun getFavoriteMovie(): Flow<List<Movie>>
    fun setFavoriteMovie(movie: Movie, state: Boolean)
    fun searchFavoriteMovies(query: String): Flow<List<Movie>>
}
