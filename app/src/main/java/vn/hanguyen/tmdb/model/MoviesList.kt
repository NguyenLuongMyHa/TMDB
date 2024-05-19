package vn.hanguyen.tmdb.model

class MoviesList(
    var movies: MutableList<Movie>,
) {
    fun updateMovieDetail(movie: Movie) : MoviesList {
        if(movies.find { it.id == movie.id } != null) {
            movies.mapInPlace { if (it.id == movie.id) movie else it }
        } else movies.add(movie)
        return this
    }

}

fun <Int> MutableList<Int>.mapInPlace(mutator: (Int) -> (Int)) {
    this.forEachIndexed { i, value ->
        val changedValue = mutator(value)
        this[i] = changedValue
    }
}