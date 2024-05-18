package vn.hanguyen.tmdb.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import vn.hanguyen.tmdb.api.TmdbService
import vn.hanguyen.tmdb.data.remote.MoviePagingSource
import vn.hanguyen.tmdb.data.remote.MovieResponse
import vn.hanguyen.tmdb.model.Movie
import vn.hanguyen.tmdb.util.Result
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(private val service: TmdbService) :
    MoviesRepository {
    // for now, store these in memory, later, using Room
    private val selectedMovies = MutableStateFlow<Set<Int>>(setOf())
    private val trendingMoviesList = MutableStateFlow<List<Movie>?>(null)

    companion object {
        private const val NETWORK_PAGE_SIZE = 20 //TMDB configuration always return 20 movies per page, max 500 pages
    }
    override suspend fun getMovie(movieId: Int): Result<Movie> {
        return withContext(Dispatchers.IO) {
            try {
                val result = service.getMovieDetail(movieId)
                    Result.Success(result.toMovie())
            } catch (_: Exception) {
                Result.Error(IllegalArgumentException("Movie not found"))
            }
        }
    }

    override suspend fun getTrendingMovies(): Result<List<Movie>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = service.getTrendingMovies()
                if(result.results.isNotEmpty()) {
                    val resultMovieList = mutableListOf<Movie>()
                    result.results.forEach { movieResponse ->  resultMovieList.add(movieResponse.toMovie())}
                    trendingMoviesList.update { resultMovieList }
                    Result.Success(resultMovieList)
                }
                else {
                    Result.Error(IllegalArgumentException("Movie not found"))
                }
            } catch (_: Exception) {
                Result.Error(IllegalArgumentException("Movie not found"))
            }
        }
    }

    override fun getSearchResultStream(query: String): Flow<PagingData<MovieResponse>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { MoviePagingSource(service, query) }
        ).flow.map {
            val movieIdMap = mutableSetOf<Int>()
            it.filter { movie ->
                if (movieIdMap.contains(movie.id)) {
                    false
                } else {
                    movieIdMap.add(movie.id)
                }
            }
        }
    }

    override fun observeSelectedMovies(): Flow<Set<Int>> = selectedMovies

    override fun observeMoviesList(): Flow<List<Movie>?> = trendingMoviesList

    override suspend fun selectMovie(movieId: Int) {
        selectedMovies.update {
            it.plus(movieId)
        }
    }
}