package com.alimsuma.movieexpert.di

import com.alimsuma.core.core.domain.usecase.MovieUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoriteComponentDependencies {
    fun movieUseCase(): MovieUseCase
}