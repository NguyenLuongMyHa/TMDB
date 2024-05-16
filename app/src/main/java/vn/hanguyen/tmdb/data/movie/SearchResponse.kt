package vn.hanguyen.tmdb.data.movie

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @field:SerializedName("results") val results: List<MovieResponse>,
    @field:SerializedName("total_pages") val totalPages: Int
)