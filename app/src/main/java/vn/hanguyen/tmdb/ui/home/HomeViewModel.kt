package vn.hanguyen.tmdb.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vn.hanguyen.tmdb.R
import vn.hanguyen.tmdb.data.MoviesRepositoryImpl
import vn.hanguyen.tmdb.model.Movie
import vn.hanguyen.tmdb.model.MoviesList
import vn.hanguyen.tmdb.util.ErrorMessage
import vn.hanguyen.tmdb.util.Result
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepositoryImpl,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var preSelectedMovieId: Int? = savedStateHandle["preSelectedMovieId"]

    private val listDetailMovieCached = mutableListOf<Movie>()

    private val viewModelState = MutableStateFlow(
        HomeViewModelState(
            isLoading = true,
            selectedMovieId = preSelectedMovieId,
            isInMovieDetailPage = preSelectedMovieId != null
        )
    )

    // UI state exposed to the UI
    val uiState = viewModelState
        .map(HomeViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        refreshTrendingMovies()

        viewModelScope.launch {
            moviesRepository.observeSelectedMovies().collect { selectedItems ->
                viewModelState.update { it.copy(selectedMovieListId = selectedItems) }
            }
        }
    }

    /**
     * Search movies and update the UI state accordingly
     */
    fun searchMoviesWithPaging() {
        viewModelState.update { it.copy(isLoading = true) }

        val searchKey = viewModelState.value.searchInput
        viewModelScope.launch {
            try {
                val resultPagingData =
                    moviesRepository.getSearchResultStream(searchKey).cachedIn(viewModelScope)
                        .first()

                viewModelState.update {
                    it.copy(
                        isShowSearchResult = true,
                        searchMovieResultPagingData = flowOf(resultPagingData),
                        isLoading = false,
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val errorMessages = ErrorMessage(
                    id = Random.nextLong(),
                    messageId = R.string.search_error
                )
                viewModelState.update {
                    it.copy(
                        isShowSearchResult = true,
                        errorMessages = listOf(errorMessages),
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Refresh movie and update the UI state accordingly
     */
    fun refreshTrendingMovies() {
        // Ui state is refreshing
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = moviesRepository.getTrendingMovies()
            viewModelState.update {
                when (result) {
                    is Result.Success -> it.copy(
                        searchInput = "",
                        isShowSearchResult = false,
                        moviesList = MoviesList(
                            trendingMovies = result.data.toMutableList(),
                            searchResultMovies = mutableListOf()
                        ), isLoading = false
                    )

                    is Result.Error -> {
                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = Random.nextLong(),
                            messageId = R.string.load_error
                        )
                        it.copy(
                            searchInput = "",
                            isShowSearchResult = false,
                            errorMessages = errorMessages, isLoading = false
                        )
                    }
                }
            }
        }
    }


    /**
     * Selects the movie to view more information detail about it.
     */
    fun selectMovie(movieId: Int) {
        interactedWithMovieDetails(movieId)
        viewModelScope.launch {
            moviesRepository.selectMovie(movieId)
        }
        if(listDetailMovieCached.any { it.id == movieId })
        {
            viewModelState.update {
                it.copy(
                    selectedMovieId = movieId,
                    isInMovieDetailPage = true,
                    selectedMovieListId = it.selectedMovieListId.plus(movieId),
                    isLoading = false
                )
            }
        }
        else {
            //fetch more detail data if movie detail was not cached in memory
            viewModelScope.launch {
                val result = moviesRepository.getMovie(movieId)
                if(result is Result.Success) listDetailMovieCached.plusAssign(result.data)
                viewModelState.update {
                    when (result) {
                        is Result.Success -> it.copy(
                            moviesList = it.moviesList?.updateMovieDetail(result.data),
                            selectedMovieId = movieId,
                            isInMovieDetailPage = true,
                            selectedMovieListId = it.selectedMovieListId.plus(movieId),
                            isLoading = false
                        )

                        is Result.Error -> {
                            val errorMessages = it.errorMessages + ErrorMessage(
                                id = Random.nextLong(),
                                messageId = R.string.load_error
                            )
                            it.copy(
                                searchInput = "",
                                isShowSearchResult = false,
                                errorMessages = errorMessages, isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Notify that the user interacted with the list
     */
    fun interactedWithMovieList() {
        viewModelState.update {
            it.copy(isInMovieDetailPage = false)
        }
    }

    /**
     * Notify that the user interacted with the movie details
     */
    fun interactedWithMovieDetails(movieId: Int) {
        viewModelState.update {
            it.copy(
                selectedMovieId = movieId,
                isInMovieDetailPage = true,
                selectedMovieListId = it.selectedMovieListId.plus(movieId)
            )
        }
    }

    /**
     * Notify that the user updated the search query
     */
    fun onSearchInputChanged(searchInput: String) {
        viewModelState.update {
            it.copy(searchInput = searchInput)
        }
    }

    fun addMovieToSearchMemory(movie: Movie) {
        viewModelState.update {
            it.copy(
                moviesList = MoviesList(
                    searchResultMovies = it.moviesList?.searchResultMovies?.plus(
                        movie
                    )?.toMutableList() ?: mutableListOf(movie),
                    trendingMovies = it.moviesList?.trendingMovies?.toMutableList() ?: mutableListOf()
                )
            )
        }
    }
}
