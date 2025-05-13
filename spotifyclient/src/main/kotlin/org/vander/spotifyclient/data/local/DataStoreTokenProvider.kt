package org.vander.spotifyclient.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.vander.spotifyclient.domain.auth.IDataStoreManager
import org.vander.spotifyclient.domain.auth.ITokenProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreTokenProvider @Inject constructor(
    val dataStoreManager: IDataStoreManager
) : ITokenProvider {

    override val tokenFlow: Flow<String?>
        get()  = dataStoreManager.accessTokenFlow

    override suspend fun getAccessToken(): String? {
        return dataStoreManager.accessTokenFlow.firstOrNull()
    }
}
