package org.vander.spotifyclient.usecase

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
import org.vander.spotifyclient.data.client.player.ISpotifyPlayerClient
import org.vander.spotifyclient.data.client.remote.ISpotifyRemoteClient
import org.vander.spotifyclient.data.model.PlayerStateData
import org.vander.spotifyclient.data.model.SpotifySessionState
import javax.inject.Inject

class SpotifySessionUseCase @Inject constructor(
    private val authClient: ISpotifyAuthClient,
    private val remoteClient: ISpotifyRemoteClient,
    private val playerClient: ISpotifyPlayerClient,
) {
    private val _sessionState = MutableStateFlow<SpotifySessionState>(SpotifySessionState.Idle)
    val sessionState: StateFlow<SpotifySessionState> = _sessionState.asStateFlow()

    private var pendingLauncher: ActivityResultLauncher<Intent>? = null

    fun requestAuthorization(launcher: ActivityResultLauncher<Intent>) {
        pendingLauncher = launcher
        _sessionState.value = SpotifySessionState.Authorizing
    }

    fun launchAuthorizationFlow(activity: Activity) {
        try {
            pendingLauncher?.let { authClient.authorize(activity, it) }
        } catch (e: Exception) {
            _sessionState.value = SpotifySessionState.Failed(e)
        }
    }

    fun handleAuthResult(
        context: Context,
        result: ActivityResult,
        coroutineScope: CoroutineScope
    ) {
        authClient.handleSpotifyAuthResult(result) { authResult ->
            if (authResult.isSuccess) {
                connectRemote(context, coroutineScope)
            } else {
                _sessionState.value = SpotifySessionState.Failed(
                    authResult.exceptionOrNull() ?: Exception("Unknown error")
                )
            }
        }
    }

    private fun connectRemote(
        context: Context,
        coroutineScope: CoroutineScope
    ) {
        _sessionState.value = SpotifySessionState.ConnectingRemote
        coroutineScope.launch(Dispatchers.Main) {
            val result = remoteClient.connect(context)
            _sessionState.value = if (result.isSuccess) SpotifySessionState.Ready
            else SpotifySessionState.Failed(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun play(trackUri: String) {
        playerClient.play(trackUri)
    }

    suspend fun pause() {
        playerClient.pause()
    }

    suspend fun togglePlayPause() {
        if (playerClient.isPlaying()) {
            pause()
        } else {
            //resume()
        }
    }

    suspend fun subscribeToPlayerState(onUpdate: (PlayerStateData) -> Unit) {
        playerClient.subscribeToPlayerState(onUpdate)
    }

    fun disconnect() {
        remoteClient.disconnect()
        _sessionState.value = SpotifySessionState.Idle
    }
}
