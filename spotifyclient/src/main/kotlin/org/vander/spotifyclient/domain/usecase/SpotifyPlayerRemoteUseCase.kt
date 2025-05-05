package org.vander.spotifyclient.domain.usecase

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.vander.spotifyclient.data.client.player.domain.IPlayerStateRepository
import org.vander.spotifyclient.data.client.player.domain.ISpotifyPlayerClient
import org.vander.spotifyclient.data.client.remote.ISpotifyAppRemoteProvider
import org.vander.spotifyclient.domain.state.SpotifyRemoteClientState
import javax.inject.Inject

class SpotifyPlayerRemoteUseCase @Inject constructor(
    private val playerClient: ISpotifyPlayerClient,
    private val remoteProvider: ISpotifyAppRemoteProvider,
    private val playerRepository: IPlayerStateRepository
) {

    companion object {
        const val TAG = "SpotifyPlayerRemoteUseCase"
    }

    init {
        collectRemoteClientState()
    }

    private fun collectRemoteClientState() {
        CoroutineScope(Dispatchers.IO).launch {
            remoteProvider.remoteState.collect { state ->
                Log.d(TAG, "Remote state: $state")
                when (state) {
                    is SpotifyRemoteClientState.Connected -> {
                        playerRepository.startListening()
                    }

                    is SpotifyRemoteClientState.Failed -> {
                        // Handle connection failure
                    }

                    else -> {
                        // TODO: Handle other states
                    }
                }
            }
        }
    }


    suspend fun play(trackUri: String) {
        playerClient.play(trackUri)
    }

    suspend fun pause() {
        playerClient.pause()
    }

    suspend fun togglePlayPause() {
        if (playerClient.isPlaying()) {
            pause()
        } else {
            //resume()
        }
    }
}