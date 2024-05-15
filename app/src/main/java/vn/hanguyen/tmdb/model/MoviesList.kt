package vn.hanguyen.tmdb.model

data class MoviesList(
    val trendingMovies: List<Movie>,
    val searchResultMovies: List<Movie>,
)