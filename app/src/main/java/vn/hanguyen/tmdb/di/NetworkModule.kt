
package vn.hanguyen.tmdb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.hanguyen.tmdb.api.TmdbService
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideTMDBService(): TmdbService {
        return TmdbService.create()
    }
}
