package org.vander.androidapp.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.vander.spotifyclient.domain.SpotifySessionManager

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SpotifySessionEntryPoint {
    fun spotifySessionManager(): SpotifySessionManager
}