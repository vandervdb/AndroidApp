package org.vander.spotifyclient.data.local

import org.vander.spotifyclient.domain.auth.IDataStoreManager
import org.vander.spotifyclient.domain.auth.ITokenProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreTokenProvider @Inject constructor(
    val dataStoreManager: IDataStoreManager
) : ITokenProvider {
    override suspend fun getAccessToken(): String? {
        return dataStoreManager.accessTokenFlow.toString()
    }
}
