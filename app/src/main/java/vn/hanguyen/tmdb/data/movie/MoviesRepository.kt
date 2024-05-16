package vn.hanguyen.tmdb.data.movie

import kotlinx.coroutines.flow.Flow
import vn.hanguyen.tmdb.model.Movie
import vn.hanguyen.tmdb.util.Result
interface MoviesRepository {
    /**
     * Get a specific movie.
     */
    suspend fun getMovie(movieId: Long?, fromSearchResult: Boolean = false): Result<Movie>

    /**
     * Get trending movies.
     */
    suspend fun getTrendingMovies():  Result<List<Movie>>

    /**
     * Search movies.
     */
    suspend fun searchMovies(searchKey: String):  Result<List<Movie>>

    /**
     * Observe the current selected movies
     */
    fun observeSelectedMovies(): Flow<Set<Long>>

    /**
     * Observe the movie list
     */
    fun observeMoviesList(): Flow<List<Movie>?>

    /**
     * Select a movie.
     */
    suspend fun selectMovie(movieId: Long)
}