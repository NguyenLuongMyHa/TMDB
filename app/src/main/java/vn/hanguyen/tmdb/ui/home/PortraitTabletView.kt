package vn.hanguyen.tmdb.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import kotlinx.coroutines.flow.Flow
import vn.hanguyen.tmdb.R
import vn.hanguyen.tmdb.model.Movie
import vn.hanguyen.tmdb.ui.theme.Shapes
import vn.hanguyen.tmdb.ui.theme.Typography

/**
 * The home screen displaying the movie list in tablet vertical.
 */
@Composable
fun PortraitTabletGridMovieScreen(
    uiState: HomeUiState,
    onSelectMovie: (Int, Boolean) -> Unit,
    onRefreshMovies: () -> Unit,
    homeListLazyGridState: LazyGridState,
    modifier: Modifier = Modifier,
    searchInput: String = "",
    onSearchInputChanged: (String) -> Unit,
    onSearchMovie: () -> Unit,
    onAddMovieToCache: (movie: Movie) -> Unit,
) {
    HomeScreenWithList(
        uiState = uiState,
        onRefreshMovies = onRefreshMovies,
        modifier = modifier,
        searchInput = uiState.searchInput,
        onSearchInputChanged = onSearchInputChanged,
        onSearchMovie = onSearchMovie
    ) { hasPostsUiState, contentPadding, contentModifier ->
        MovieListGrid(
            searchMoviesListPaging = hasPostsUiState.searchMoviesListPaging,
            trendingMoviesListPaging = hasPostsUiState.trendingMoviesListPaging,
            selectedItems = hasPostsUiState.selectedMovieListId,
            onAddMovieToCache = onAddMovieToCache,
            onSelectMovie = onSelectMovie,
            contentPadding = contentPadding,
            modifier = contentModifier,
            stateGrid = homeListLazyGridState,
            isSearchResult = hasPostsUiState.isSearchResult
        )
    }
}

@Composable
private fun MovieListGrid(
    searchMoviesListPaging: Flow<PagingData<Movie>>?,
    trendingMoviesListPaging: Flow<PagingData<Movie>>?,
    isSearchResult: Boolean,
    selectedItems: Set<Int>,
    onSelectMovie: (id: Int, fromTrending: Boolean) -> Unit,
    onAddMovieToCache: (movie: Movie) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
    stateGrid: LazyGridState = rememberLazyGridState(),
) {
    Column(
        modifier = modifier.padding(contentPadding),
    ) {
        val contentTypeText =
            if (isSearchResult) stringResource(id = R.string.search_result) else stringResource(
                id = R.string.trending
            )
        Text(
            text = contentTypeText,
            style = Typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
        )
        if (searchMoviesListPaging != null && isSearchResult) {

            val pagingItems: LazyPagingItems<Movie> =
                searchMoviesListPaging.collectAsLazyPagingItems()
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(all = 12.dp),
                state = stateGrid
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.id }
                ) { index ->
                    val movie = pagingItems[index] ?: return@items
                    onAddMovieToCache(movie)
                    MovieCardItemGrid(
                        movie = movie,
                        isSelected = selectedItems.contains(movie.id),
                        onSelectMovie = { onSelectMovie(movie.id, false) }
                    )
                }
            }
        } else if (trendingMoviesListPaging != null) {

            val pagingItems: LazyPagingItems<Movie> =
                trendingMoviesListPaging.collectAsLazyPagingItems()
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(all = 12.dp),
                state = stateGrid
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.id }
                ) { index ->
                    val movie = pagingItems[index] ?: return@items
                    MovieCardItemGrid(
                        movie = movie,
                        isSelected = selectedItems.contains(movie.id),
                        onSelectMovie = { onSelectMovie(movie.id, true) }
                    )
                }
            }
        }
    }
}

@Composable
fun MovieCardItemGrid(
    movie: Movie,
    onSelectMovie: (Int) -> Unit,
    isSelected: Boolean,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = {
                onSelectMovie(movie.id)
            })
    ) {
        MoviePosterImageGrid(movie, Modifier.padding(16.dp))
        MovieTitle(movie, isSelected, Modifier.padding(horizontal = 16.dp))
        YearAndVoteAverage(movie, Modifier.padding(horizontal = 16.dp))
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MoviePosterImageGrid(movie: Movie, modifier: Modifier = Modifier) {
    GlideImage(
        model = movie.posterPath,
        contentDescription = "Poster image for the movie ${movie.title}",
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(3f / 4f)
            .clip(
                Shapes.small
            ),
        contentScale = ContentScale.Crop,
        failure = placeholder(R.drawable.ic_launcher_foreground)
    )

}