package org.vander.spotifyclient.data.remote.datasource


import org.vander.spotifyclient.BuildConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import org.vander.spotifyclient.data.remote.dto.SpotifyTokenResponseDto
import org.vander.spotifyclient.utils.*
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun fetchAccessToken(code: String): Result<SpotifyTokenResponseDto> {
        return try {
            val response = httpClient.submitForm(
                url = "https://accounts.spotify.com/api/token",
                formParameters = Parameters.build {
                    append("grant_type", "authorization_code")
                    append("code", code)
                    append("redirect_uri", REDIRECT_URI)
                    append("client_id", BuildConfig.CLIENT_ID)
                    append("client_secret", BuildConfig.CLIENT_SECRET)
                }
            ).body<SpotifyTokenResponseDto>()

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}