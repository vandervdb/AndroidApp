package org.vander.spotifyclient.data.remote.datasource


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import org.vander.spotifyclient.BuildConfig.CLIENT_ID
import org.vander.spotifyclient.BuildConfig.CLIENT_SECRET
import org.vander.spotifyclient.data.remote.dto.SpotifyTokenResponseDto
import org.vander.spotifyclient.domain.auth.IAuthRemoteDatasource
import javax.inject.Inject
import javax.inject.Named
import kotlin.io.encoding.ExperimentalEncodingApi

class AuthRemoteDataSource @Inject constructor(
    @Named("AuthHttpClient") val httpClient: HttpClient
) : IAuthRemoteDatasource {
    @OptIn(ExperimentalEncodingApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun fetchAccessToken(code: String): Result<SpotifyTokenResponseDto> {
        return try {
            Log.d("AuthRemoteDataSource", "Fetching token with code: $code")
            val credentials = "$CLIENT_ID:$CLIENT_SECRET"
            val encodedCredentials = kotlin.io.encoding.Base64.encode(credentials.toByteArray())

            val response = httpClient.submitForm(
                url = "token",
                formParameters = Parameters.build {
                    append("grant_type", "client_credentials")
                }
            ) {
                headers {
                    append("Authorization", "Basic $encodedCredentials")
                }
            }

            val rawBody = response.bodyAsText()
            Log.d("AuthRemoteDataSource", "Raw body: $rawBody")
            val responseDto = response.body<SpotifyTokenResponseDto>()
            Result.success(responseDto)
        } catch (e: Exception) {
            Log.e("AuthRemoteDataSource", "Error fetching token", e)
            Result.failure(e)
        }
    }
}