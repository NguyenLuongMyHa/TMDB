package vn.hanguyen.tmdb.model

class MoviesList(
    var trendingMovies: MutableList<Movie>,
    var searchResultMovies: MutableList<Movie>,
) {
    fun updateMovieDetail(movie: Movie) : MoviesList {
        trendingMovies.mapInPlace {
            if (it.id == movie.id) movie else it
        }
        searchResultMovies.mapInPlace {
            if (it.id == movie.id) movie else it
        }
        return this
    }

}

fun <Int> MutableList<Int>.mapInPlace(mutator: (Int) -> (Int)) {
    this.forEachIndexed { i, value ->
        val changedValue = mutator(value)
        this[i] = changedValue
    }
}