package com.alimsuma.core.utils

import com.alimsuma.core.core.domain.model.Movie

object DummyData {
    fun generateMovies(): List<Movie> {
        val movieList = ArrayList<Movie>()
        for (i in 0 until 10) {
            val movie = Movie(
                id = i,
                title = "Name $i",
                description = "Lorem Ipsum $i",
                backdropPath = "https://image.tmdb.org/t/p/w500/ajAxe7i34S42cY5N7ju70CjVYDK.jpg",
                posterPath = "https://image.tmdb.org/t/p/w500/ajAxe7i34S42cY5N7ju70CjVYDK.jpg",
                releaseDate = "2022-01-08T06:34:18.598Z",
                voteAverage = 8.0,
                isFavorite = false
            )
            movieList.add(movie)
        }

        return movieList
    }
}
