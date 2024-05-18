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
import vn.hanguyen.tmdb.ui.home.HomeScreenType.PortraitListMovie
import vn.hanguyen.tmdb.ui.home.HomeScreenType.PortraitTabletGridMovie
import vn.hanguyen.tmdb.ui.home.HomeScreenType.LandscapeTabletListWithMovieDetail
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
    val homeListLazyListState = rememberLazyListState()
    val homeListLazyGridState = rememberLazyGridState()

    val homeScreenType = getHomeScreenType(isExpandedScreenInWidth, isExpandedScreenInHeight, uiState)
    when (homeScreenType) {
        LandscapeTabletListWithMovieDetail -> {
            LandscapeTabletListWithMovieDetailScreen(
                uiState = uiState,
                onSelectMovieItem = onSelectMovieItem,
                onRefreshMovies = onRefreshMovies,
                onInteractWithList = onInteractWithList,
                onInteractWithDetail = onInteractWithDetail,
                homeListLazyListState = homeListLazyListState,
                onSearchInputChanged = onSearchInputChanged,
                onSearchMovie = onSearchMovie,
                onAddMovieToCache = onAddMovieToCache
            )
        }

        PortraitTabletGridMovie -> {
            PortraitTabletGridMovieScreen(
                uiState = uiState,
                onSelectMovie = onSelectMovieItem,
                onRefreshMovies = onRefreshMovies,
                homeListLazyGridState = homeListLazyGridState,
                onSearchInputChanged = onSearchInputChanged,
                onSearchMovie = onSearchMovie,
                onAddMovieToCache = onAddMovieToCache
            )
        }
        PortraitListMovie -> {
            PortraitListMovieScreen(
                uiState = uiState,
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
                isTablet = isExpandedScreenInWidth,
                onBack = onInteractWithList,
            )

            // If we are just showing the detail, have a back press switch to the list.
            BackHandler {
                onInteractWithList()
            }
        }
    }

}

