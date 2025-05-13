package org.vander.androidapp.presentation.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.vander.coreui.IMiniPlayerViewModel
import org.vander.spotifyclient.domain.data.CurrentlyPlayingAndQueue
import org.vander.spotifyclient.domain.data.SpotifyPlaylistsResponse
import org.vander.spotifyclient.domain.player.repository.IPlayerStateRepository
import org.vander.spotifyclient.domain.state.PlayerStateData
import org.vander.spotifyclient.domain.state.SpotifySessionState
import org.vander.spotifyclient.domain.usecase.SpotifyPlayerRemoteUseCase
import org.vander.spotifyclient.domain.usecase.SpotifyPlaylistUseCase
import org.vander.spotifyclient.domain.usecase.SpotifySessionUseCase
import javax.inject.Inject

@HiltViewModel
open class SpotifyViewModel @Inject constructor(
    private val sessionUseCase: SpotifySessionUseCase,
    private val playerUseCase: SpotifyPlayerRemoteUseCase,
    private val playlistUseCase: SpotifyPlaylistUseCase,
    private val playerRepository: IPlayerStateRepository
) : ViewModel(), IMiniPlayerViewModel {

    override val sessionState: StateFlow<SpotifySessionState> = sessionUseCase.sessionState
    override val playerStateData: StateFlow<PlayerStateData> = playerRepository.playerStateData

    private val _playlists = MutableStateFlow<Result<SpotifyPlaylistsResponse>?>(null)
    val playlists: StateFlow<Result<SpotifyPlaylistsResponse>?> = _playlists

    private val _queue = MutableStateFlow<Result<CurrentlyPlayingAndQueue>?>(null)
    val queue: StateFlow<Result<CurrentlyPlayingAndQueue>?> = _queue

    companion object {
        private const val TAG = "SpotifyViewModel"
    }

    init {
        observeUserPlaylists()
        observeUserQueue()
    }

    override fun requestAuthorization(launcher: ActivityResultLauncher<Intent>) {
        sessionUseCase.requestAuthorization(launcher)
    }

    override fun launchAuthorizationFlow(activity: Activity) {
        playlistUseCase.observeUserPlaylistsWhenTokenAvailable()
        sessionUseCase.launchAuthorizationFlow(activity)
    }


    fun handleAuthResult(context: Context, result: ActivityResult) {
        sessionUseCase.handleAuthResult(context, result, viewModelScope)
    }


    fun playTrack(trackUri: String) {
        viewModelScope.launch {
            playerUseCase.play(trackUri)
        }
    }

    fun pauseTrack() {
        viewModelScope.launch {
            playerUseCase.pause()
        }
    }

    override fun togglePlayPause() {
        viewModelScope.launch {
            playerUseCase.togglePlayPause()
        }
    }

    override fun disconnectSpotify() {
        viewModelScope.launch {
            sessionUseCase.disconnect()
        }
    }


    override fun isPlaying(): Boolean {
        return true
    }


    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
        disconnectSpotify()
    }

    private fun observeUserPlaylists() {
        viewModelScope.launch {
            playlistUseCase.observeUserPlaylistsWhenTokenAvailable()
                .collect { result ->
                    Log.d(TAG, "Received playlists: $result")
                    _playlists.value = result
                }
        }
    }

    private fun observeUserQueue() {
        viewModelScope.launch {
            playlistUseCase.observeUserQueueWhenTokenAvailable()
                .collect { result ->
                    Log.d(TAG, "Received queue: $result")
                    _queue.value = result
                }
        }
    }

}