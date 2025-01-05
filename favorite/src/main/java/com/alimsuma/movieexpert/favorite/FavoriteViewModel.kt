package com.alimsuma.movieexpert.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.alimsuma.core.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class FavoriteViewModel(movieUseCase: MovieUseCase) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val searchQuery = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteTourism = searchQuery.flatMapLatest { query ->
        if (query.isEmpty()) {
            movieUseCase.getFavoriteMovie()
        } else {
            movieUseCase.searchFavoriteMovies(query)
        }
    }.asLiveData()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    companion object {
        fun provideFactory(
            movieUseCase: MovieUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FavoriteViewModel(movieUseCase) as T
            }
        }
    }
}
