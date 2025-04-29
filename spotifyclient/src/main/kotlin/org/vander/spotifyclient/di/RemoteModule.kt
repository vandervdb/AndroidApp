package org.vander.spotifyclient.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import org.vander.spotifyclient.data.client.remote.ISpotifyRemoteClient
import org.vander.spotifyclient.data.client.remote.SpotifyRemoteClient

@Module
@InstallIn(ViewModelComponent::class)
abstract class RemoteModule {

    @Binds
    @ViewModelScoped
    abstract fun bindSpotifyRemoteClient(
        spotifyRemoteClient: SpotifyRemoteClient
    ): ISpotifyRemoteClient

    companion object {
        @Provides
        @ViewModelScoped
        fun provideSpotifyRemoteClient(context: Context): SpotifyRemoteClient {
            return SpotifyRemoteClient(context)
        }
    }
}