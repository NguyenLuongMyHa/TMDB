package vn.hanguyen.tmdb.ui.home

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.hanguyen.tmdb.model.Movie
import vn.hanguyen.tmdb.model.MoviesList
import vn.hanguyen.tmdb.util.ErrorMessage


sealed interface HomeUiState {

    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>
    val searchInput: String

    /**
     * No movies to render.
     *
     * Reason: in Loading or failed to load
     */
    data class NoMovies(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        override val searchInput: String
    ) : HomeUiState

    /**
     * There are movies to render
     */
    data class HasMovies(
        val searchMoviesListPaging: Flow<PagingData<Movie>>?,
        val trendingMoviesListPaging: Flow<PagingData<Movie>>?,
        val isSearchResult: Boolean,
        val selectedMovie: Movie?,
        val selectedMovieListId: Set<Int>,
        val isInMovieDetailPage: Boolean,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        override val searchInput: String
    ) : HomeUiState
}

data class HomeViewModelState(
    val moviesList: MoviesList,
    val searchMovieResultPagingData: Flow<PagingData<Movie>>? = null,
    val trendingMovieResultPagingData: Flow<PagingData<Movie>>? = null,
    val isShowSearchResult: Boolean = false,
    val isInMovieDetailPage: Boolean = false,
    val selectedMovieId: Int? = null,
    val selectedMovieListId: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val searchInput: String = "",
) {

    /**
     * Convert [HomeViewModelState] into [HomeUiState] for the ui.
     */
    fun toUiState(): HomeUiState =
        if (moviesList.movies.isEmpty() && searchMovieResultPagingData == null && trendingMovieResultPagingData == null) {
            HomeUiState.NoMovies(
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput
            )
        } else {
            val movieList = moviesList.movies
            HomeUiState.HasMovies(
                isSearchResult = isShowSearchResult,
                isInMovieDetailPage = isInMovieDetailPage,
                // Determine the selected movie. This will be the movie the user last selected.
                // If there is none (or that movie isn't in the current list), default to the
                // first movie in the list
                selectedMovie = movieList.find {
                    it.id == selectedMovieId
                } ?: movieList.elementAtOrNull(0),//TODO make sure always have a selected movie here
                searchMoviesListPaging = searchMovieResultPagingData,
                trendingMoviesListPaging = trendingMovieResultPagingData,
                selectedMovieListId = selectedMovieListId,
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput
            )
        }
}
