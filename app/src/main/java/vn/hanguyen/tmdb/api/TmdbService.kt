package vn.hanguyen.tmdb.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import vn.hanguyen.tmdb.BuildConfig
import vn.hanguyen.tmdb.data.movie.SearchResponse

/**
 * Connect to the Moviedb API to fetch movies
 */
interface TmdbService {

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
//        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_ACCESS_KEY
    ): SearchResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
//        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_ACCESS_KEY
    ): SearchResponse


    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        fun create(): TmdbService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TmdbService::class.java)
        }
    }
}