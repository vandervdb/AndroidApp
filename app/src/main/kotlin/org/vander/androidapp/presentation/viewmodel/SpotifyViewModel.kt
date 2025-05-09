package org.vander.androidapp.presentation.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.vander.spotifyclient.domain.player.repository.IPlayerStateRepository
import org.vander.spotifyclient.domain.state.PlayerStateData
import org.vander.spotifyclient.domain.state.SpotifySessionState
import org.vander.spotifyclient.domain.usecase.SpotifyPlayerRemoteUseCase
import org.vander.spotifyclient.domain.usecase.SpotifySessionUseCase
import javax.inject.Inject

@HiltViewModel
class SpotifyViewModel @Inject constructor(
    private val sessionUseCase: SpotifySessionUseCase,
    private val playerUseCase: SpotifyPlayerRemoteUseCase,
    private val playerRepository: IPlayerStateRepository
) : ViewModel() {

    val sessionState: StateFlow<SpotifySessionState> = sessionUseCase.sessionState
    val playerStateData: StateFlow<PlayerStateData> = playerRepository.playerStateData


    fun requestAuthorization(launcher: ActivityResultLauncher<Intent>) {
        sessionUseCase.requestAuthorization(launcher)
    }

    fun launchAuthorizationFlow(activity: Activity) {
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

    fun togglePlayPause() {
        viewModelScope.launch {
            playerUseCase.togglePlayPause()
        }
    }

    fun disconnectSpotify() {
        sessionUseCase.disconnect()
    }


    fun isPlaying(): Boolean {
        return true
    }


    override fun onCleared() {
        super.onCleared()
        disconnectSpotify()
    }

}