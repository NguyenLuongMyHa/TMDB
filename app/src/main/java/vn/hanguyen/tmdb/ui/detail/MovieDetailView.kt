package vn.hanguyen.tmdb.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import vn.hanguyen.tmdb.R
import vn.hanguyen.tmdb.model.Movie
import vn.hanguyen.tmdb.model.MovieProduction
import vn.hanguyen.tmdb.ui.theme.Shapes

@Composable
fun MovieDetailScreen(
    movie: Movie,
    isExpandedScreen: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    var showUnimplementedActionDialog by rememberSaveable { mutableStateOf(false) }
    if (showUnimplementedActionDialog) {
        FunctionalityNotAvailablePopup { showUnimplementedActionDialog = false }
    }

    Row(modifier.fillMaxSize()) {
        val context = LocalContext.current
        MovieDetailScreenContent(
            movie = movie,
            // Allow opening the Drawer if the screen is not expanded
            navigationIconContent = {
                if (!isExpandedScreen) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_movie_list_screen),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            lazyListState = lazyListState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailScreenContent(
    movie: Movie,
    navigationIconContent: @Composable () -> Unit = { },
    lazyListState: LazyListState = rememberLazyListState()
) {
    val topAppBarState = rememberTopAppBarState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = movie.title,
                navigationIconContent = navigationIconContent,
            )
        },
    ) { innerPadding ->
        MovieContent(
            movie = movie,
            contentPadding = innerPadding,
            state = lazyListState,
        )
    }
}


@Composable
fun MovieContent(
    movie: Movie,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier.padding(horizontal = 16.dp),
        state = state,
    ) {
        movieContentItems(movie)
    }
}

fun LazyListScope.movieContentItems(movie: Movie) {
    item {
        MoviePosterHeaderImage(movie)
        Spacer(Modifier.height(16.dp))
        Text(movie.title, style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))
        if (movie.overview != null) {
            Text(movie.overview, style = MaterialTheme.typography.bodyMedium)
            Spacer(androidx.compose.ui.Modifier.height(16.dp))
        }
    }
    item { MovieProduction(movie.production, Modifier.padding(bottom = 24.dp)) }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MovieProduction(
    production: MovieProduction,
    modifier: Modifier = Modifier
) {
    Row {
        GlideImage(
            model = production.imageUrl,
            contentDescription = "Poster image for the movie's production",
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = production.name,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = production.country,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MoviePosterHeaderImage(movie: Movie) {
    val imageModifier = Modifier
        .heightIn(max = 180.dp)
        .fillMaxWidth()
        .clip(shape = Shapes.small)
    val imageModifier2 =
        Modifier
            .aspectRatio(ratio = 4f / 3f)
            .fillMaxWidth()
            .clip(shape = Shapes.small)
    GlideImage(
        model = movie.posterUrl,
        contentDescription = "Poster image for the movie ${movie.title}",
        modifier = imageModifier2,
        contentScale = ContentScale.FillBounds
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    title: String,
    navigationIconContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        },
        navigationIcon = navigationIconContent,
        modifier = modifier
    )
}

@Composable
private fun FunctionalityNotAvailablePopup(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = stringResource(id = R.string.article_functionality_not_available),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.close))
            }
        }
    )
}