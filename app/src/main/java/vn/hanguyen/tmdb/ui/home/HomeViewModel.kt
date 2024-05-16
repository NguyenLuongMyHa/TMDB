package vn.hanguyen.tmdb.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vn.hanguyen.tmdb.R
import vn.hanguyen.tmdb.data.movie.MoviesRepositoryImpl
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

    private var preSelectedMovieId: Long? = savedStateHandle["preSelectedMovieId"]

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
                viewModelState.update { it.copy(selectedItems = selectedItems) }
            }
        }
    }

    /**
     * Search movies and update the UI state accordingly
     */
    fun searchMovies() {
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = moviesRepository.searchMovies(viewModelState.value.searchInput)
            viewModelState.update {
                when (result) {
                    is Result.Success -> it.copy(
                        moviesList = it.moviesList?.copy(searchResultMovies = result.data)
                        , isLoading = false
                    )

                    is Result.Error -> {
                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = Random.nextLong(),
                            messageId = R.string.search_error
                        )
                        it.copy(errorMessages = errorMessages, isLoading = false)
                    }
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
                        moviesList = MoviesList(
                            trendingMovies = result.data,
                            searchResultMovies = emptyList()
                        ), isLoading = false
                    )

                    is Result.Error -> {
                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = Random.nextLong(),
                            messageId = R.string.load_error
                        )
                        it.copy(errorMessages = errorMessages, isLoading = false)
                    }
                }
            }
        }
    }


    /**
     * Selects the movie to view more information detail about it.
     */
    fun selectMovie(movieId: Long) {
        interactedWithMovieDetails(movieId)
        viewModelScope.launch {
            moviesRepository.selectMovie(movieId)
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
    fun interactedWithMovieDetails(movieId: Long) {
        viewModelState.update {
            it.copy(
                selectedMovieId = movieId,
                isInMovieDetailPage = true,
                selectedItems = it.selectedItems.plus(movieId)
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
}
