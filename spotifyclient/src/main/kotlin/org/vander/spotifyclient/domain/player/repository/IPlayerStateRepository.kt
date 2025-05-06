package org.vander.spotifyclient.domain.player.repository

import kotlinx.coroutines.flow.StateFlow
import org.vander.spotifyclient.domain.state.PlayerStateData

interface IPlayerStateRepository {
    val playerStateData: StateFlow<PlayerStateData>
    suspend fun startListening()
    suspend fun stopListening()
}