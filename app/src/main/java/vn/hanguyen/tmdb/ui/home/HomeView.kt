package vn.hanguyen.tmdb.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import vn.hanguyen.tmdb.R
import vn.hanguyen.tmdb.data.movie.MovieResponse
import vn.hanguyen.tmdb.model.Movie
import vn.hanguyen.tmdb.ui.detail.movieContentItems
import vn.hanguyen.tmdb.ui.theme.Shapes
import vn.hanguyen.tmdb.ui.theme.Typography
import vn.hanguyen.tmdb.util.interceptKey


/**
 * The home screen displaying the movie list.
 */
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    showTopAppBar: Boolean,
    onSelectMovie: (Long) -> Unit,
    onRefreshMovies: () -> Unit,
    homeListLazyListState: LazyListState,
    modifier: Modifier = Modifier,
    searchInput: String = "",
    onSearchInputChanged: (String) -> Unit,
    onSearchMovie: () -> Unit,
    onAddMovieToCache: (movie: Movie) -> Unit,
    ) {
    HomeScreenWithList(
        uiState = uiState,
        showTopAppBar = showTopAppBar,
        onRefreshMovies = onRefreshMovies,
        modifier = modifier,
        searchInput = uiState.searchInput,
        onSearchInputChanged = onSearchInputChanged,
        onSearchMovie = onSearchMovie
    ) { hasPostsUiState, contentPadding, contentModifier ->
        MovieList(
            moviesList = hasPostsUiState.moviesList,
            moviesListPaging = hasPostsUiState.moviesListPaging,
            selectedItems = hasPostsUiState.selectedMovieListId,
            onAddMovieToCache = onAddMovieToCache,
            onSelectMovie = onSelectMovie,
            modifier = contentModifier,
            state = homeListLazyListState,
            isSearchResult = hasPostsUiState.isSearchResult
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenWithList(
    uiState: HomeUiState,
    showTopAppBar: Boolean,
    onRefreshMovies: () -> Unit,
    modifier: Modifier = Modifier,
    searchInput: String = "",
    onSearchInputChanged: (String) -> Unit,
    onSearchMovie: () -> Unit,
    hasMoviesContent: @Composable (
        uiState: HomeUiState.HasMovies,
        contentPadding: PaddingValues,
        modifier: Modifier
    ) -> Unit,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    Scaffold(
        topBar = {
            HomeSearch(
                Modifier.padding(16.dp),
                searchInput = searchInput,
                onSearchInputChanged = onSearchInputChanged,
                onSearchMovie = onSearchMovie
            )
        },
        modifier = modifier
    ) { innerPadding ->
        val contentModifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)

        LoadingContent(
            empty = when (uiState) {
                is HomeUiState.HasMovies -> false
                is HomeUiState.NoMovies -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshMovies,
            content = {
                when (uiState) {
                    is HomeUiState.HasMovies ->
                        hasMoviesContent(uiState, innerPadding, contentModifier)

                    is HomeUiState.NoMovies -> {
                        if (uiState.errorMessages.isEmpty()) {
                            // if there are no movie, and no error, let the user refresh manually
                            TextButton(
                                onClick = onRefreshMovies,
                                modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                            ) {
                                Text(
                                    stringResource(id = R.string.home_tap_to_load_content),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            Box(
                                contentModifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                            ) { /* empty screen */ }
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        Box(Modifier.fillMaxSize().zIndex(-1f)) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(loading),
                onRefresh = onRefresh,
                content = content,
            )
        }

    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MovieList(
    moviesList: List<Movie>,
    moviesListPaging: Flow<PagingData<MovieResponse>>?,
    isSearchResult: Boolean,
    selectedItems: Set<Long>,
    onSelectMovie: (postId: Long) -> Unit,
    onAddMovieToCache: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
) {
    Column(
        modifier = modifier,
    ) {
        if (moviesListPaging != null && isSearchResult) {
            val contentTypeText =
                if (isSearchResult) stringResource(id = R.string.search_result) else stringResource(
                    id = R.string.trending
                )
            Text(
                text = contentTypeText,
                style = Typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            val pagingItems: LazyPagingItems<MovieResponse> =
                moviesListPaging.collectAsLazyPagingItems()
            LazyColumn(
                contentPadding = PaddingValues(all = 0.dp),
                state = state
            ) {
                items(
                    count = pagingItems.itemCount,
                      key = pagingItems.itemKey { it.id }
                ) { index ->
                    val movie = pagingItems[index] ?: return@items
                    onAddMovieToCache(movie.toMovie())
                    MovieCardItem(
                        movie = movie.toMovie(),
                        isSelected = selectedItems.contains(movie.id),
                        onSelectMovie = { onSelectMovie(movie.id) }
                    )
                }
            }
        }
        else {
            LazyColumn(
                modifier = modifier,
                state = state
            ) {
                if (moviesList.isNotEmpty()) {
                    item {
                        val contentTypeText =
                            if (isSearchResult) stringResource(id = R.string.search_result) else stringResource(
                                id = R.string.trending
                            )
                        Text(
                            text = contentTypeText,
                            style = Typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    item {
                        MovieItemSection(
                            moviesList,
                            selectedItems,
                            onSelectMovie,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeSearch(
    modifier: Modifier = Modifier,
    searchInput: String = "",
    onSearchInputChanged: (String) -> Unit,
    onSearchMovie: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = searchInput,
        onValueChange = onSearchInputChanged,
        placeholder = { Text(stringResource(R.string.home_search)) },
        leadingIcon = { Icon(Icons.Filled.Search, null) },
        modifier = modifier
            .fillMaxWidth()
            .interceptKey(Key.Enter) {
                // submit a search query when Enter is pressed
                onSearchMovie()
                keyboardController?.hide()
                focusManager.clearFocus(force = true)
            },
        singleLine = true,
        // keyboardOptions change the newline key to a search key on the soft keyboard
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        // keyboardActions submits the search query when the search key is pressed
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchMovie()
                keyboardController?.hide()
                focusManager.clearFocus(force = true)
            }
        )
    )
}


@Composable
private fun MovieItemSection(
    movies: List<Movie>,
    selectedMovies: Set<Long>,
    onSelectMovie: (Long) -> Unit
) {
    Column {
        movies.forEach { movie ->
            MovieCardItem(
                movie = movie,
                isSelected = selectedMovies.contains(movie.id),
                onSelectMovie = { onSelectMovie(movie.id) }
            )
            MovieItemsListDivider()
        }
    }
}

@Composable
private fun MovieItemsListDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}


@Composable
fun MovieCardItem(
    movie: Movie,
    onSelectMovie: (Long) -> Unit,
    isSelected: Boolean,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = {
                onSelectMovie(movie.id)
            })
    ) {
        MoviePosterImage(movie, Modifier.padding(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 10.dp)
        ) {
            MovieTitle(movie, isSelected)
            YearAndVoteAverage(movie)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MoviePosterImage(movie: Movie, modifier: Modifier = Modifier) {
    GlideImage(
        model = movie.posterUrl,
        contentDescription = "Poster image for the movie ${movie.title}",
        modifier = modifier
            .size(64.dp)
            .clip(
                Shapes.small
            ),
        contentScale = ContentScale.Crop,
        failure = placeholder(R.drawable.ic_launcher_foreground)
    )

}

@Composable
fun MovieTitle(movie: Movie, isSelected: Boolean) {
    val style = if (isSelected) {
        MaterialTheme.typography.titleMedium.copy(textDecoration = TextDecoration.Underline)
    } else MaterialTheme.typography.titleMedium
    Text(
        text = movie.title,
        style = style,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun YearAndVoteAverage(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(
            text = stringResource(
                id = R.string.home_movie_year_vote,
                formatArgs = arrayOf(
                    movie.releaseYear,
                    movie.voteAverage
                )
            ),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeListWithMovieDetailsScreen(
    uiState: HomeUiState,
    showTopAppBar: Boolean,
    onSelectMovieItem: (Long) -> Unit,
    onRefreshMovies: () -> Unit,
    onInteractWithList: () -> Unit,
    onInteractWithDetail: (Long) -> Unit,
    homeListLazyListState: LazyListState,
    movieDetailLazyListStates: Map<Long, LazyListState>,
    modifier: Modifier = Modifier,
    searchInput: String = "",
    onSearchInputChanged: (String) -> Unit,
    onSearchMovie: () -> Unit,
    onAddMovieToCache: (movie: Movie) -> Unit,
    ) {
    HomeScreenWithList(
        uiState = uiState,
        showTopAppBar = showTopAppBar,
        onRefreshMovies = onRefreshMovies,
        modifier = modifier,
        searchInput = searchInput,
        onSearchInputChanged = onSearchInputChanged,
        onSearchMovie = onSearchMovie
    ) { hasMoviesUiState, contentPadding, contentModifier ->
        Row(contentModifier) {
            MovieList(
                moviesList = hasMoviesUiState.moviesList,
                moviesListPaging = hasMoviesUiState.moviesListPaging,
                selectedItems = hasMoviesUiState.selectedMovieListId,
                onSelectMovie = onSelectMovieItem,
                modifier = Modifier
                    .width(334.dp)
                    .notifyInput(onInteractWithList),
                state = homeListLazyListState,
                onAddMovieToCache = onAddMovieToCache,
                isSearchResult = hasMoviesUiState.isSearchResult
            )
            // Crossfade between different detail posts
            if(hasMoviesUiState.selectedMovie!= null)
            Crossfade(targetState = hasMoviesUiState.selectedMovie) { detailMovie ->
                // Get the lazy list state for this detail view
                val detailLazyListState by remember {
                    derivedStateOf {
                        movieDetailLazyListStates.getValue(detailMovie.id)
                    }
                }

                // Key against the movie id to avoid sharing any state between different movies
                key(detailMovie.id) {
                    LazyColumn(
                        state = detailLazyListState,
                        contentPadding = contentPadding,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize()
                            .notifyInput {
                                onInteractWithDetail(detailMovie.id)
                            }
                    ) {
                        movieContentItems(detailMovie)
                    }
                }
            }
        }
    }
}

/**
 * A [Modifier] that tracks all input, and calls [block] every time input is received.
 */
private fun Modifier.notifyInput(block: () -> Unit): Modifier =
    composed {
        val blockState = rememberUpdatedState(block)
        pointerInput(Unit) {
            while (currentCoroutineContext().isActive) {
                awaitPointerEventScope {
                    awaitPointerEvent(PointerEventPass.Initial)
                    blockState.value()
                }
            }
        }
    }