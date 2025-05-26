package org.vander.spotifyclient.domain.state

data class SavedRemotelyChangedState(
    var isSaved: Boolean = false,
    val trackId: String = ""
)