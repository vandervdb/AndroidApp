package org.vander.spotifyclient.data.client.client

import android.graphics.Bitmap
import com.spotify.android.appremote.api.SpotifyAppRemote

interface ISpotifyClient {
    fun setOnTrackImageReceived(callback: (Bitmap) -> Unit)
    fun connect()
    fun registerAppRemotePlayerState(remote: SpotifyAppRemote)
    fun play(url: String)
    fun play()
    fun pause()
    fun skipToNext()
    fun skipToPrevious()
    fun seek(position: Int)
    fun setVolume(volume: Int)
    fun setShuffle(shuffle: Boolean)
    fun setRepeat(repeat: Boolean)
    fun disconnect()
}