package org.vander.spotifyclient.domain.state

data class SpotifyPlayerState(
    val base: PlayerStateData,
    val isTrackSaved: Boolean? = null,
) {
    companion object {
        fun empty(): SpotifyPlayerState {
            return SpotifyPlayerState(PlayerStateData.empty(), null)
        }
    }
}