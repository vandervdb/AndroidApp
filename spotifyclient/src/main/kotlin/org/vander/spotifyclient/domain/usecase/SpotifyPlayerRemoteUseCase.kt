package org.vander.spotifyclient.domain.usecase

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.vander.spotifyclient.data.client.player.ISpotifyPlayerClient
import org.vander.spotifyclient.data.client.remote.ISpotifyRemoteClient
import org.vander.spotifyclient.domain.state.PlayerStateData
import org.vander.spotifyclient.domain.state.SpotifyRemoteClientState
import javax.inject.Inject

class SpotifyPlayerRemoteUseCase @Inject constructor(
    private val playerClient: ISpotifyPlayerClient,
    private val remoteClient: ISpotifyRemoteClient
) {

    private val _playerState = MutableStateFlow<PlayerStateData>(PlayerStateData.empty())
    val playerState: StateFlow<PlayerStateData> = _playerState.asStateFlow()

    private val onPlayerStateEvent = { state: PlayerStateData ->
        Log.d(TAG, "Player state: $state")
        _playerState.value = state
    }

    companion object {
        const val TAG = "SpotifyPlayerRemoteUseCase"
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            remoteClient.remoteState.collect { state ->
                Log.d(TAG, "Remote state: $state")
                when (state) {
                    is SpotifyRemoteClientState.Connected -> {
                        playerClient.registerAppRemotePlayerState(state.appRemote, onPlayerStateEvent)
                        playerClient.subscribeToPlayerState()
                    }

                    is SpotifyRemoteClientState.Failed -> {
                        // Handle connection failure
                    }

                    else -> {
                        // TODO: Handle other states
                    }
                }
            }
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
}