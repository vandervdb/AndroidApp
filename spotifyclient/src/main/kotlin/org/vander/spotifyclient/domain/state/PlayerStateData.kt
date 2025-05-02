package org.vander.spotifyclient.domain.state

import com.spotify.protocol.types.Track

data class PlayerStateData(
    val trackName: String,
    val artistName: String,
    val isPaused: Boolean,
    val playing: Boolean,
    val playingTrack: Track,
    val paused: Boolean,
    val stopped: Boolean,
    val shuffling: Boolean,
    val repeating: Boolean,
    val seeking: Boolean,
    val skippingNext: Boolean,
    val skippingPrevious: Boolean,
)