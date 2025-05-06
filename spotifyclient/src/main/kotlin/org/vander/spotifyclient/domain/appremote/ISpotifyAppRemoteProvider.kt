package org.vander.spotifyclient.domain.appremote

import android.content.Context
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.flow.StateFlow
import org.vander.spotifyclient.domain.state.SpotifyRemoteClientState

interface ISpotifyAppRemoteProvider {
    val remoteState: StateFlow<SpotifyRemoteClientState>
    suspend fun connect(context: Context): Result<Unit>
    fun get(): SpotifyAppRemote?
    fun disconnect()
}