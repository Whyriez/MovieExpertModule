package com.alimsuma.movieexpert.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alimsuma.core.core.domain.usecase.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieUseCase: MovieUseCase) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val searchQuery = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val movies = searchQuery.flatMapLatest { query ->
        if (query.isEmpty()) {
            movieUseCase.getAllMovie()
        } else {
            movieUseCase.searchMovies(query)
        }
    }.asLiveData()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}