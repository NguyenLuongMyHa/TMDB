@file:JvmName("MovieRemoteMediatorKt")

package vn.hanguyen.tmdb.data.remote

import android.net.http.HttpException
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import vn.hanguyen.tmdb.api.TmdbService
import vn.hanguyen.tmdb.data.local.MovieDatabase
import vn.hanguyen.tmdb.data.local.MovieEntity
import vn.hanguyen.tmdb.data.local.RemoteKeys
import java.io.IOException

private const val MOVIE_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val service: TmdbService,           //make network requests.
    private val movieDatabase: MovieDatabase    //save data we got from the network request
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> MOVIE_STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> movieDatabase.remoteKeysDao().getLastRemoteKey()?.nextKey
                ?: return MediatorResult.Success(endOfPaginationReached = true)
        }

        Log.d(
            "TRENDING_PAGING_LOG",
            "MovieRemoteMediator: load() called with: loadType = $loadType, page: $page, stateLastItem = ${state.isEmpty()}, "
        )

        // There was a lag in loading the first page; as a result, it jumps to the end of the pagination.
        if (state.isEmpty() && page == 2) return MediatorResult.Success(endOfPaginationReached = false)

        try {
            service.getTrendingMovies2(page).also { successResult ->
                Log.d("XXX", "MovieRemoteMediator: get movies from remote")
                if (loadType == LoadType.REFRESH) {
                    movieDatabase.remoteKeysDao().clearRemoteKeys()
                    movieDatabase.moviesDao().clearMovies()
                }

                val movies = successResult.results

                val endOfPaginationReached = movies.isEmpty()

                val prevPage = if (page == MOVIE_STARTING_PAGE_INDEX) null else page - 1
                val nextPage = if (endOfPaginationReached) null else page + 1

                val keys = movies.map {
                    RemoteKeys(movieId = it.id, prevKey = prevPage, nextKey = nextPage)
                }
                movieDatabase.moviesDao().insertAll(movies)
                movieDatabase.remoteKeysDao().insertAll(keys)

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}