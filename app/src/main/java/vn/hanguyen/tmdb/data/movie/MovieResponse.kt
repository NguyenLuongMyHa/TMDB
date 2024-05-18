package vn.hanguyen.tmdb.data.movie

import com.google.gson.annotations.SerializedName
import vn.hanguyen.tmdb.model.Movie

/*
{
  "adult": false,
  "backdrop_path": "/qrGtVFxaD8c7et0jUtaYhyTzzPg.jpg",
  "belongs_to_collection": {
    "id": 1280074,
    "name": "Kong Collection",
    "poster_path": "/lhyEUeOihbKf7ll8RCIE5CHTie3.jpg",
    "backdrop_path": "/qHY4ZMIDSmElhiykjhh40Q5qMJl.jpg"
  },
  "budget": 150000000,
  "genres": [
    {
      "id": 878,
      "name": "Science Fiction"
    },
    {
      "id": 28,
      "name": "Action"
    },
    {
      "id": 12,
      "name": "Adventure"
    }
  ],
  "homepage": "https://www.godzillaxkongmovie.com",
  "id": 823464,
  "imdb_id": "tt14539740",
  "origin_country": [
    "US"
  ],
  "original_language": "en",
  "original_title": "Godzilla x Kong: The New Empire",
  "overview": "Following their explosive showdown, Godzilla and Kong must reunite against a colossal undiscovered threat hidden within our world, challenging their very existence – and our own.",
  "popularity": 6633.896,
  "poster_path": "/z1p34vh7dEOnLDmyCrlUVLuoDzd.jpg",
  "production_companies": [
    {
      "id": 923,
      "logo_path": "/8M99Dkt23MjQMTTWukq4m5XsEuo.png",
      "name": "Legendary Pictures",
      "origin_country": "US"
    },
    {
      "id": 174,
      "logo_path": "/zhD3hhtKB5qyv7ZeL4uLpNxgMVU.png",
      "name": "Warner Bros. Pictures",
      "origin_country": "US"
    }
  ],
  "production_countries": [
    {
      "iso_3166_1": "US",
      "name": "United States of America"
    }
  ],
  "release_date": "2024-03-27",
  "revenue": 558503756,
  "runtime": 115,
  "spoken_languages": [
    {
      "english_name": "English",
      "iso_639_1": "en",
      "name": "English"
    }
  ],
  "status": "Released",
  "tagline": "Rise together or fall alone.",
  "title": "Godzilla x Kong: The New Empire",
  "video": false,
  "vote_average": 7.085,
  "vote_count": 1548
}
 */
data class MovieResponse(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("poster_path") val posterPath: String?,
    @field:SerializedName("title") val title: String?,
    @field:SerializedName("release_date") val releaseDate: String?,
    @field:SerializedName("vote_average") val voteAverage: Float?,
    @field:SerializedName("vote_count") val voteCount: Int?,
    @field:SerializedName("overview") val overview: String?,
    @field:SerializedName("tagline") val tagline: String?,
    @field:SerializedName("status") val status: String?,
    @field:SerializedName("adult") val adult: Boolean?,
    @field:SerializedName("video") val video: Boolean?,
    @field:SerializedName("revenue") val revenue: Int?,
    @field:SerializedName("budget") val budget: Int?,
    @field:SerializedName("popularity") val popularity: Float?,
    @field:SerializedName("homepage") val homepage: String?,
    @field:SerializedName("belongs_to_collection") val belongsToCollection: MovieCollection?,
    @field:SerializedName("origin_country") val originCountry: List<String>?,
    @field:SerializedName("genres") val genres: List<Genres>?,
    @field:SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>?,
    @field:SerializedName("production_companies") val productionCompanies: List<ProductionCompany>?,
) {
    fun toMovie(): Movie {
        return Movie(
            title = title,
            id = id,
            posterPath = if (posterPath == null) null else "https://image.tmdb.org/t/p/original$posterPath",
            releaseDate = releaseDate?:"",
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
}

data class MovieCollection(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("poster_path") val posterPath: String?,
) {
    fun toMovieCollection() : vn.hanguyen.tmdb.model.MovieCollection {
        return vn.hanguyen.tmdb.model.MovieCollection(id = id, name = name, posterPath = if (posterPath == null) null else "https://image.tmdb.org/t/p/original$posterPath")
    }
}

data class ProductionCompany(
    @field:SerializedName("logo_path") val logoPath: String?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("origin_country") val originCountry: String?,
) {
    fun toProductionCompany() : vn.hanguyen.tmdb.model.ProductionCompany {
        return vn.hanguyen.tmdb.model.ProductionCompany(
            logoPath = if (logoPath == null) null else "https://image.tmdb.org/t/p/original$logoPath",
            name = name,
            originCountry = originCountry)
    }
}

data class SpokenLanguage(
    @field:SerializedName("english_name") val englishName: String?,
) {
    fun toSpokenLanguage(): vn.hanguyen.tmdb.model.SpokenLanguage {
        return vn.hanguyen.tmdb.model.SpokenLanguage(englishName = englishName)
    }
}

data class Genres(
    @field:SerializedName("name") val name: String?,
) {
    fun toGenres() : vn.hanguyen.tmdb.model.Genres {
        return vn.hanguyen.tmdb.model.Genres(name = name)
    }
}
