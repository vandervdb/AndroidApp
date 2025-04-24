package org.vander.spotifyclient.data.client.client

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import org.vander.spotify.data.utils.CLIENT_ID
import org.vander.spotify.data.utils.REDIRECT_URI

class SpotifyClient(private val context: Context?) : ISpotifyClient {
    companion object {
        private const val TAG = "SpotifyClient"
    }

    private val connectionParams: ConnectionParams = ConnectionParams.Builder(CLIENT_ID)
        .setRedirectUri(REDIRECT_URI)
        .showAuthView(true)
        .build()
    private var onTrackImageReceived: ((Bitmap) -> Unit)? = null
    private var spotifyAppRemote: SpotifyAppRemote? = null

    /**
     * Sets a callback function to be invoked when a new track image is received.
     *
     * This function allows you to register a listener that will be notified whenever
     * a new image associated with a track becomes available. The provided callback
     * function will receive the newly received image as a Bitmap.
     *
     * @param callback The callback function to be invoked when a new track image is received.
     *                 This function takes a single parameter:
     *                 - `Bitmap`: The received track image.
     *
     * @see Bitmap
     *
     * Example Usage:
     * ```kotlin
     * myTrackImageReceiver.setOnTrackImageReceived { bitmap ->
     *     // Process the received bitmap here, e.g., display it in an ImageView
     *     myImageView.setImageBitmap(bitmap)
     * }
     * ```
     *
     * Note: Multiple calls to this function will override the previous callback.
     */
    override fun setOnTrackImageReceived(callback: (Bitmap) -> Unit) {
        Log.d(TAG, "setOnTrackImageReceived: $callback")
        onTrackImageReceived = callback
    }

    /**
     * Connects to the Spotify App Remote SDK.
     *
     * This function initiates a connection to the Spotify application on the device using the
     * Spotify App Remote SDK. It sets up a connection listener to handle successful connection and
     * connection failures.
     *
     * Upon successful connection:
     *   - The `spotifyAppRemote` instance is initialized.
     *   - The `registerAppRemotePlayerState` function is called to start listening for player state changes.
     *   - The `connected` flag is set to true.
     *   - A debug log message is printed indicating a successful connection.
     *
     * Upon connection failure:
     *   - An error log message is printed with details about the failure.
     *   - The `connected` flag is set to false.
     *
     * @param context The Android context used for connecting to the Spotify app.
     * @throws Throwable if an error occurred during the attempt to connect to the app. Details of the exception are logged.
     * @see SpotifyAppRemote
     * @see Connector.ConnectionListener
     * @see registerAppRemotePlayerState
     */
    override fun connect() {
        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d(TAG, "Connected! Yay!")
            }

            override fun onFailure(throwable: Throwable) {
                Log.e(TAG, throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }

    override fun registerAppRemotePlayerState(remote: SpotifyAppRemote) {
        remote.playerApi.subscribeToPlayerState().setEventCallback {
            val track: Track = it.track
            Log.d(TAG, track.name + " by " + track.artist.name + "(paused: " + it.isPaused + ")")
            remote.imagesApi.getImage(track.imageUri)
                .setResultCallback { bitmap ->
                    Log.d(
                        TAG,
                        "Image récupérée:Url: ${track.imageUri} -  ${bitmap.width} x ${bitmap.height}"
                    )
                    onTrackImageReceived?.invoke(bitmap)
                }
                .setErrorCallback {
                    Log.e(TAG, "Erreur lors de la récupération de l'image: ${it.message}")
                }
        }
    }


    override fun play(url: String) {
        TODO("Not yet implemented")
    }

    override fun play() {
        Log.d(TAG, "play: ")
        spotifyAppRemote?.let {
            // Play a playlist
            val playlistURI = "spotify:playlist:37i9dQZF1E36GtyTu91uSn?si=e1bcb347bbf34ee3"
            it.playerApi.play(playlistURI)
        }
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun skipToNext() {
        TODO("Not yet implemented")
    }

    override fun skipToPrevious() {
        TODO("Not yet implemented")
    }

    override fun seek(position: Int) {
        TODO("Not yet implemented")
    }

    override fun setVolume(volume: Int) {
        TODO("Not yet implemented")
    }

    override fun setShuffle(shuffle: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setRepeat(repeat: Boolean) {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        Log.d(TAG, "disconnect: ")
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }
}

sealed class ClientState {
    object NotConnected : ClientState()
    object Connecting : ClientState()
    object Connected : ClientState()
    object Playing : ClientState()
    data class PlayingTrack(val track: Track) : ClientState()
    object Paused : ClientState()
    object Stopped : ClientState()
    object Shuffling : ClientState()
    object Repeating : ClientState()
    object Seeking : ClientState()
    object SkippingNext : ClientState()
    object SkippingPrevious : ClientState()
}