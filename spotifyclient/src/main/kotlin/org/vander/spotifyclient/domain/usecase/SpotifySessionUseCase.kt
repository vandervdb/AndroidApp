package org.vander.spotifyclient.domain.usecase

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.vander.spotifyclient.data.client.auth.ISpotifyAuthClient
import org.vander.spotifyclient.data.client.remote.ISpotifyRemoteClient
import org.vander.spotifyclient.domain.error.SpotifySessionError
import org.vander.spotifyclient.domain.state.SpotifySessionState
import javax.inject.Inject

class SpotifySessionUseCase @Inject constructor(
    private val authClient: ISpotifyAuthClient,
    private val remoteClient: ISpotifyRemoteClient
) {
    val dispatcher = Dispatchers.Main

    private val _sessionState = MutableStateFlow<SpotifySessionState>(SpotifySessionState.Idle)
    val sessionState: StateFlow<SpotifySessionState> = _sessionState.asStateFlow()

    private var launchAuthFlow: ActivityResultLauncher<Intent>? = null

    fun requestAuthorization(launchAuth: ActivityResultLauncher<Intent>) {
        launchAuthFlow = launchAuth
        _sessionState.value = SpotifySessionState.Authorizing
    }

    fun launchAuthorizationFlow(activity: Activity) {
        try {
            launchAuthFlow?.let {
                authClient.authorize(activity, it)
            } ?: run {
                _sessionState.value = SpotifySessionState.Failed(
                    SpotifySessionError.UnknownError(Exception("Authorization flow not set"))
                )
            }
        } catch (e: Exception) {
            _sessionState.value = SpotifySessionState.Failed(SpotifySessionError.UnknownError(e))
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
                    connectRemote(context, coroutineScope)
                }
            } else {
                _sessionState.value = SpotifySessionState.Failed(
                    SpotifySessionError.AuthFailed(Exception("Authorization failed with unknown error"))
                )
            }
        }
    }

    fun disconnect() {
        remoteClient.disconnect()
        _sessionState.value = SpotifySessionState.Idle
    }

    private fun connectRemote(
        context: Context,
        coroutineScope: CoroutineScope
    ) {
        _sessionState.value = SpotifySessionState.ConnectingRemote
        coroutineScope.launch(dispatcher) {
            val result = remoteClient.connect(context)
            if (result.isSuccess) {
                _sessionState.value = SpotifySessionState.Ready
            } else {
                _sessionState.value = SpotifySessionState.Failed(
                    SpotifySessionError.RemoteConnectionFailed(result.exceptionOrNull())
                )
            }
        }
    }
}