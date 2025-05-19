package org.vander.spotifyclient.data.remote.datasource

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.put
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
    @Named("SpotifyRemoteHttpClient") private val httpClient: HttpClient,
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
            val isSavedList = json.decodeFromString<List<Boolean>>(rawBody)
            val isSaved = isSavedList.firstOrNull() == true

            return Result.success(isSaved)

        } catch (e: Exception) {
            Log.e("SpotifyRemoteDataSource", "Error fetching is track saved", e)
            Result.failure(e)
        }
    }

    override suspend fun saveTrackForCurrentUser(trackId: String): Result<Unit> {
        return try {
            val token = getAccessToken()
            val response = httpClient.put("tracks") {
                url {
                    parameters.append("ids", trackId)
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            return Result.success(Unit)

        } catch (e: Exception) {
            Log.e("SpotifyRemoteDataSource", "Error saving track $trackId", e)
            Result.failure(e)
        }
    }

    override suspend fun removeTrackForCurrentUser(trackId: String): Result<Unit> {
        return try {
            val token = getAccessToken()
            val response = httpClient.delete("tracks") {
                url {
                    parameters.append("ids", trackId)
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            return Result.success(Unit)

        } catch (e: Exception) {
            Log.e("SpotifyRemoteDataSource", "Error removing track $trackId from saved tracks", e)
            Result.failure(e)
        }
    }


    private suspend fun getAccessToken(): String {
        return tokenProvider.getAccessToken() ?: ""
    }
}