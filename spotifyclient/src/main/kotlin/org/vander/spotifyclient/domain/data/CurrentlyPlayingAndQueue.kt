package org.vander.spotifyclient.domain.data

data class CurrentlyPlayingAndQueue(
    val currentlyPlaying: SpotifyTrack,
    val queue: SpotifyQueue
)
