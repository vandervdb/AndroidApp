package org.vander.spotifyclient.domain.usecase

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.vander.spotifyclient.domain.SpotifySessionManager
import org.vander.spotifyclient.domain.data.CurrentlyPlayingAndQueue
import org.vander.spotifyclient.domain.data.UIQueueItem
import org.vander.spotifyclient.domain.player.ISpotifyPlayerClient
import org.vander.spotifyclient.domain.player.repository.IPlayerStateRepository
import org.vander.spotifyclient.domain.repository.SpotifyLibraryRepository
import org.vander.spotifyclient.domain.state.PlayerStateData
import org.vander.spotifyclient.domain.state.SavedRemotelyChangedState
import org.vander.spotifyclient.domain.state.SpotifyPlayerState
import org.vander.spotifyclient.domain.state.SpotifySessionState
import org.vander.spotifyclient.domain.state.UIQueueState
import org.vander.spotifyclient.domain.state.copyWithBase
import org.vander.spotifyclient.domain.state.copyWithSaved
import org.vander.spotifyclient.domain.state.setTrackSaved
import org.vander.spotifyclient.domain.state.togglePause
import org.vander.spotifyclient.domain.state.update
import javax.inject.Inject

class SpotifyUseCase @Inject constructor(
    val sessionUseCase: SpotifySessionManager,
    val remoteUseCase: SpotifyRemoteUseCase,
    val playerStateRepository: IPlayerStateRepository,
    val libraryRepository: SpotifyLibraryRepository,
    val playerRepository: IPlayerStateRepository,
    val playerClient: ISpotifyPlayerClient,
) {

    companion object {
        private const val TAG = "SpotifyUseCase"
    }

    private var activity: Activity? = null

    private val _spotifyPlayerState =
        MutableStateFlow<SpotifyPlayerState>(SpotifyPlayerState.empty())
    val spotifyPlayerState: StateFlow<SpotifyPlayerState> = _spotifyPlayerState.asStateFlow()

    val currentUserQueue: StateFlow<CurrentlyPlayingAndQueue?> = remoteUseCase.currentUserQueue

    private val _uIQueueState = MutableStateFlow<UIQueueState>(UIQueueState.empty())
    val uIQueueState: StateFlow<UIQueueState> = _uIQueueState.asStateFlow()

    val savedRemotelyChangedState: StateFlow<SavedRemotelyChangedState> =
        playerStateRepository.savedRemotelyChangedState


    private var hasReceivedUpdatedQueue = false


    suspend fun startUp(activity: Activity) =
        coroutineScope {
            Log.d(TAG, "Starting up...")
            this@SpotifyUseCase.activity = activity
            launch { updateSpotifyPlayerStateAndUIQueueState() } // TODO: Group in one call
            launch { collectSessionState() }
            launch { observeSavedRemotelyChangedState() }
        }

    fun handleAuthResult(context: Context, result: ActivityResult, viewModelScope: CoroutineScope) {
        sessionUseCase.handleAuthResult(context, result, viewModelScope)
    }

    suspend fun shutDown() {
        sessionUseCase.shutDown()
    }

    suspend fun togglePlayPause() {
        _spotifyPlayerState.togglePause()
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

    suspend fun skipNext() {
        playerClient.skipNext()
    }

    suspend fun skipPrevious() {
        playerClient.skipPrevious()
    }

    suspend fun playTrack(trackId: String) {
        playerClient.play("spotify:track:$trackId")
    }

    private suspend fun collectSessionState() {
        Log.d(TAG, "Collecting session state...")
        sessionUseCase.sessionState.collect { sessionState ->
            Log.d(TAG, "Received session state: $sessionState")
            when (sessionState) {
                is SpotifySessionState.Ready -> {
                    Log.d(TAG, "Session state: Ready")
                    remoteUseCase.getAndEmitUserQueueFlow()
                    playerRepository.startListening()
                }

                else -> {
                    Log.d(TAG, "Session state: $sessionState")
                }
            }
        }
    }


    private suspend fun observeSavedRemotelyChangedState() {
        Log.d(TAG, "Observing saved remotely changed state...")
        savedRemotelyChangedState.collect { state ->
            val isSaved = state.isSaved
            Log.d(TAG, "Received saved remotely changed state: $isSaved")
            if (isSaved) {
                Log.d(TAG, "Saved remotely changed state: true")
                val trackId = state.trackId
                updateSpotifyPlayerWithIsSavedState(playerStateData = null, trackId)
            }
        }
    }

    private suspend fun updateSpotifyPlayerStateAndUIQueueState() {
        combine(
            currentUserQueue,
            playerStateRepository.playerStateData
        ) { queueData, playerStateData ->
            Pair(queueData, playerStateData)
        }.collect { (queueData, playerStateData) ->
            Log.d(TAG, "Received player state data: $playerStateData")
            Log.d(TAG, "Received queue data: $queueData")
            if (queueData != null && !hasReceivedUpdatedQueue) {
                val playerStateDataItem = UIQueueItem(
                    trackName = playerStateData.trackName,
                    artistName = playerStateData.artistName,
                    trackId = playerStateData.trackId
                )
                val matchesCurrent = queueData.currentlyPlaying?.id == playerStateData.trackId
                if (!matchesCurrent) {
                    Log.d(
                        TAG,
                        "queueData's currentlyPlaying and playerStateData don't share same trackId"
                    )
                    remoteUseCase.getAndEmitUserQueueFlow()
                    return@collect
                }
                Log.d(TAG, "queueData's currentlyPlaying and playerStateData share same trackId")
                Log.d(TAG, "Updating UI queue state...")
                hasReceivedUpdatedQueue = true
                val queueListItems = queueData.queue.tracks.map {
                    UIQueueItem(
                        trackName = it.name,
                        artistName = it.artists[0].name,
                        trackId = it.id
                    )
                }
                val parsedQueAndPlayerStateItems = listOf(playerStateDataItem) + queueListItems
                _uIQueueState.update { UIQueueState(parsedQueAndPlayerStateItems) }
            }

            if (hasReceivedUpdatedQueue && playerStateData != PlayerStateData.empty()) {
                if (!isTrackInQueue(playerStateData.trackId, _uIQueueState.value)) {
                    Log.d(TAG, "Queue is not updated, so we will request it again")
                    hasReceivedUpdatedQueue = false
                    remoteUseCase.getAndEmitUserQueueFlow()
//                    return@collect // TODO: Check behaviour
                }
                updateSpotifyPlayerWithIsSavedState(playerStateData)
            }
        }
    }

    private suspend fun updateSpotifyPlayerWithIsSavedState(
        playerStateData: PlayerStateData? = null,
        trackId: String? = null
    ) {
        Log.d(TAG, "Updating Spotify player state: $playerStateData")
        var currentTrackId = playerStateData?.trackId ?: trackId!!
        val isSaved = libraryRepository.isTrackSaved(currentTrackId).getOrDefault(false)
        val currentPlayerState = _spotifyPlayerState.value
        Log.d(TAG, "(Spotify player state: $currentPlayerState)")
        _spotifyPlayerState.update { currentPlayerState.copyWithSaved(isSaved) }

        if (playerStateData != null) {
            _spotifyPlayerState.update { _spotifyPlayerState.value.copyWithBase(playerStateData) }
        }

    }

    fun isTrackInQueue(trackIdToFind: String, uIQueueState: UIQueueState): Boolean {
        return uIQueueState.items.any { it.trackId == trackIdToFind }
    }


}