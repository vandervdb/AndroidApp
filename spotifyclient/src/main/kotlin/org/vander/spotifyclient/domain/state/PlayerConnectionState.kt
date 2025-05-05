package org.vander.spotifyclient.domain.state

sealed class PlayerConnectionState {
    object NotConnected : PlayerConnectionState()
    object Connecting : PlayerConnectionState()
    object Connected : PlayerConnectionState()
}