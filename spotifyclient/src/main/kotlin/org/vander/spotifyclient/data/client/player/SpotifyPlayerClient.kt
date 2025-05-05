package org.vander.spotifyclient.data.client.player

import android.util.Log
import com.spotify.android.appremote.api.PlayerApi
import com.spotify.protocol.types.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.vander.spotifyclient.data.client.player.domain.ISpotifyPlayerClient
import org.vander.spotifyclient.data.client.remote.ISpotifyAppRemoteProvider
import org.vander.spotifyclient.data.mapper.toPlayerStateData
import org.vander.spotifyclient.domain.state.PlayerState
import org.vander.spotifyclient.domain.state.PlayerStateData
import javax.inject.Inject

class SpotifyPlayerClient @Inject constructor(
    private val appRemoteProvider: ISpotifyAppRemoteProvider
) : ISpotifyPlayerClient {

    companion object {
        const val TAG = "SpotifyPlayerClient"
    }

    private val playerApi: PlayerApi?
        get() = appRemoteProvider.get()?.playerApi
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.NotConnected)
    override val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    override suspend fun subscribeToPlayerState(function: (PlayerStateData) -> Unit) {
        playerApi?.let {
            it.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                Log.d(
                    TAG,
                    track.name + " by " + track.artist.name + "(paused: " + it.isPaused + " / coverUri: " + track.imageUri + ")"
                )
                val playerStateData = it.toPlayerStateData()
                Log.d(TAG, "Player state: $playerStateData")
                function(playerStateData)
            }
        } ?: run {
            Log.e(TAG, "spotifyPlayerApi is null")
            _playerState.value = PlayerState.NotConnected
        }
    }

    override suspend fun play(trackUri: String) {
        Log.d(TAG, "play: ")
        playerApi?.play(trackUri)
    }

    override suspend fun pause(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun resume(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun skipNext(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun skipPrevious(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun seek(position: Int) {
        TODO("Not yet implemented")
    }

    override fun setVolume(volume: Int) {
        TODO("Not yet implemented")
    }

    override fun setShuffle(shuffle: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setRepeat(repeat: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isPlaying(): Boolean {
        TODO("Not yet implemented")
    }
}