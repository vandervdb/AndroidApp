package org.vander.spotifyclient.domain.auth

import kotlinx.coroutines.flow.Flow

interface IDataStoreManager {
    val accessTokenFlow: Flow<String?>
    suspend fun saveAccessToken(token: String)
    suspend fun clearAccessToken()
}