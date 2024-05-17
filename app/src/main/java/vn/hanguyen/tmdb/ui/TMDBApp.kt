package vn.hanguyen.tmdb.ui

import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import vn.hanguyen.tmdb.ui.home.HomeRoute
import vn.hanguyen.tmdb.ui.theme.TMDBTheme

@Composable
fun TMDBApp(windowSizeClass: WindowSizeClass) {
    TMDBTheme {
        val isExpandedScreenInWidth = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED
        val isExpandedScreenInHeight = windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.EXPANDED

        HomeRoute(
            isExpandedScreenInWidth = isExpandedScreenInWidth,
            isExpandedScreenInHeight = isExpandedScreenInHeight,
        )
    }
}