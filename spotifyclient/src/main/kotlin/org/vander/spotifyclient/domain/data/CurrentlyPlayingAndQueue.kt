package org.vander.spotifyclient.domain.data

data class CurrentlyPlayingAndQueue(
    val currentlyPlaying: SpotifyTrack? = null,
    val queue: SpotifyQueue
)
