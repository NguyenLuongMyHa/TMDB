package vn.hanguyen.tmdb.ui.home

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.hanguyen.tmdb.data.movie.MovieResponse
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
        val moviesList: List<Movie>,
        val moviesListPaging: Flow<PagingData<MovieResponse>>?,
        val isSearchResult: Boolean,
        val selectedMovie: Movie,
        val selectedMovieListId: Set<Int>,
        val isInMovieDetailPage: Boolean,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        override val searchInput: String
    ) : HomeUiState
}

data class HomeViewModelState(
    val moviesList: MoviesList? = null,
    val searchMovieResultPagingData: Flow<PagingData<MovieResponse>>? = null,
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
        if (moviesList?.searchResultMovies == null && moviesList?.trendingMovies == null) {
            HomeUiState.NoMovies(
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput
            )
        } else {
            val movieList =
                if (isShowSearchResult) moviesList.searchResultMovies else moviesList.trendingMovies
            HomeUiState.HasMovies(
                moviesList = movieList,
                isSearchResult = isShowSearchResult,
                isInMovieDetailPage = isInMovieDetailPage,
                // Determine the selected movie. This will be the movie the user last selected.
                // If there is none (or that movie isn't in the current list), default to the
                // first movie in the list
                selectedMovie = movieList.find {
                    it.id == selectedMovieId
                } ?: movieList.elementAtOrNull(0)
                ?: moviesList.trendingMovies.first(),//TODO logic here
                moviesListPaging = searchMovieResultPagingData,
                selectedMovieListId = selectedMovieListId,
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput
            )
        }
}
