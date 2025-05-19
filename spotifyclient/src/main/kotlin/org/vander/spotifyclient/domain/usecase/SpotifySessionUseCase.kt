package org.vander.spotifyclient.domain.usecase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.vander.spotifyclient.domain.appremote.ISpotifyAppRemoteProvider
import org.vander.spotifyclient.domain.auth.IAuthRepository
import org.vander.spotifyclient.domain.auth.ISpotifyAuthClient
import org.vander.spotifyclient.domain.error.SpotifySessionError
import org.vander.spotifyclient.domain.state.SpotifySessionState
import javax.inject.Inject

class SpotifySessionUseCase @Inject constructor(
    private val authClient: ISpotifyAuthClient,
    private val remoteProvider: ISpotifyAppRemoteProvider,
    private val authRepository: IAuthRepository,
) {
    companion object {
        private const val TAG = "SpotifySessionUseCase"
    }

    val dispatcher = Dispatchers.Main

    private val _sessionState = MutableStateFlow<SpotifySessionState>(SpotifySessionState.Idle)
    val sessionState: StateFlow<SpotifySessionState> = _sessionState.asStateFlow()

    private var launchAuthFlow: ActivityResultLauncher<Intent>? = null


    fun requestAuthorization(launchAuth: ActivityResultLauncher<Intent>) {
        Log.d(TAG, "Requesting authorization...")
        launchAuthFlow = launchAuth
        _sessionState.update { SpotifySessionState.Authorizing }
    }

    fun launchAuthorizationFlow(activity: Activity) {
        Log.d(TAG, "Launching authorization flow...")
        try {
            launchAuthFlow?.let {
                authClient.authorize(activity, it)
            } ?: run {
                _sessionState.update {
                    SpotifySessionState.Failed(
                        SpotifySessionError.UnknownError(Exception("Authorization flow not set"))
                    )
                }
            }
        } catch (e: Exception) {
            _sessionState.update { SpotifySessionState.Failed(SpotifySessionError.UnknownError(e)) }
        }
    }

    fun handleAuthResult(
        context: Context,
        result: ActivityResult,
        coroutineScope: CoroutineScope
    ) {
        authClient.handleSpotifyAuthResult(result) { authResult ->
            if (authResult.isSuccess) {
                coroutineScope.launch {
                    val authCode = authResult.getOrElse { "" }
                    Log.d(TAG, "Launching auth token request...")
                    fetchAndStoreAuthToken(authCode)
                        .onFailure { error ->
                            Log.e(TAG, "Error storing access token", error)
                            _sessionState.update {
                                SpotifySessionState.Failed(
                                    SpotifySessionError.AuthFailed(error)
                                )
                            }
                        }
                        .onSuccess {
                            Log.d(TAG, "Access token stored, Connecting to remote...")
                            connectRemote(context, coroutineScope)

                        }
                }
            } else {
                _sessionState.update {
                    SpotifySessionState.Failed(
                        SpotifySessionError.AuthFailed(Exception("Authorization failed with unknown error"))
                    )
                }
            }
        }
    }

    suspend fun shutDown() {
        remoteProvider.disconnect()
        _sessionState.update { SpotifySessionState.Idle }
        authRepository.clearAccessToken()
    }

    private fun connectRemote(
        context: Context,
        coroutineScope: CoroutineScope
    ) {
        _sessionState.update { SpotifySessionState.ConnectingRemote }
        coroutineScope.launch(dispatcher) {
            val result = remoteProvider.connect(context)
            if (result.isSuccess) {
                Log.d(TAG, "SpotifySessionState.Ready")
                _sessionState.update { SpotifySessionState.Ready }
            } else {
                Log.e(TAG, "Failed to connect to remote", result.exceptionOrNull())
                _sessionState.update {
                    SpotifySessionState.Failed(
                        SpotifySessionError.RemoteConnectionFailed(result.exceptionOrNull())
                    )
                }
            }
        }
    }

    private suspend fun fetchAndStoreAuthToken(authCode: String): Result<Unit> =
        authRepository.storeAccessToken(authCode)

}