package org.vander.spotifyclient.domain.auth

import org.vander.spotifyclient.data.remote.dto.SpotifyTokenResponseDto

interface IAuthRemoteDatasource {
    suspend fun fetchAccessToken(code: String): Result<SpotifyTokenResponseDto>
}