package org.vander.spotifyclient.data.model

sealed class SpotifySessionState {
    object Idle : SpotifySessionState()
    object Authorizing : SpotifySessionState()
    object ConnectingRemote : SpotifySessionState()
    object Ready : SpotifySessionState()
    object IsPaused : SpotifySessionState()
    data class Failed(val exception: Throwable) : SpotifySessionState()
}