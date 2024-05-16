package vn.hanguyen.tmdb.data.movie

import com.google.gson.annotations.SerializedName
import vn.hanguyen.tmdb.model.Movie

/*
{
            "adult": false,
            "backdrop_path": "/fypydCipcWDKDTTCoPucBsdGYXW.jpg",
            "genre_ids": [
                878,
                12,
                28
            ],
            "id": 653346,
            "original_language": "en",
            "original_title": "Kingdom of the Planet of the Apes",
            "overview": "Several generations in the future following Caesar's reign, apes are now the dominant species and live harmoniously while humans have been reduced to living in the shadows. As a new tyrannical ape leader builds his empire, one young ape undertakes a harrowing journey that will cause him to question all that he has known about the past and to make choices that will define a future for apes and humans alike.",
            "popularity": 2181.36,
            "poster_path": "/gKkl37BQuKTanygYQG1pyYgLVgf.jpg",
            "release_date": "2024-05-08",
            "title": "Kingdom of the Planet of the Apes",
            "video": false,
            "vote_average": 7.255,
            "vote_count": 345
        },
 */
data class MovieResponse(
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("backdrop_path") val backdropPath: String,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("release_date") val releaseDate: String,
    @field:SerializedName("vote_average") val voteAverage: Float,
    @field:SerializedName("overview") val overview: String,
) {
    fun toMovie() : Movie = Movie(title = title, id = id, posterUrl = "https://image.tmdb.org/t/p/original"+ backdropPath, releaseYear = releaseDate, voteAverage = voteAverage, overview = overview, production = null)
}