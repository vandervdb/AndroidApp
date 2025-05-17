package org.vander.spotifyclient.domain.player.repository

import kotlinx.coroutines.flow.StateFlow
import org.vander.spotifyclient.domain.state.PlayerStateData
import org.vander.spotifyclient.domain.state.SavedRemotelyChangedState

interface IPlayerStateRepository {
    val playerStateData: StateFlow<PlayerStateData>
    val savedRemotelyChangedState: StateFlow<SavedRemotelyChangedState>
    suspend fun startListening()
    suspend fun stopListening()
}