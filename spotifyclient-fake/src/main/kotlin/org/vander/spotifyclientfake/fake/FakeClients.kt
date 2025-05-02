package org.vander.spotifyclientfake.fake

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import kotlinx.coroutines.delay
import org.vander.spotifyclient.data.client.auth.ISpotifyAuthClient
import org.vander.spotifyclient.data.client.player.ISpotifyPlayerClient
import org.vander.spotifyclient.data.client.remote.ISpotifyRemoteClient
import org.vander.spotifyclient.domain.state.PlayerStateData

class FakeSpotifyAuthClient(
    private val shouldSucceed: Boolean = true
) : ISpotifyAuthClient {

    override fun authorize(
        activity: Activity,
        launcher: ActivityResultLauncher<Intent>
    ) {
        // no real intent
    }

    override fun handleSpotifyAuthResult(
        result: ActivityResult,
        onResult: (Result<String>) -> Unit
    ) {
        if (shouldSucceed) {
            onResult(Result.success("Success"))
        } else {
            onResult(Result.failure(Exception("Fake auth failure")))
        }
    }
}

class FakeSpotifyRemoteClient(
    private val shouldSucceed: Boolean = true
) : ISpotifyRemoteClient {

    override suspend fun connect(context: Context): Result<Unit> {
        return if (shouldSucceed) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Fake connection failed"))
        }
    }

    override fun getRemote(): SpotifyAppRemote {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        // nothing to do
    }
}

class FakeSpotifyPlayerClient : ISpotifyPlayerClient {

    private var isPlaying = true
    private val fakeTrack = createFakeTrack()
    private val fakePlayerState = createFakePlayerStateData(fakeTrack)

    override suspend fun subscribeToPlayerState(onUpdate: (PlayerStateData) -> Unit) {
        delay(100) // simulate async delay
        onUpdate(fakePlayerState.copy(playing = isPlaying, isPaused = !isPlaying))
    }

    override suspend fun play(trackUri: String) {
        isPlaying = true
    }

    override suspend fun pause(): Result<Unit> {
        isPlaying = false
        return Result.success(Unit)
    }

    override suspend fun resume(): Result<Unit> {
        isPlaying = true
        return Result.success(Unit)
    }

    override suspend fun skipNext(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun skipPrevious(): Result<Unit> {
        return Result.success(Unit)
    }

    override fun seek(position: Int) {
        // simulate seeking
    }

    override fun setVolume(volume: Int) {
        // simulate volume set
    }

    override fun setShuffle(shuffle: Boolean) {
        // simulate shuffle toggle
    }

    override fun setRepeat(repeat: Boolean) {
        // simulate repeat toggle
    }

    override fun isPlaying(): Boolean = isPlaying
}


fun createFakePlayerStateData(track: Track): PlayerStateData {
    return PlayerStateData(
        trackName = track.name ?: "Fake Track",
        artistName = track.artist?.name ?: "Fake Artist",
        isPaused = false,
        playing = true,
        playingTrack = track,
        paused = false,
        stopped = false,
        shuffling = false,
        repeating = false,
        seeking = false,
        skippingNext = false,
        skippingPrevious = false
    )
}


