package vn.hanguyen.tmdb.data.movie

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import vn.hanguyen.tmdb.api.TmdbService
import vn.hanguyen.tmdb.model.Movie
import vn.hanguyen.tmdb.util.Result
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(private val service: TmdbService) : MoviesRepository {
    // for now, store these in memory, later, using Room
    private val selectedMovies = MutableStateFlow<Set<Long>>(setOf())

    private val trendingMoviesList = MutableStateFlow<List<Movie>?>(null)
    private val searchMoviesList = MutableStateFlow<List<Movie>?>(null)


    override suspend fun getMovie(movieId: Long?, fromSearchResult: Boolean): Result<Movie> {
        return withContext(Dispatchers.IO) {

            val movie =
                if (fromSearchResult) movies.searchResultMovies.find { it.id == movieId } else movies.trendingMovies.find { it.id == movieId }
            if (movie == null) {
                Result.Error(IllegalArgumentException("Movie not found"))
            } else {
                Result.Success(movie)
            }
        }
    }

    override suspend fun getTrendingMovies(): Result<List<Movie>> {
        return withContext(Dispatchers.IO) {
            delay(800) // pretend we're on a slow network
            trendingMoviesList.update { movies.trendingMovies }
            Result.Success(movies.trendingMovies)
        }
    }

    override suspend fun searchMovies(searchKey: String): Result<List<Movie>> {
        return withContext(Dispatchers.IO) {
            delay(800) // pretend we're on a slow network
            searchMoviesList.update { movies.searchResultMovies }
            Result.Success(movies.searchResultMovies)
        }
    }

    override fun observeSelectedMovies(): Flow<Set<Long>> = selectedMovies

    override fun observeMoviesList(): Flow<List<Movie>?> = trendingMoviesList

    override suspend fun selectMovie(movieId: Long) {
        selectedMovies.update {
            it.plus(movieId)
        }
    }
}