package com.alimsuma.movieexpert.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alimsuma.core.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val searchQuery = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteTourism = searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                movieUseCase.getFavoriteMovie()
            } else {
                movieUseCase.searchFavoriteMovies(query)
            }
        }
        .catch { error ->
            Log.e("FavoriteViewModel", "Error in favorite movies flow", error)
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        .asLiveData(viewModelScope.coroutineContext)

    fun setSearchQuery(query: String) {
        viewModelScope.launch {
            _searchQuery.emit(query)
        }
    }

    fun clearResources() {
        viewModelScope.launch {
            _searchQuery.emit("")
        }
    }

    override fun onCleared() {
        super.onCleared()
        clearResources()
    }

    companion object {
        fun provideFactory(movieUseCase: MovieUseCase): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FavoriteViewModel(movieUseCase) as T
                }
            }
    }
}
