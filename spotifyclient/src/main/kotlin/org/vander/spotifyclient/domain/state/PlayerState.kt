package org.vander.spotifyclient.domain.state

sealed class PlayerState {
    object NotConnected : PlayerState()
    object Connecting : PlayerState()
    object Connected : PlayerState()
}