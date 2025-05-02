package org.vander.spotifyclient.data.client.player

import android.util.Log
import com.spotify.android.appremote.api.SpotifyAppRemote
import org.vander.spotifyclient.domain.state.PlayerStateData
import javax.inject.Inject

class SpotifyPlayerClient @Inject constructor() : ISpotifyPlayerClient {

    companion object {
        const val TAG = "SpotifyPlayerClient"
    }

    private var spotifyAppRemote: SpotifyAppRemote? = null
    override suspend fun subscribeToPlayerState(onUpdate: (PlayerStateData) -> Unit) {
        TODO("Not yet implemented")
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