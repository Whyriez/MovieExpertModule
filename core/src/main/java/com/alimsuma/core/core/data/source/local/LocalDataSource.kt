package com.alimsuma.core.core.data.source.local

import com.alimsuma.core.core.data.source.local.entity.MovieEntity
import com.alimsuma.core.core.data.source.local.room.MovieDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val movieDao: MovieDao) {

    fun getAllTourism(): Flow<List<MovieEntity>> = movieDao.getAllMovie()

    fun getFavoriteTourism(): Flow<List<MovieEntity>> = movieDao.getFavoriteMovie()

    fun searchMovies(query: String): Flow<List<MovieEntity>> = movieDao.searchMovies(query)

    suspend fun insertMovie(movieList: List<MovieEntity>) = movieDao.insertMovie(movieList)

    fun setFavoriteTourism(movie: MovieEntity, newState: Boolean) {
        movie.isFavorite = newState
        movieDao.updateFavoriteMovie(movie)
    }

    fun searchFavoriteMovies(query: String): Flow<List<MovieEntity>> = movieDao.searchFavoriteMovies(query)
}