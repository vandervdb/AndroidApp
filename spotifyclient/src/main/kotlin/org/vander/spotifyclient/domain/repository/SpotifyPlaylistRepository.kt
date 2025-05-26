package org.vander.spotifyclient.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.vander.spotifyclient.domain.data.SpotifyPlaylistsResponse

interface SpotifyPlaylistRepository {
    val playlists: StateFlow<SpotifyPlaylistsResponse?>
    suspend fun getUserPlaylists(): Result<SpotifyPlaylistsResponse>
}