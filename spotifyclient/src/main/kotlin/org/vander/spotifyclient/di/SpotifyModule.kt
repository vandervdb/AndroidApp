package org.vander.spotifyclient.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.vander.spotifyclient.data.client.auth.ISpotifyAuthClient
import org.vander.spotifyclient.data.client.auth.SpotifyClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpotifyModule {

    @Provides
    @Singleton
    fun provideSpotifyClient(
        @ApplicationContext context: Context
    ): ISpotifyAuthClient {
        return SpotifyClient(context)
    }
}