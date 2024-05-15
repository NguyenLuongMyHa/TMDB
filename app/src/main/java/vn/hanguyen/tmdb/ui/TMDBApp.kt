package vn.hanguyen.tmdb.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.coroutines.launch
import vn.hanguyen.tmdb.ui.theme.TMDBTheme

@Composable
fun TMDBApp (widthSizeClass: WindowSizeClass) {
    TMDBTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            TmdbNavigationActions(navController)
        }

        val coroutineScope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =
            navBackStackEntry?.destination?.route ?: TmdbDestinations.HOME_ROUTE

        val isExpandedScreen = widthSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED

        TmdbNavGraph(
            isExpandedScreen = isExpandedScreen,
            navController = navController,
        )
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
    }
}