package org.vander.spotifyclient.data.client.remote

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.vander.spotify.data.utils.CLIENT_ID
import org.vander.spotify.data.utils.REDIRECT_URI
import org.vander.spotifyclient.domain.state.SpotifyRemoteClientState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyAppRemoteProvider @Inject constructor() : ISpotifyAppRemoteProvider {
    companion object {
        private const val TAG = "SpotifyAppRemoteProvider"
    }

    private var _remoteState =
        MutableStateFlow<SpotifyRemoteClientState>(SpotifyRemoteClientState.NotConnected)
    override val remoteState: StateFlow<SpotifyRemoteClientState> = _remoteState.asStateFlow()


    private var spotifyAppRemote: SpotifyAppRemote? = null

    override suspend fun connect(context: Context): Result<Unit> {
        _remoteState.value = SpotifyRemoteClientState.Connecting

        return suspendCancellableCoroutine { continuation ->
            val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(false)
                .build()

            SpotifyAppRemote.connect(
                context,
                connectionParams,
                object : Connector.ConnectionListener {
                    override fun onConnected(remote: SpotifyAppRemote) {
                        Log.d(TAG, "Connected to remote")
                        spotifyAppRemote = remote
                        _remoteState.value = SpotifyRemoteClientState.Connected
                        continuation.resumeWith(Result.success(Result.success(Unit)))
                    }

                    override fun onFailure(error: Throwable) {
                        _remoteState.value = SpotifyRemoteClientState.Failed(error as Exception)
                        Log.e(TAG, "SpotifyAppRemote connection failed", error)
                        continuation.resumeWith(Result.success(Result.failure(error)))
                    }
                })
        }
    }

    override fun get(): SpotifyAppRemote? = spotifyAppRemote

    override fun disconnect() {
        spotifyAppRemote?.let { SpotifyAppRemote.disconnect(it) }
        spotifyAppRemote = null
        _remoteState.value = SpotifyRemoteClientState.NotConnected
    }

}