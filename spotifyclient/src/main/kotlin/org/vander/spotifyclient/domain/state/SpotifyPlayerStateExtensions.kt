package org.vander.spotifyclient.domain.state

val SpotifyPlayerState.trackName get() = base.trackName
val SpotifyPlayerState.artistName get() = base.artistName
val SpotifyPlayerState.coverId get() = base.coverId
val SpotifyPlayerState.trackId get() = base.trackId

val SpotifyPlayerState.isPaused get() = base.isPaused
val SpotifyPlayerState.playing get() = base.playing
val SpotifyPlayerState.paused get() = base.paused
val SpotifyPlayerState.stopped get() = base.stopped
val SpotifyPlayerState.shuffling get() = base.shuffling
val SpotifyPlayerState.repeating get() = base.repeating
val SpotifyPlayerState.seeking get() = base.seeking
val SpotifyPlayerState.skippingNext get() = base.skippingNext
val SpotifyPlayerState.skippingPrevious get() = base.skippingPrevious

fun SpotifyPlayerState.copyWithSaved(isSaved: Boolean): SpotifyPlayerState {
    return this.copy(isTrackSaved = isSaved)
}

fun SpotifyPlayerState.copyWithBase(base: PlayerStateData): SpotifyPlayerState {
    return this.copy(base = base)
}
