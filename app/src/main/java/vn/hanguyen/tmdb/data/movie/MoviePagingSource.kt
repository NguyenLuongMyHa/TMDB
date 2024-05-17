package vn.hanguyen.tmdb.data.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import vn.hanguyen.tmdb.api.TmdbService

private const val MOVIE_STARTING_PAGE_INDEX = 1

class MoviePagingSource(private val service: TmdbService, private val query: String) : PagingSource<Int, MovieResponse>() {
    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse> {
        val page = params.key ?: MOVIE_STARTING_PAGE_INDEX
        return try {
            val response = service.searchMovies(query, page)
            val movies = response.results
            LoadResult.Page(
                data = movies,
                prevKey = if (page == MOVIE_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.totalPages) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}