package org.vander.spotifyclient.data.player

import android.util.Log
import com.spotify.android.appremote.api.PlayerApi
import com.spotify.protocol.types.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.vander.spotifyclient.data.player.mapper.toPlayerStateData
import org.vander.spotifyclient.domain.appremote.ISpotifyAppRemoteProvider
import org.vander.spotifyclient.domain.player.ISpotifyPlayerClient
import org.vander.spotifyclient.domain.state.PlayerConnectionState
import org.vander.spotifyclient.domain.state.PlayerStateData
import javax.inject.Inject

class SpotifyPlayerClient @Inject constructor(
    private val appRemoteProvider: ISpotifyAppRemoteProvider,
) : ISpotifyPlayerClient {

    companion object {
        const val TAG = "SpotifyPlayerClient"
    }

    private var isPlaying = false
    private val playerApi: PlayerApi?
        get() = appRemoteProvider.get()?.playerApi


    private val _playerConnectionState =
        MutableStateFlow<PlayerConnectionState>(PlayerConnectionState.NotConnected)
    override val playerConnectionState: StateFlow<PlayerConnectionState> =
        _playerConnectionState.asStateFlow()


    override suspend fun subscribeToPlayerState(function: (PlayerStateData) -> Unit) {
        playerApi?.let {
            it.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                Log.d(
                    TAG,
                    "PlayerClient received new data" + track.name + " by " + track.artist.name +
                            "(paused: " + it.isPaused + " / coverUri: " + track.imageUri + ")"
                )
                isPlaying = !it.isPaused
                function(it.toPlayerStateData())
            }
        } ?: run {
            Log.e(TAG, "spotifyPlayerApi is null")
            _playerConnectionState.value = PlayerConnectionState.NotConnected
        }
    }

    override suspend fun play(trackUri: String) {
        Log.d(TAG, "play: ")
        playerApi?.play(trackUri)
    }

    override suspend fun pause() {
        playerApi?.pause()
    }

    override suspend fun resume() {
        Log.d(TAG, "resume: ")
        playerApi?.resume()
    }

    override suspend fun skipNext() {
        playerApi?.skipNext()
    }

    override suspend fun skipPrevious() {
        playerApi?.skipPrevious()
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
        return isPlaying
    }

}