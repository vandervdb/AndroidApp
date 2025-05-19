package org.vander.spotifyclient.data.appremote

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import org.vander.spotifyclient.BuildConfig
import org.vander.spotifyclient.domain.appremote.ISpotifyAppRemoteProvider
import org.vander.spotifyclient.domain.state.SpotifyRemoteClientState
import org.vander.spotifyclient.utils.REDIRECT_URI
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
        _remoteState.update { SpotifyRemoteClientState.Connecting }

        return suspendCancellableCoroutine { continuation ->
            val connectionParams = ConnectionParams.Builder(BuildConfig.CLIENT_ID)
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
                        _remoteState.update { SpotifyRemoteClientState.Connected }
                        continuation.resumeWith(Result.success(Result.success(Unit)))
                    }

                    override fun onFailure(error: Throwable) {
                        _remoteState.update { SpotifyRemoteClientState.Failed(error as Exception) }
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
        _remoteState.update { SpotifyRemoteClientState.NotConnected }
    }

}