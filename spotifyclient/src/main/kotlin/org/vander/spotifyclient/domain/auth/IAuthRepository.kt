package org.vander.spotifyclient.domain.auth

interface IAuthRepository {
    suspend fun getAccessToken(code: String): Result<String>
    suspend fun storeAccessToken(token: String)
}