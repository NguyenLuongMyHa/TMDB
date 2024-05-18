package vn.hanguyen.tmdb.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    /**
     * Insert a list of **RemoteKeys**, as whenever we get Movies from the network we will generate the remote keys for them.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    /**
     * Get a **RemoteKey** based on a Movie id.
     */
    @Query("SELECT * FROM remote_keys WHERE movieId = :movieId")
    suspend fun remoteKeysMovieId(movieId: Int): RemoteKeys?

    /**
     * Clear the **RemoteKeys**, which we will use whenever we have a new query.
     */
    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}
