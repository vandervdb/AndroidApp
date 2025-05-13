package org.vander.spotifyclient.domain.datasource

import org.vander.spotifyclient.data.playlist.dto.SpotifyPlaylistDto
import org.vander.spotifyclient.data.playlist.dto.SpotifyPlaylistsResponseDto
import org.vander.spotifyclient.data.remote.dto.CurrentlyPlayingWithQueueDto

interface IPlaylistRemoteDataSource {
    suspend fun fetchUserQueue(): Result<CurrentlyPlayingWithQueueDto>
    suspend fun fetchUserPlaylists(): Result<SpotifyPlaylistsResponseDto>
    suspend fun fetchPlaylist(playlistId: String): Result<SpotifyPlaylistDto>

}