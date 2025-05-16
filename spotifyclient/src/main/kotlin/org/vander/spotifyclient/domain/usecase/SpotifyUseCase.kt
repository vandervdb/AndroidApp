package org.vander.spotifyclient.domain.usecase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.vander.spotifyclient.domain.data.SpotifyPlaylistsResponse
import org.vander.spotifyclient.domain.data.SpotifyQueue
import org.vander.spotifyclient.domain.player.ISpotifyPlayerClient
import org.vander.spotifyclient.domain.player.repository.IPlayerStateRepository
import org.vander.spotifyclient.domain.playlist.repository.ISpotifyRepository
import org.vander.spotifyclient.domain.state.PlayerStateData
import org.vander.spotifyclient.domain.state.SpotifyPlayerState
import org.vander.spotifyclient.domain.state.SpotifySessionState
import org.vander.spotifyclient.domain.state.copyWithBase
import org.vander.spotifyclient.domain.state.copyWithSaved
import org.vander.spotifyclient.domain.state.setTrackSaved
import org.vander.spotifyclient.domain.state.togglePause
import javax.inject.Inject

class SpotifyUseCase @Inject constructor(
    val sessionUseCase: SpotifySessionUseCase,
    val remoteUseCase: SpotifyRemoteUseCase,
    val playerStateRepository: IPlayerStateRepository,
    val spotifyRepository: ISpotifyRepository,
    val playerRepository: IPlayerStateRepository,
    val playerClient: ISpotifyPlayerClient,
) {

    companion object {
        private const val TAG = "SpotifyUseCase"
    }

    private var activity: Activity? = null

    val sessionState: StateFlow<SpotifySessionState> = sessionUseCase.sessionState

    val _spotifyPlayerState = MutableStateFlow<SpotifyPlayerState>(SpotifyPlayerState.empty())
    val spotifyPlayerState: StateFlow<SpotifyPlayerState> = _spotifyPlayerState.asStateFlow()

    private val _playlists = MutableStateFlow<Result<SpotifyPlaylistsResponse>?>(null)
    val playlists: StateFlow<Result<SpotifyPlaylistsResponse>?> = _playlists.asStateFlow()

    private val _queue = MutableStateFlow<SpotifyQueue?>(null)
    val currentUserQueue: StateFlow<SpotifyQueue?> = _queue.asStateFlow()

    suspend fun startUp(launchAuth: ActivityResultLauncher<Intent>, activity: Activity) {
        Log.d(TAG, "Starting up...")
        sessionUseCase.requestAuthorization(launchAuth)
        sessionUseCase.launchAuthorizationFlow(activity)
        this.activity = activity
        coroutineScope {
            launch { observePlayerStateData() }
            launch { collectSessionState() }
        }
    }

    fun handleAuthResult(context: Context, result: ActivityResult, viewModelScope: CoroutineScope) {
        sessionUseCase.handleAuthResult(context, result, viewModelScope)
    }

    suspend fun shutDown() {
        sessionUseCase.shutDown()
    }

    suspend fun togglePlayPause() {
        if (playerClient.isPlaying()) {
            playerClient.pause()
        } else {
            playerClient.resume()
        }
        _spotifyPlayerState.togglePause()
    }

    fun toggleSaveTrackState(trackId: String) {
        val newSaveState = _spotifyPlayerState.value.isTrackSaved == false
        _spotifyPlayerState.setTrackSaved(newSaveState)
    }

    private suspend fun collectSessionState() {
        Log.d(TAG, "Collecting session state...")
        sessionUseCase.sessionState.collect { sessionState ->
            Log.d(TAG, "Received session state: $sessionState")
            when (sessionState) {
                is SpotifySessionState.Ready -> {
                    Log.d(TAG, "Session state: Ready")
                    playerRepository.startListening()
                }

                else -> {
                    Log.d(TAG, "Session state: $sessionState")
                }
            }
        }
    }

    private suspend fun observePlayerStateData() {
        Log.d(TAG, "Observing player state data...")
        playerStateRepository.playerStateData.collect { playerStateData ->
            Log.d(TAG, "Received player state data: $playerStateData")
            if (playerStateData != PlayerStateData.empty()) {
                val stateIsTrackSaved = _spotifyPlayerState.value.isTrackSaved
                val isSaved = if (stateIsTrackSaved == null) {
                    spotifyRepository.isTrackSaved(playerStateData.trackId).getOrDefault(false)
                } else {
                    Log.d(TAG, "State is track saved did not change: $stateIsTrackSaved")
                    stateIsTrackSaved!!
                }
                val currentPlayerState = _spotifyPlayerState.value
                _spotifyPlayerState.value = currentPlayerState
                    .copyWithSaved(isSaved)
                    .copyWithBase(playerStateData)
            }
        }
    }


}