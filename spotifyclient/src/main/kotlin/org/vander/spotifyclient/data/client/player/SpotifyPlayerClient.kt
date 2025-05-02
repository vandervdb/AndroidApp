package org.vander.spotifyclient.data.client.player

import android.util.Log
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import org.vander.spotifyclient.data.mapper.toPlayerStateData
import org.vander.spotifyclient.domain.state.PlayerStateData
import javax.inject.Inject

class SpotifyPlayerClient @Inject constructor() : ISpotifyPlayerClient {

    companion object {
        const val TAG = "SpotifyPlayerClient"
    }

    private var spotifyAppRemote: SpotifyAppRemote? = null
    private lateinit var onPlayerEvent: (PlayerStateData) -> Unit

    override fun registerAppRemotePlayerState(remote: SpotifyAppRemote, onPlayerEvent: (PlayerStateData) -> Unit) {
        this.spotifyAppRemote = remote
        this.onPlayerEvent = onPlayerEvent
    }

    override suspend fun subscribeToPlayerState() {
        spotifyAppRemote?.let {
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                Log.d(
                    TAG,
                    track.name + " by " + track.artist.name + "(paused: " + it.isPaused + " / coverUri: " + track.imageUri + ")"
                )
                val playerStateData = it.toPlayerStateData()
                Log.d(TAG, "Player state: $playerStateData")
                onPlayerEvent(playerStateData)
            }
        }
    }

    override suspend fun play(trackUri: String) {
        Log.d(TAG, "play: ")
        spotifyAppRemote?.playerApi?.play(trackUri)
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