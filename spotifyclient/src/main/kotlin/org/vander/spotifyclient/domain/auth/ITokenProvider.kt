package org.vander.spotifyclient.domain.auth

interface ITokenProvider {
    suspend fun getAccessToken(): String?
}