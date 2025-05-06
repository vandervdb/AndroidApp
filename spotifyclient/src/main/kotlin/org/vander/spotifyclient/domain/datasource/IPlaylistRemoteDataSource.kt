package org.vander.spotifyclient.domain.datasource

import org.vander.spotifyclient.data.playlist.dto.SpotifyPlaylistsResponseDto

interface IPlaylistRemoteDataSource {
    suspend fun fetchUserPlaylists(token: String): Result<SpotifyPlaylistsResponseDto>
}