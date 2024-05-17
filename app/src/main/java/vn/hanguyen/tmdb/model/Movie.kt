package vn.hanguyen.tmdb.model

data class Movie(
    val id: Long,
    val title: String,
    val releaseYear: String,
    val overview: String? = null,
    val posterUrl: String?,
    val voteAverage: Float,
    val production: MovieProduction? = null
)

data class MovieProduction(
 val name: String,
    val imageUrl: String,
    val country: String
)