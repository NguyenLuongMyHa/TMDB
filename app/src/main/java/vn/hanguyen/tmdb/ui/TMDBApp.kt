package vn.hanguyen.tmdb.ui

import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import vn.hanguyen.tmdb.ui.home.HomeRoute
import vn.hanguyen.tmdb.ui.theme.TMDBTheme

@Composable
fun TMDBApp(widthSizeClass: WindowSizeClass) {
    TMDBTheme {
        val isExpandedScreen = widthSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED

        HomeRoute(
            isExpandedScreen = isExpandedScreen,
        )
    }
}