package org.vander.spotifyclient.domain.auth

import kotlinx.coroutines.flow.Flow

interface ITokenProvider {
    val tokenFlow: Flow<String?>
    suspend fun getAccessToken(): String?
}