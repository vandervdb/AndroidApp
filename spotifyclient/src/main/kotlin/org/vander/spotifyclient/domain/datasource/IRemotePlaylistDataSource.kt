package org.vander.spotifyclient.domain.datasource

import org.vander.spotifyclient.data.playlist.dto.SpotifyPlaylistsResponseDto


interface IRemotePlaylistDataSource {
    suspend fun fetchUserPlaylists(): Result<SpotifyPlaylistsResponseDto>
}