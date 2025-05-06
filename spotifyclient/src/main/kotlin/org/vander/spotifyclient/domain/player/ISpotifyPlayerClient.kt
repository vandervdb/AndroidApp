package org.vander.spotifyclient.domain.player

import kotlinx.coroutines.flow.StateFlow
import org.vander.spotifyclient.domain.state.PlayerConnectionState
import org.vander.spotifyclient.domain.state.PlayerStateData

interface ISpotifyPlayerClient {
    val playerConnectionState: StateFlow<PlayerConnectionState>
    suspend fun subscribeToPlayerState(function: (PlayerStateData) -> Unit)
    suspend fun play(trackUri: String)
    suspend fun pause()
    suspend fun resume()
    suspend fun skipNext(): Result<Unit>
    suspend fun skipPrevious(): Result<Unit>
    fun seek(position: Int)
    fun setVolume(volume: Int)
    fun setShuffle(shuffle: Boolean)
    fun setRepeat(repeat: Boolean)
    fun isPlaying(): Boolean
}