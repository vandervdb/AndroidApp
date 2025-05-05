package org.vander.spotifyclient.data.client.player.data

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.vander.spotifyclient.data.client.player.domain.IPlayerStateRepository
import org.vander.spotifyclient.data.client.player.domain.ISpotifyPlayerClient
import org.vander.spotifyclient.domain.state.PlayerStateData
import javax.inject.Inject

class DefaultPlayerStateRepository @Inject constructor(
    private val playerClient: ISpotifyPlayerClient
) : IPlayerStateRepository {

    companion object {
        private const val TAG = "DefaultPlayerStateRepository"
    }

    private val _playerStateData = MutableStateFlow(PlayerStateData.empty())
    override val playerStateData: StateFlow<PlayerStateData> = _playerStateData.asStateFlow()

    private var isListening = false

    override suspend fun startListening() {
        if (isListening) return
        isListening = true
        playerClient.subscribeToPlayerState() { newState ->
            Log.d(TAG, "Player state: $newState")
            _playerStateData.value = newState
        }
    }

    override suspend fun stopListening() {
        isListening = false

    }
}