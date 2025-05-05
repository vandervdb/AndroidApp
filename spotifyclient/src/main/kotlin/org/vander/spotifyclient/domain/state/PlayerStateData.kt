package org.vander.spotifyclient.domain.state

data class PlayerStateData(
    val trackName: String,
    val artistName: String,
    val trackId: String,
    val isPaused: Boolean,
    val playing: Boolean,
    val paused: Boolean,
    val stopped: Boolean,
    val shuffling: Boolean,
    val repeating: Boolean,
    val seeking: Boolean,
    val skippingNext: Boolean,
    val skippingPrevious: Boolean,
) {
    companion object {
        fun empty(): PlayerStateData {
            return PlayerStateData(
                trackName = "",
                artistName = "",
                trackId = "",
                isPaused = true,
                playing = false,
                paused = true,
                stopped = true,
                shuffling = false,
                repeating = false,
                seeking = false,
                skippingNext = false,
                skippingPrevious = false
            )
        }
    }

    override fun toString(): String {
        return "PlayerStateData(" +
                "trackName='$trackName', " +
                "artistName='$artistName', " +
                "trackId='$trackId', " +
                "isPaused=$isPaused, " +
                "playing=$playing, " +
                "paused=$paused, " +
                "stopped=$stopped, " +
                "shuffling=$shuffling, " +
                "repeating=$repeating, " +
                "seeking=$seeking, " +
                "skippingNext=$skippingNext, " +
                "skippingPrevious=$skippingPrevious" +
                ")"
    }
}
