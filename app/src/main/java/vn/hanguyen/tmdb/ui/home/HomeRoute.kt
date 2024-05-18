package vn.hanguyen.tmdb.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vn.hanguyen.tmdb.model.Movie
import vn.hanguyen.tmdb.ui.detail.MovieDetailScreen
import vn.hanguyen.tmdb.ui.home.HomeScreenType.ListMovie
import vn.hanguyen.tmdb.ui.home.HomeScreenType.GridMovie
import vn.hanguyen.tmdb.ui.home.HomeScreenType.ListWithMovieDetail
import vn.hanguyen.tmdb.ui.home.HomeScreenType.MovieDetail

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    isExpandedScreenInWidth: Boolean,
    isExpandedScreenInHeight: Boolean,
) {
    // UiState of the HomeScreen
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeRoute(
        uiState = uiState,
        isExpandedScreenInWidth = isExpandedScreenInWidth,
        isExpandedScreenInHeight = isExpandedScreenInHeight,
        onSelectMovieItem = { homeViewModel.selectMovie(it) },
        onRefreshMovies = { homeViewModel.getTrendingMoviesWithPaging() },
        onSearchMovie = { homeViewModel.searchMoviesWithPaging() },
        onSearchInputChanged = { homeViewModel.onSearchInputChanged(it) },
        onInteractWithList = { homeViewModel.interactedWithMovieList() },
        onInteractWithDetail = { homeViewModel.interactedWithMovieDetails(it) },
        onAddMovieToCache = { homeViewModel.addMovieToMemory(it) },
    )
}

@Composable
fun HomeRoute(
    uiState: HomeUiState,
    isExpandedScreenInWidth: Boolean,
    isExpandedScreenInHeight: Boolean,
    onSelectMovieItem: (Int) -> Unit,
    onRefreshMovies: () -> Unit,
    onSearchInputChanged: (String) -> Unit,
    onSearchMovie: () -> Unit,
    onInteractWithList: () -> Unit,
    onInteractWithDetail: (Int) -> Unit,
    onAddMovieToCache: (movie: Movie) -> Unit,
    ) {
    // Construct the lazy list states for the list and the details outside of deciding which one to
    // show. This allows the associated state to survive beyond that decision, and therefore
    // we get to preserve the scroll throughout any changes to the content.
    val homeListLazyListState = rememberLazyListState()
    val homeListLazyGridState = rememberLazyGridState()
//    val movieDetailLazyListStates = when (uiState) {
//        is HomeUiState.HasMovies -> uiState.moviesList
//        is HomeUiState.NoMovies -> emptyList()
//    }.associate { movie ->
//        key(movie.id) {
//            movie.id to rememberLazyListState()
//        }
//    }

    val homeScreenType = getHomeScreenType(isExpandedScreenInWidth, isExpandedScreenInHeight, uiState)
    when (homeScreenType) {
        ListWithMovieDetail -> {
            HomeListWithMovieDetailsScreen(
                uiState = uiState,
                showTopAppBar = !isExpandedScreenInWidth,
                onSelectMovieItem = onSelectMovieItem,
                onRefreshMovies = onRefreshMovies,
                onInteractWithList = onInteractWithList,
                onInteractWithDetail = onInteractWithDetail,
                homeListLazyListState = homeListLazyListState,
//                movieDetailLazyListStates = movieDetailLazyListStates,
                onSearchInputChanged = onSearchInputChanged,
                onSearchMovie = onSearchMovie,
                onAddMovieToCache = onAddMovieToCache
            )
        }

        GridMovie -> {
            HomeGridScreen(
                uiState = uiState,
                showTopAppBar = !isExpandedScreenInWidth,
                onSelectMovie = onSelectMovieItem,
                onRefreshMovies = onRefreshMovies,
                homeListLazyGridState = homeListLazyGridState,
                onSearchInputChanged = onSearchInputChanged,
                onSearchMovie = onSearchMovie,
                onAddMovieToCache = onAddMovieToCache
            )
        }
        ListMovie -> {
            HomeScreen(
                uiState = uiState,
                showTopAppBar = !isExpandedScreenInWidth,
                onSelectMovie = onSelectMovieItem,
                onRefreshMovies = onRefreshMovies,
                homeListLazyListState = homeListLazyListState,
                onSearchInputChanged = onSearchInputChanged,
                onSearchMovie = onSearchMovie,
                onAddMovieToCache = onAddMovieToCache
            )
        }

        MovieDetail -> {
            if(uiState is HomeUiState.HasMovies && uiState.selectedMovie!= null)
            MovieDetailScreen(
                movie = uiState.selectedMovie,
                isExpandedScreen = isExpandedScreenInWidth,
                onBack = onInteractWithList,
//                lazyListState = movieDetailLazyListStates.getValue(
//                    uiState.selectedMovie.id
//                )
            )

            // If we are just showing the detail, have a back press switch to the list.
            BackHandler {
                onInteractWithList()
            }
        }
    }

}


/**
 * Decide which type of screen to display at the home route.
 *
 * There are 3 options:
 * - [ListWithMovieDetail], which displays both a list of all movies and a specific movie.
 * - [ListMovie], which displays just the list of all movies
 * - [MovieDetail], which displays just a specific movie.
 */
private enum class HomeScreenType {
    ListWithMovieDetail,
    ListMovie,
    GridMovie,
    MovieDetail
}

@Composable
private fun getHomeScreenType(
    isExpandedScreenInWidth: Boolean,
    isExpandedScreenInHeight: Boolean,
    uiState: HomeUiState
): HomeScreenType {
    when (isExpandedScreenInWidth) {
        false -> {
            when (isExpandedScreenInHeight) {
                false -> {
                    return when (uiState) {
                        is HomeUiState.HasMovies -> {
                            if (uiState.isInMovieDetailPage) {
                                MovieDetail
                            } else {
                                ListMovie
                            }
                        }

                        is HomeUiState.NoMovies -> ListMovie
                    }
                }
                true -> return when (uiState) {
                    is HomeUiState.HasMovies -> {
                        if (uiState.isInMovieDetailPage) {
                            MovieDetail
                        } else {
                            GridMovie
                        }
                    }

                    is HomeUiState.NoMovies -> GridMovie
                }
            }
        }
        true -> return ListWithMovieDetail
    }
}

