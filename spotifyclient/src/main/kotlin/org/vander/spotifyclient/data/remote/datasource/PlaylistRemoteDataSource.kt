package org.vander.spotifyclient.data.remote.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import org.vander.spotifyclient.data.playlist.dto.SpotifyPlaylistsResponseDto
import org.vander.spotifyclient.domain.datasource.IPlaylistRemoteDataSource
import javax.inject.Inject
import javax.inject.Named

class PlaylistRemoteDataSource @Inject constructor(
    @Named("PlaylistHttpClient") private val httpClient: HttpClient
) : IPlaylistRemoteDataSource {

    override suspend fun fetchUserPlaylists(token: String): Result<SpotifyPlaylistsResponseDto> {
        return try {
            val response: SpotifyPlaylistsResponseDto =
                httpClient.get("playlists") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $token")
                    }
                }.body()

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}