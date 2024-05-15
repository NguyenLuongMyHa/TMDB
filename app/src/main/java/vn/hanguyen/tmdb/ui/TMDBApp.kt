package vn.hanguyen.tmdb.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import vn.hanguyen.tmdb.data.movie.AppContainer
import vn.hanguyen.tmdb.ui.home.HomeRoute
import vn.hanguyen.tmdb.ui.home.HomeViewModel
import vn.hanguyen.tmdb.ui.theme.TMDBTheme

@Composable
fun TMDBApp(appContainer: AppContainer, widthSizeClass: WindowSizeClass) {
    TMDBTheme {
        val isExpandedScreen = widthSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED

        val homeViewModel: HomeViewModel = viewModel(
            factory = HomeViewModel.provideFactory(
                moviesRepository = appContainer.moviesRepository,
            )
        )
        HomeRoute(
            homeViewModel = homeViewModel,
            isExpandedScreen = isExpandedScreen,
        )
    }
}