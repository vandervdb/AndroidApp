package org.vander.spotifyclient.data.local.mapper

import org.vander.spotifyclient.domain.state.PlayerStateData
import org.vander.spotifyclient.domain.state.SpotifyPlayerState

fun PlayerStateData.toAppPlayerState(isSaved: Boolean): SpotifyPlayerState {
    return SpotifyPlayerState(this, isSaved)
}