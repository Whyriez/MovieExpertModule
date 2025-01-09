package com.alimsuma.core.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alimsuma.core.MainDispatcherRule
import com.alimsuma.core.core.data.source.Resource
import com.alimsuma.core.core.domain.model.Movie
import com.alimsuma.core.core.domain.usecase.MovieUseCase
import com.alimsuma.core.utils.DummyData
import com.alimsuma.core.utils.getOrAwaitValue
import com.alimsuma.movieexpert.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class HomeViewModelTest() {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var movieUseCase: MovieUseCase
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        movieUseCase = mock(MovieUseCase::class.java)
        homeViewModel = HomeViewModel(movieUseCase)
    }

    @Test
    fun `test movies flow emits all movies when query is empty`() = runTest {
        val movieList = DummyData.generateMovies()
        val resource = Resource.Success(movieList)

        `when`(movieUseCase.getAllMovie()).thenReturn(flowOf(resource))
        homeViewModel.setSearchQuery("")

        val result = homeViewModel.movies.getOrAwaitValue()
        assertTrue(result is Resource.Success<*>)
        assertEquals(movieList, (result as Resource.Success<*>).data)
    }

    @Test
    fun `test movies flow emits search result when query is provided`() = runTest {
        val movieList = DummyData.generateMovies().filter { it.title?.contains("Name 1") == true }
        val resource = Resource.Success(movieList)

        `when`(movieUseCase.searchMovies("Name 1")).thenReturn(flowOf(resource))
        homeViewModel.setSearchQuery("Name 1")

        val result = homeViewModel.movies.getOrAwaitValue()
        assertTrue(result is Resource.Success<*>)
        assertEquals(movieList, (result as Resource.Success<*>).data)
    }

    @Test
    fun `test movies flow emits loading state when query is empty`() = runTest {
        val loadingResource = Resource.Loading<List<Movie>>()
        val observer: Observer<Resource<List<Movie>>> = mock()

        `when`(movieUseCase.getAllMovie()).thenReturn(flowOf(loadingResource))
        homeViewModel.movies.observeForever(observer)

        homeViewModel.setSearchQuery("")
        verify(observer).onChanged(loadingResource)
    }

    @Test
    fun `test movies flow emits error state when search fails`() = runTest {
        val errorResource = Resource.Error<List<Movie>>("An error occurred")
        `when`(movieUseCase.searchMovies("query")).thenReturn(flowOf(errorResource))
        homeViewModel.setSearchQuery("query")
        val result = homeViewModel.movies.getOrAwaitValue()
        assertTrue(result is Resource.Error)
        assertEquals("An error occurred", (result as Resource.Error).message)
    }
}