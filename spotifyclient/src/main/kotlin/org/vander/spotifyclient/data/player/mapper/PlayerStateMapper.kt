package org.vander.spotifyclient.data.player.mapper

import android.util.Log
import com.spotify.protocol.types.PlayerState
import org.vander.spotifyclient.domain.state.PlayerStateData

fun PlayerState.toPlayerStateData(): PlayerStateData {
    val track = this.track
    Log.d("momo", "track: ${track.imageUri.toString().extractSpotifyTrackIdOrNull()}")
    return PlayerStateData(
        trackName = track?.name ?: "Unknown Track",
        artistName = track?.artist?.name ?: "Unknown Artist",
        trackId = track.imageUri.toString().extractSpotifyTrackIdOrNull() ?: "",
        isPaused = isPaused,
        playing = !isPaused,
        paused = isPaused,
        stopped = false,
        shuffling = playbackOptions?.isShuffling ?: false,
        repeating = playbackOptions?.repeatMode == 1, // 1 = repeat context, 2 = repeat track
        seeking = false,
        skippingNext = playbackRestrictions?.canSkipNext == false,
        skippingPrevious = playbackRestrictions?.canSkipPrev == false,
    )
}

fun String.extractSpotifyTrackIdOrNull(): String? {
    return if (startsWith("ImageId{spotify:image:")) {
        substringAfter("ImageId{spotify:image:")
            .substringBefore("'}")
    } else null
}