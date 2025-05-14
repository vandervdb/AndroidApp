package org.vander.spotifyclient.domain.playlist.repository

import org.vander.spotifyclient.domain.data.CurrentlyPlayingAndQueue
import org.vander.spotifyclient.domain.data.SpotifyPlaylistsResponse

interface ISpotifyRepository {
    suspend fun getUserQueue(): Result<CurrentlyPlayingAndQueue>
    suspend fun getUserPlaylists(): Result<SpotifyPlaylistsResponse>
}