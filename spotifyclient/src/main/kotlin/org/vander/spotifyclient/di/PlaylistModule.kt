package org.vander.spotifyclient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.vander.spotifyclient.data.remote.datasource.PlaylistRemoteDataSource
import org.vander.spotifyclient.data.repository.PlaylistRepository
import org.vander.spotifyclient.domain.datasource.IPlaylistRemoteDataSource
import org.vander.spotifyclient.domain.playlist.repository.IPlaylistRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaylistModule {

    @Binds
    @Singleton
    abstract fun bindPlaylistRepository(
        impl: PlaylistRepository
    ): IPlaylistRepository

    @Binds
    @Singleton
    abstract fun bindPlaylistRemoteDataSource(
        impl: PlaylistRemoteDataSource
    ): IPlaylistRemoteDataSource
}
