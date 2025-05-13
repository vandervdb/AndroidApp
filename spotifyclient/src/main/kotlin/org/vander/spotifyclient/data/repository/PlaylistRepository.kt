package org.vander.spotifyclient.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import org.vander.spotifyclient.data.playlist.mapper.toDomain
import org.vander.spotifyclient.data.playlist.dto.SpotifyPlaylistsResponseDto
import org.vander.spotifyclient.domain.data.SpotifyPlaylistsResponse
import org.vander.spotifyclient.domain.datasource.IPlaylistRemoteDataSource
import org.vander.spotifyclient.domain.playlist.repository.IPlaylistRepository
import javax.inject.Inject

class PlaylistRepository @Inject constructor(
    private val remoteDataSource: IPlaylistRemoteDataSource
) : IPlaylistRepository {

    override suspend fun getUserPlaylists(): Result<SpotifyPlaylistsResponse> {
        return remoteDataSource.fetchUserPlaylists().map { it.toDomain() }
    }
}