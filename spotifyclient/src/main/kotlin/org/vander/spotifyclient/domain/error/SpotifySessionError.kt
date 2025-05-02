package org.vander.spotifyclient.domain.error

sealed class SpotifySessionError(message: String? = null, cause: Throwable?) : Exception(message) {
    data class AuthFailed(override val cause: Throwable? = null) :
        SpotifySessionError("Spotify Authorization Failed", cause)

    data class RemoteConnectionFailed(override val cause: Throwable? = null) :
        SpotifySessionError("Spotify Remote Connection Failed", cause)

    data class UnknownError(override val cause: Throwable? = null) :
        SpotifySessionError("Unknown error occurred", cause)
}