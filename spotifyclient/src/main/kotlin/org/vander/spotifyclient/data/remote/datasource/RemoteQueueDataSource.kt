package org.vander.spotifyclient.data.remote.datasource

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import org.vander.spotifyclient.data.remote.dto.CurrentlyPlayingWithQueueDto
import org.vander.spotifyclient.domain.auth.ITokenProvider
import org.vander.spotifyclient.domain.datasource.IRemoteQueueDataSource
import org.vander.spotifyclient.utils.parseSpotifyResult
import javax.inject.Inject
import javax.inject.Named


class RemoteQueueDataSource @Inject constructor(
    @Named("SpotifyRemoteQueueHttpClient") private val httpClient: HttpClient,
    private val tokenProvider: ITokenProvider
) : IRemoteQueueDataSource {

    override suspend fun fetchUserQueue(): Result<CurrentlyPlayingWithQueueDto> {
        val token = tokenProvider.getAccessToken() ?: ""
        return try {
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
}
