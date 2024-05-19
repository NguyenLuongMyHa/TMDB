package vn.hanguyen.tmdb.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(
    entities = [MovieEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MovieCollectionTypeConverter::class, ListStringTypeConverters::class, ListGenresTypeConverter::class, ListSpokenLanguageTypeConverter::class, ListProductionCompanyTypeConverter::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun moviesDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        /**
         * builds the MovieDatabase object if it doesn't exist already.
         */
        fun getInstance(context: Context): MovieDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MovieDatabase::class.java, "Movies.db"
            )
                .build()
    }
}
