package org.vander.spotifyclient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import org.vander.spotifyclient.data.client.player.ISpotifyPlayerClient
import org.vander.spotifyclient.data.client.player.SpotifyPlayerClient

@Module
@InstallIn(ViewModelComponent::class)
abstract class PlayerModule {

    @Binds
    @ViewModelScoped
    abstract fun bindSpotifyPlayerClient(
        spotifyPlayerClient: SpotifyPlayerClient
    ): ISpotifyPlayerClient
}