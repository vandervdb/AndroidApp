package org.vander.spotifyclient.domain.playlist.repository

import org.vander.spotifyclient.domain.data.SpotifyPlaylistsResponse

interface IPlaylistRepository {
    suspend fun getUserPlaylists(token: String): Result<SpotifyPlaylistsResponse>
}