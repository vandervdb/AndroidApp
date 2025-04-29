package org.vander.spotifyclient.data.client.remote

import android.content.Context
import android.graphics.Bitmap
import com.spotify.android.appremote.api.SpotifyAppRemote

interface ISpotifyRemoteClient {
    suspend fun connect(context: Context): Result<Unit>
    fun registerAppRemotePlayerState(remote: SpotifyAppRemote)
    fun getRemote(): SpotifyAppRemote?
    fun disconnect()
}