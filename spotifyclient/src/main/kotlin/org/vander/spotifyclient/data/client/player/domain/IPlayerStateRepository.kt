package org.vander.spotifyclient.data.client.player.domain

import kotlinx.coroutines.flow.StateFlow
import org.vander.spotifyclient.domain.state.PlayerStateData

interface IPlayerStateRepository {
    val playerStateData: StateFlow<PlayerStateData>
    suspend fun startListening()
    suspend fun stopListening()
}