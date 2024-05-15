package vn.hanguyen.tmdb

import android.app.Application
import vn.hanguyen.tmdb.data.movie.AppContainer
import vn.hanguyen.tmdb.data.movie.AppContainerImpl

class TmdbApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}