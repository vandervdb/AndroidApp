package org.vander.spotifyclient.data.remote.datasource

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import org.vander.spotifyclient.data.playlist.dto.SpotifyPlaylistDto
import org.vander.spotifyclient.data.playlist.dto.SpotifyPlaylistsResponseDto
import org.vander.spotifyclient.data.remote.dto.CurrentlyPlayingWithQueueDto
import org.vander.spotifyclient.domain.auth.ITokenProvider
import org.vander.spotifyclient.domain.datasource.ISpotifyRemoteDataSource
import org.vander.spotifyclient.utils.parseSpotifyResult
import javax.inject.Inject
import javax.inject.Named

class SpotifyRemoteDataSource @Inject constructor(
    @Named("PlaylistHttpClient") private val httpClient: HttpClient,
    private val tokenProvider: ITokenProvider
) : ISpotifyRemoteDataSource {

    override suspend fun fetchUserQueue(): Result<CurrentlyPlayingWithQueueDto> {
        return try {
            val token = getAccessToken()
            val response = httpClient.get("player/queue") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            val rawBody = response.bodyAsText()
            val json = Json { ignoreUnknownKeys = true }

            return response.parseSpotifyResult<CurrentlyPlayingWithQueueDto>("SpotifyRemoteDataSource")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchUserPlaylists(): Result<SpotifyPlaylistsResponseDto> {
        return try {
            val token = getAccessToken()
            val response = httpClient.get("playlists") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            val rawBody = response.bodyAsText()
            val json = Json { ignoreUnknownKeys = true }

            return response.parseSpotifyResult<SpotifyPlaylistsResponseDto>("SpotifyRemoteDataSource")

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchPlaylist(
        playlistId: String
    ): Result<SpotifyPlaylistDto> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchIsTrackSaved(trackId: String): Result<Boolean> {
        return try {
            val token = getAccessToken()
            val response = httpClient.get("tracks/contains") {
                url {
                    parameters.append("ids", trackId)
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            val rawBody = response.bodyAsText()
            val json = Json { ignoreUnknownKeys = true }

            return Result.success(true)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private suspend fun getAccessToken(): String {
        return tokenProvider.getAccessToken() ?: ""
    }
}