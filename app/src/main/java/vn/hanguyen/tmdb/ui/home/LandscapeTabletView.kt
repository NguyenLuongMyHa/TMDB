package vn.hanguyen.tmdb.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vn.hanguyen.tmdb.model.Movie
import vn.hanguyen.tmdb.ui.detail.movieContentItems

@Composable
fun LandscapeTabletListWithMovieDetailScreen(
    uiState: HomeUiState,
    onSelectMovieItem: (Int, Boolean) -> Unit,
    onRefreshMovies: () -> Unit,
    onInteractWithList: () -> Unit,
    onInteractWithDetail: (Int) -> Unit,
//    movieDetailLazyListStates: Map<Int, LazyListState>,
    modifier: Modifier = Modifier,
    searchInput: String = "",
    onSearchInputChanged: (String) -> Unit,
    onSearchMovie: () -> Unit,
    onAddMovieToCache: (movie: Movie) -> Unit,
) {
    HomeScreenWithList(
        uiState = uiState,
        onRefreshMovies = onRefreshMovies,
        modifier = modifier,
        searchInput = uiState.searchInput,
        onSearchInputChanged = onSearchInputChanged,
        onSearchMovie = onSearchMovie
    ) { hasMoviesUiState, contentPadding, contentModifier ->
        Row(contentModifier) {
            MovieList(
                searchMoviesListPaging = hasMoviesUiState.searchMoviesListPaging,
                trendingMoviesListPaging = hasMoviesUiState.trendingMoviesListPaging,
                selectedItems = hasMoviesUiState.selectedMovieListId,
                onSelectMovie = onSelectMovieItem,
                contentPadding = contentPadding,
                modifier = contentModifier
                    .width(334.dp)
                    .notifyInput(onInteractWithList),
                onAddMovieToCache = onAddMovieToCache,
                isSearchResult = hasMoviesUiState.isSearchResult
            )
            // Crossfade between different detail posts
            if (hasMoviesUiState.selectedMovie != null) {
                Crossfade(
                    modifier = contentModifier.padding(contentPadding),
                    targetState = hasMoviesUiState.selectedMovie
                ) { detailMovie ->
                    // Get the lazy list state for this detail view
//                val detailLazyListState by remember {
//                    derivedStateOf {
//                        movieDetailLazyListStates.getValue(detailMovie.id)
//                    }
//                }

                    // Key against the movie id to avoid sharing any state between different movies
                    key(detailMovie.id) {
                        LazyColumn(
//                        state = detailLazyListState,
                            contentPadding = contentPadding,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxSize()
                                .notifyInput {
                                    onInteractWithDetail(detailMovie.id)
                                }
                        ) {
                            movieContentItems(detailMovie)
                        }
                    }
                }
            }
        }
    }
}