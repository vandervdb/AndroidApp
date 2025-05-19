package org.vander.spotifyclient.data.repository

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.vander.spotifyclient.domain.player.ISpotifyPlayerClient
import org.vander.spotifyclient.domain.player.repository.IPlayerStateRepository
import org.vander.spotifyclient.domain.state.PlayerStateData
import org.vander.spotifyclient.domain.state.SavedRemotelyChangedState
import javax.inject.Inject

class DefaultPlayerStateRepository @Inject constructor(
    private val playerClient: ISpotifyPlayerClient
) : IPlayerStateRepository {

    companion object {
        private const val TAG = "DefaultPlayerStateRepository"
    }

    private val _playerStateData = MutableStateFlow(PlayerStateData.Companion.empty())
    override val playerStateData: StateFlow<PlayerStateData> = _playerStateData.asStateFlow()

    private val _savedRemotelyChangedState = MutableStateFlow<SavedRemotelyChangedState>(SavedRemotelyChangedState())
    override val savedRemotelyChangedState: StateFlow<SavedRemotelyChangedState> =
        _savedRemotelyChangedState.asStateFlow()

    private var isListening = false

    override suspend fun startListening() {
        if (isListening) return
        isListening = true
        playerClient.subscribeToPlayerState() { newState ->
            if (newState == _playerStateData.value) {
                Log.d(TAG, "Player state did not change -> saved status changed")
                _savedRemotelyChangedState.update { SavedRemotelyChangedState(true, newState.trackId) }
                _savedRemotelyChangedState.update { SavedRemotelyChangedState(false) }  // reset
            }
            _playerStateData.update { newState }
        }
    }

    override suspend fun stopListening() {
        isListening = false
    }

}