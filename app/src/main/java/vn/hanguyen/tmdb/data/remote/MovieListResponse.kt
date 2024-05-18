package vn.hanguyen.tmdb.data.remote

import com.google.gson.annotations.SerializedName
import vn.hanguyen.tmdb.data.local.MovieEntity

data class MovieListResponse(
    @field:SerializedName("results") val results: List<MovieResponse>,
    @field:SerializedName("total_pages") val totalPages: Int
)
data class MovieListResponse2(
    @field:SerializedName("results") val results: List<MovieEntity>,
    @field:SerializedName("total_pages") val totalPages: Int
)
