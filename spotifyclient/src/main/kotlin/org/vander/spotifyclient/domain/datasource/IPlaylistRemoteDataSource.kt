package org.vander.spotifyclient.domain.datasource

import org.vander.spotifyclient.data.playlist.dto.SpotifyPlaylistDto
import org.vander.spotifyclient.data.playlist.dto.SpotifyPlaylistsResponseDto

interface IPlaylistRemoteDataSource {
    suspend fun fetchUserPlaylists(): Result<SpotifyPlaylistsResponseDto>
    suspend fun fetchPlaylist(playlistId: String): Result<SpotifyPlaylistDto>

}