package org.vander.spotifyclient.data.client.player.domain

import kotlinx.coroutines.flow.StateFlow
import org.vander.spotifyclient.domain.state.PlayerState
import org.vander.spotifyclient.domain.state.PlayerStateData

interface ISpotifyPlayerClient {
    val playerState: StateFlow<PlayerState>
    suspend fun subscribeToPlayerState(function: (PlayerStateData) -> Unit)
    suspend fun play(trackUri: String)
    suspend fun pause(): Result<Unit>
    suspend fun resume(): Result<Unit>
    suspend fun skipNext(): Result<Unit>
    suspend fun skipPrevious(): Result<Unit>
    fun seek(position: Int)
    fun setVolume(volume: Int)
    fun setShuffle(shuffle: Boolean)
    fun setRepeat(repeat: Boolean)
    fun isPlaying(): Boolean
}