package vn.hanguyen.tmdb.data.movie

import android.content.Context

interface AppContainer {
    val moviesRepository: MoviesRepository
}

class AppContainerImpl(private val applicationContext: Context) : AppContainer {
    override val moviesRepository: MoviesRepository by lazy {
        MoviesRepositoryImpl()
    }
}
