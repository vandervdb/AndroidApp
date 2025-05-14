package org.vander.spotifyclient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.vander.spotifyclient.data.remote.datasource.SpotifyRemoteDataSource
import org.vander.spotifyclient.data.repository.SpotifyRepository
import org.vander.spotifyclient.domain.datasource.ISpotifyRemoteDataSource
import org.vander.spotifyclient.domain.playlist.repository.ISpotifyRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaylistModule {

    @Binds
    @Singleton
    abstract fun bindPlaylistRepository(
        impl: SpotifyRepository
    ): ISpotifyRepository

    @Binds
    @Singleton
    abstract fun bindPlaylistRemoteDataSource(
        impl: SpotifyRemoteDataSource
    ): ISpotifyRemoteDataSource
}
