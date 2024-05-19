package vn.hanguyen.tmdb.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MovieDao {

    /**
     * Insert a list of Movie objects. If the Movie objects are already in the table, then replace them.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    /**
     * return PagingSource<Int, Movie>. That way, the movies table becomes the source of data for Paging
     */
    @Query(
        "SELECT * FROM movies"
    )
    fun getAllMovies(): PagingSource<Int, MovieEntity>

    @Query(
        "SELECT * FROM movies WHERE id = :movieId"
    )
    suspend fun getSelectedMovie(movieId: Int): MovieEntity?

    @Update()
    suspend fun updateMovie(movieEntity: MovieEntity)

    /**
     * Clear all data in the Movies table.
     */
    @Query("DELETE FROM movies")
    suspend fun clearMovies()
}
