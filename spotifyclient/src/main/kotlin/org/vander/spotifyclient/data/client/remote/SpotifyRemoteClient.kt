package org.vander.spotifyclient.data.client.remote

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import kotlinx.coroutines.suspendCancellableCoroutine
import org.vander.spotify.data.utils.CLIENT_ID
import org.vander.spotify.data.utils.REDIRECT_URI
import javax.inject.Inject

class SpotifyRemoteClient @Inject constructor(private val context: Context?) : ISpotifyRemoteClient {
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

    override suspend fun connect(context: Context): Result<Unit> {
        return suspendCancellableCoroutine { continuation ->
            val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(false)
                .build()

            SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
                override fun onConnected(spotifyRemote: SpotifyAppRemote) {
                    spotifyAppRemote = spotifyRemote
                    continuation.resumeWith(Result.success(Result.success(Unit)))
                }

                override fun onFailure(error: Throwable) {
                    continuation.resumeWith(Result.success(Result.failure(error)))
                }
            })
        }
    }

    override fun registerAppRemotePlayerState(remote: SpotifyAppRemote) {
        remote.playerApi.subscribeToPlayerState().setEventCallback {
            val track: Track = it.track
            Log.d(TAG, track.name + " by " + track.artist.name + "(paused: " + it.isPaused + ")")
//            remote.imagesApi.getImage(track.imageUri)
//                .setResultCallback { bitmap ->
//                    Log.d(
//                        TAG,
//                        "Image récupérée:Url: ${track.imageUri} -  ${bitmap.width} x ${bitmap.height}"
//                    )
//                    onTrackImageReceived?.invoke(bitmap)
//                }
//                .setErrorCallback {
//                    Log.e(TAG, "Erreur lors de la récupération de l'image: ${it.message}")
//                }
        }
    }

    override fun getRemote(): SpotifyAppRemote? = spotifyAppRemote

    override fun disconnect() {
        Log.d(TAG, "disconnect: ")
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }


}

