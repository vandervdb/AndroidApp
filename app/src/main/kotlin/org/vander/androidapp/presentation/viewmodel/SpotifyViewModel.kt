package org.vander.androidapp.presentation.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.vander.spotifyclient.domain.state.PlayerStateData
import org.vander.spotifyclient.domain.state.SpotifySessionState
import org.vander.spotifyclient.domain.usecase.SpotifySessionUseCase
import javax.inject.Inject

@HiltViewModel
open class SpotifyViewModel @Inject constructor(
    private val sessionUseCase: SpotifySessionUseCase
) : ViewModel() {

    open val sessionState: StateFlow<SpotifySessionState> = sessionUseCase.sessionState

    private val _currentTrackName = MutableStateFlow<String?>(null)
    val currentTrackName: StateFlow<String?> = _currentTrackName.asStateFlow()

    private val _currentArtistName = MutableStateFlow<String?>(null)
    val currentArtistName: StateFlow<String?> = _currentArtistName.asStateFlow()

    private val _currentTrackImage = MutableStateFlow<Bitmap?>(null)
    val currentTrackImage: StateFlow<Bitmap?> = _currentTrackImage.asStateFlow()

    open fun requestAuthorization(launcher: ActivityResultLauncher<Intent>) {
        sessionUseCase.requestAuthorization(launcher)
    }

    open fun launchAuthorizationFlow(activity: Activity) {
        sessionUseCase.launchAuthorizationFlow(activity)
    }

    open fun handleAuthResult(context: Context, result: ActivityResult) {
        sessionUseCase.handleAuthResult(context, result, viewModelScope)
    }

    open suspend fun playTrack(trackUri: String) {
        sessionUseCase.play(trackUri)
    }

    open suspend fun pauseTrack() {
        sessionUseCase.pause()
    }

    open suspend fun subscribeToPlayerState(onUpdate: (PlayerStateData) -> Unit) {
        sessionUseCase.subscribeToPlayerState(onUpdate)
    }

    open fun togglePlayPause() {
        viewModelScope.launch {
            sessionUseCase.togglePlayPause()
        }
    }

    open fun disconnectSpotify() {
        sessionUseCase.disconnect()
    }

    open fun isConnected(): Boolean {
        return sessionState.value is SpotifySessionState.Ready
    }

    open fun isPlaying(): Boolean {
//        return (sessionState.value as? SpotifySessionState.IsPaused)?.isPaused == false
        return true
    }


    override fun onCleared() {
        super.onCleared()
        disconnectSpotify()
    }
}