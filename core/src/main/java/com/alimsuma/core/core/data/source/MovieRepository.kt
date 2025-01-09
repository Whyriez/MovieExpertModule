package com.alimsuma.core.core.data.source

import com.alimsuma.core.core.data.source.local.LocalDataSource
import com.alimsuma.core.core.data.source.remote.RemoteDataSource
import com.alimsuma.core.core.data.source.remote.network.ApiResponse
import com.alimsuma.core.core.domain.model.Movie
import com.alimsuma.core.core.domain.repository.IMovieRepository
import com.alimsuma.core.core.utils.AppExecutors
import com.alimsuma.core.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IMovieRepository {

    override fun getAllMovie(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())

        val localMovies = localDataSource.getAllMovies().map {
            DataMapper.mapEntitiesToDomain(it)
        }.first()

        if (localMovies.isEmpty()) {
            when (val apiResponse = remoteDataSource.getMoviesPopular().first()) {
                is ApiResponse.Success -> {
                    val movieList = DataMapper.mapResponsesToEntities(apiResponse.data)
                    localDataSource.insertMovie(movieList)
                    emit(Resource.Success(DataMapper.mapEntitiesToDomain(movieList)))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(emptyList()))
                }
            }
        } else {
            emit(Resource.Success(localMovies))
        }
    }

    override fun getFavoriteMovie(): Flow<List<Movie>> {
        return localDataSource.getFavoriteMovies().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setFavoriteMovie(movie: Movie, state: Boolean) {
        val movieEntity = DataMapper.mapDomainToEntity(movie)
        appExecutors.diskIO().execute {
            localDataSource.setFavoriteMovies(movieEntity, state)
        }
    }

    override fun searchMovies(query: String): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())

        val localMovies = localDataSource.searchMovies(query).map {
            DataMapper.mapEntitiesToDomain(it)
        }.first()

        if (localMovies.isEmpty()) {
            when (val apiResponse = remoteDataSource.searchMovies(query).first()) {
                is ApiResponse.Success -> {
                    val movieList = DataMapper.mapResponsesToEntities(apiResponse.data)
                    localDataSource.insertMovie(movieList)
                    emit(Resource.Success(DataMapper.mapEntitiesToDomain(movieList)))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(emptyList()))
                }
            }
        } else {
            emit(Resource.Success(localMovies))
        }
    }

    override fun searchFavoriteMovies(query: String): Flow<List<Movie>> {
        return localDataSource.searchFavoriteMovies(query).map { movieEntities ->
            DataMapper.mapEntitiesToDomain(movieEntities)
        }
    }
}
