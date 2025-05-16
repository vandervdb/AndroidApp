package org.vander.spotifyclient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import org.vander.spotifyclient.data.auth.SpotifyAuthClient
import org.vander.spotifyclient.domain.auth.ISpotifyAuthClient

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthModule {

    @Binds
    @ViewModelScoped
    abstract fun bindSpotifyAuthClient(
        spotifyAuthClient: SpotifyAuthClient
    ): ISpotifyAuthClient
}

