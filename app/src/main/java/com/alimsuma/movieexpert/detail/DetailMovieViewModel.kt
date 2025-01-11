package com.alimsuma.movieexpert.detail

import androidx.lifecycle.ViewModel
import com.alimsuma.core.core.domain.model.Movie
import com.alimsuma.core.core.domain.usecase.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(private val movieUseCase: MovieUseCase) : ViewModel() {
    fun setFavoriteMovies(movie: Movie, newStatus: Boolean) =
        movieUseCase.setFavoriteMovie(movie, newStatus)
}
