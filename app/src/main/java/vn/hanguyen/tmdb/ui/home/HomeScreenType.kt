package vn.hanguyen.tmdb.ui.home

import androidx.compose.runtime.Composable


/**
 * Decide which type of screen to display at the home route.
 *
 * There are 3 options:
 * - [LandscapeTabletListWithMovieDetail], which displays both a list of all movies and a specific movie.
 * - [PortraitListMovie], which displays just the list of all movies in vertical
 * - [MovieDetail], which displays just a specific movie.
 * - [PortraitTabletGridMovie], which displays just the list of all movies in grid
 */
enum class HomeScreenType {
    LandscapeTabletListWithMovieDetail,
    PortraitListMovie,
    PortraitTabletGridMovie,
    MovieDetail
}

@Composable
fun getHomeScreenType(
    isExpandedScreenInWidth: Boolean,
    isExpandedScreenInHeight: Boolean,
    uiState: HomeUiState
): HomeScreenType {
    when (isExpandedScreenInWidth) {
        //tablet landscape
        true -> return HomeScreenType.LandscapeTabletListWithMovieDetail
        false -> {
            when (isExpandedScreenInHeight) {
                //tablet portrait
                true -> return when (uiState) {
                    is HomeUiState.HasMovies -> {
                        if (uiState.isInMovieDetailPage) {
                            HomeScreenType.MovieDetail
                        } else {
                            HomeScreenType.PortraitTabletGridMovie
                        }
                    }
                    is HomeUiState.NoMovies -> HomeScreenType.PortraitTabletGridMovie
                }
                false -> {
                    //phone
                    return when (uiState) {
                        is HomeUiState.HasMovies -> {
                            if (uiState.isInMovieDetailPage) {
                                HomeScreenType.MovieDetail
                            } else {
                                HomeScreenType.PortraitListMovie
                            }
                        }

                        is HomeUiState.NoMovies -> HomeScreenType.PortraitListMovie
                    }
                }

            }
        }
    }
}

