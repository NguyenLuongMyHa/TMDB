package vn.hanguyen.tmdb.model

import vn.hanguyen.tmdb.data.local.MovieEntity

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
) {
    fun toMovieEntity() :MovieEntity  = MovieEntity(
        title = title,
        id = id,
        posterPath = if (posterPath == null) null else "https://image.tmdb.org/t/p/original$posterPath",
        releaseDate = releaseDate?:"----",
        voteAverage = voteAverage,
        overview = overview,
        voteCount = voteCount,
        tagline = tagline,
        status = status,
        adult = adult,
        video = video,
        revenue = revenue,
        budget = budget,
        popularity = popularity,
        homepage = homepage,
        belongsToCollection = belongsToCollection?.toMovieCollection(),
        originCountry = originCountry,
        genres = genres?.map{ genre -> genre.toGenres()},
        spokenLanguages = spokenLanguages?.map { spokenLanguage -> spokenLanguage.toSpokenLanguage() },
        productionCompanies = productionCompanies?.map { productionCompany -> productionCompany.toProductionCompany() },
    )
}
data class MovieCollection (
    val id: String,
    val name: String? = null,
    val posterPath: String? = null,
) {
    fun toMovieCollection(): vn.hanguyen.tmdb.data.remote.MovieCollection = vn.hanguyen.tmdb.data.remote.MovieCollection(id, name, posterPath)
}
data class ProductionCompany (
    val logoPath: String? = null,
    val name: String? = null,
    val originCountry: String? = null,
) {
    fun toProductionCompany(): vn.hanguyen.tmdb.data.remote.ProductionCompany = vn.hanguyen.tmdb.data.remote.ProductionCompany(logoPath, name, originCountry)
}

data class SpokenLanguage(
    val englishName: String? = null,
) {
    fun toSpokenLanguage(): vn.hanguyen.tmdb.data.remote.SpokenLanguage = vn.hanguyen.tmdb.data.remote.SpokenLanguage(englishName)
}

data class Genres(
    val name: String? = null,
) {
    fun toGenres(): vn.hanguyen.tmdb.data.remote.Genres = vn.hanguyen.tmdb.data.remote.Genres(name)
}
