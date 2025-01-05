package com.alimsuma.core.core.domain.usecase

import com.alimsuma.core.core.domain.model.Movie
import com.alimsuma.core.core.domain.repository.IMovieRepository
import javax.inject.Inject

class MovieInteractor @Inject constructor(private val movieRepository: IMovieRepository): MovieUseCase {
    override fun getAllMovie() = movieRepository.getAllMovie()

    override fun getFavoriteMovie() = movieRepository.getFavoriteMovie()

    override fun searchMovies(query: String) = movieRepository.searchMovies(query)

    override fun setFavoriteMovie(movie: Movie, state: Boolean) = movieRepository.setFavoriteMovie(movie, state)

    override fun searchFavoriteMovies(query: String) = movieRepository.searchFavoriteMovies(query)
}