package vn.hanguyen.tmdb.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import vn.hanguyen.tmdb.R
import vn.hanguyen.tmdb.model.Genres
import vn.hanguyen.tmdb.model.Movie
import vn.hanguyen.tmdb.model.ProductionCompany
import vn.hanguyen.tmdb.ui.theme.Shapes

@Composable
fun MovieDetailScreen(
    movie: Movie,
    isTablet: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(modifier.fillMaxSize()) {
        MovieDetailScreenContent(
            movie = movie,
            navigationIconContent = {
                if (!isTablet) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_movie_list_screen),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = movie.title ?: "",
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

@OptIn(ExperimentalGlideComposeApi::class)
fun LazyListScope.movieContentItems(movie: Movie) {
    item {
        MoviePosterHeaderImage(movie)
        Spacer(Modifier.height(16.dp))
        Text(movie.title ?: "", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))
        movie.overview?.let {
            Text(movie.overview)
            Spacer(Modifier.height(16.dp))
        }

        movie.genres?.let {
            GenresListItem(
                movie.genres
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        var releaseYear: String = try {
            movie.releaseDate.substring(0, 4)
        } catch (e: Exception) {
            "Year release is not specified"
        }
        Text(
            text = "Release Year: $releaseYear",
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Vote Average: " + movie.voteAverage + "/10",
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Vote Count: " + movie.voteCount,
        )

        movie.revenue?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Revenue: " + movie.revenue,
            )
        }

        movie.budget?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Budget: " + movie.budget,
            )
        }

        movie.belongsToCollection?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Belong to collection: ${movie.belongsToCollection.name}")
            Spacer(modifier = Modifier.height(12.dp))
            GlideImage(
                model = movie.belongsToCollection.posterPath,
                contentDescription = "Poster image for the movie's production",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(3f / 4f),
                contentScale = ContentScale.Fit,
                failure = placeholder(R.drawable.ic_launcher_foreground)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
    movie.productionCompanies?.let {
        item {
            Text(text = "Production companies:", modifier = Modifier.padding(top = 12.dp))
        }
        items(count = movie.productionCompanies.size) {
            MovieProductionCompany(movie.productionCompanies[it])
        }
    }
    movie.homepage?.let {
        item {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                val uriHandler = LocalUriHandler.current
                Text(
                    modifier = Modifier.clickable { uriHandler.openUri(it) },
                    text = it,
                    style = TextStyle().copy(textDecoration = TextDecoration.Underline, color = Color.Blue)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

}

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun GenresListItem(
    genres: List<Genres>
) {
    FlowRow {
        for (genre in genres) {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .background(Color.Gray, MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {
                genre.name?.let {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MovieProductionCompany(
    productionCompany: ProductionCompany,
    modifier: Modifier = Modifier
) {
    Row {
        GlideImage(
            model = productionCompany.logoPath,
            contentDescription = "logo image for the movie's production company",
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit,
            failure = placeholder(R.drawable.ic_launcher_foreground)
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = productionCompany.name ?: "",
            )
            Text(
                text = productionCompany.originCountry ?: "",
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MoviePosterHeaderImage(movie: Movie) {
    val imageModifier =
        Modifier
            .aspectRatio(ratio = 4f / 3f)
            .fillMaxWidth()
            .clip(shape = Shapes.small)
    GlideImage(
        model = movie.posterPath,
        contentDescription = "Poster image for the movie ${movie.title}",
        modifier = imageModifier,
        contentScale = ContentScale.FillBounds,
        failure = placeholder(R.drawable.ic_launcher_foreground)
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
