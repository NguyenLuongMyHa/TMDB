package vn.hanguyen.tmdb.data.movie

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.hanguyen.tmdb.model.Movie
import vn.hanguyen.tmdb.util.Result
interface MoviesRepository {
    /**
     * Get a specific movie.
     */
    suspend fun getMovie(movieId: Int): Result<Movie>

    /**
     * Get trending movies.
     */
    suspend fun getTrendingMovies():  Result<List<Movie>>

    /**
     * Search movies.
     */
    fun getSearchResultStream(query: String): Flow<PagingData<MovieResponse>>
    /**
     * Observe the current selected movies
     */
    fun observeSelectedMovies(): Flow<Set<Int>>

    /**
     * Observe the movie list
     */
    fun observeMoviesList(): Flow<List<Movie>?>

    /**
     * Select a movie.
     */
    suspend fun selectMovie(movieId: Int)
}