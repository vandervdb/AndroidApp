package org.vander.spotifyclient.data.client.player

import org.vander.spotifyclient.domain.state.PlayerStateData

interface ISpotifyPlayerClient {
    suspend fun subscribeToPlayerState(onUpdate: (PlayerStateData) -> Unit)
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