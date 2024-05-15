package vn.hanguyen.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import vn.hanguyen.tmdb.ui.TMDBApp
import vn.hanguyen.tmdb.ui.theme.TMDBTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as TmdbApplication).container
        setContent {
            val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
            TMDBApp(appContainer, windowSizeClass)
        }
    }
}