package org.vander.spotifyclient.data.repository

import android.util.Log
import org.vander.spotifyclient.data.local.DataStoreManager
import org.vander.spotifyclient.data.remote.datasource.AuthRemoteDataSource
import org.vander.spotifyclient.domain.auth.IAuthRepository
import org.vander.spotifyclient.domain.auth.IDataStoreManager
import javax.inject.Inject

class AutRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val dataStoreManager: IDataStoreManager
) : IAuthRepository {

    companion object {
        private const val TAG = "AuthRepository"
    }

    override suspend fun getAccessToken(authCode: String): Result<String> {
        return authRemoteDataSource.fetchAccessToken(authCode)
            .onSuccess { storeAccessToken(it.accessToken) }
            .onFailure {
                Log.e(
                    "AuthRepository",
                    "Erreur lors de la récupération du token",
                    it
                )
            }
            .map { it.accessToken }
    }

    override suspend fun storeAccessToken(token: String) {
        dataStoreManager.saveAccessToken(token)
    }

    override suspend fun clearAccessToken() {
        dataStoreManager.clearAccessToken()
    }

}
