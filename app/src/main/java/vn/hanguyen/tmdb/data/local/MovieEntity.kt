package vn.hanguyen.tmdb.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import vn.hanguyen.tmdb.model.Movie

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey @field:SerializedName("id") val id: Int,
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
    //   TODO impl below entity
    //    @field:SerializedName("belongs_to_collection") val belongsToCollection: MovieCollection?,
    //    @field:SerializedName("origin_country") val originCountry: List<String>?,
    //    @field:SerializedName("genres") val genres: List<Genres>?,
    //    @field:SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>?,
    //    @field:SerializedName("production_companies") val productionCompanies: List<ProductionCompany>?,
) {
    fun toMovie(): Movie {
        return Movie(
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
//            belongsToCollection = belongsToCollection?.toMovieCollection(),
//            originCountry = originCountry,
//            genres = genres?.map{ genre -> genre.toGenres()},
//            spokenLanguages = spokenLanguages?.map { spokenLanguage -> spokenLanguage.toSpokenLanguage() },
//            productionCompanies = productionCompanies?.map { productionCompany -> productionCompany.toProductionCompany() },
        )
    }
}