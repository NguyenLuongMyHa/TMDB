package vn.hanguyen.tmdb.model

data class Movie(
    val id: Int,
    val posterPath: String? = null,
    val title: String? = null,
    val releaseDate: String,
    val voteAverage: Float? = null,
    val voteCount: Int? = null,
    val overview: String? = null,
    val tagline: String? = null,
    val status: String? = null,
    val adult: Boolean? = null,
    val video: Boolean? = null,
    val revenue: Int? = null,
    val budget: Int? = null,
    val popularity: Float? = null,
    val homepage: String? = null,
    val belongsToCollection: MovieCollection? = null,
    val originCountry: List<String>? = null,
    val genres: List<Genres>? = null,
    val spokenLanguages: List<SpokenLanguage>? = null,
    val productionCompanies: List<ProductionCompany>? = null,
)
data class MovieCollection (
    val id: String,
    val name: String? = null,
    val posterPath: String? = null,
)
data class ProductionCompany (
    val logoPath: String? = null,
    val name: String? = null,
    val originCountry: String? = null,
)

data class SpokenLanguage(
    val englishName: String? = null,
)

data class Genres(
    val name: String? = null,
)
