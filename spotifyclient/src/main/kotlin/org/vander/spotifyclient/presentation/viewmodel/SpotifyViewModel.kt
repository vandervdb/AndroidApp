package org.vander.spotifyclient.presentation.viewmodel

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.vander.spotifyclient.data.client.auth.ConnectionState
import org.vander.spotifyclient.data.client.auth.ConnectionState.Disconnected
import org.vander.spotifyclient.data.client.auth.ISpotifyAuthClient
import javax.inject.Inject

@HiltViewModel
class SpotifyViewModel @Inject constructor(
    private var spotifyClient: ISpotifyAuthClient
) : ViewModel() {

    private val _connectionState = MutableStateFlow<ConnectionState>(Disconnected("Not connected"))
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()
    private val _trackImage = MutableStateFlow<Bitmap?>(null)
    val trackImage: StateFlow<Bitmap?> = _trackImage.asStateFlow()

    fun initSpotifyClient() {
        viewModelScope.launch {
            Log.d("SpotifyViewModel", "Initializing Spotify client...")
            spotifyClient.setOnTrackImageReceived(::displayCover)
        }
    }

    fun connectToSpotify(activity: Activity, launcher: ActivityResultLauncher<Intent>) {
        viewModelScope.launch {
            Log.d("SpotifyViewModel", "Connecting to Spotify...")
            // update the UI with the connection status
            try {
                spotifyClient.authorize(activity, launcher)
                spotifyClient.connect()
            } catch (e: Exception) {
                Log.e("SpotifyViewModel", "Error connecting to Spotify", e)
            }
        }
    }

    fun play() {
        viewModelScope.launch {
            spotifyClient.play()
        }
    }

    fun setStateFromConnectionResult(result: ActivityResult) {
        _connectionState.value =
            spotifyClient.handleSpotifyAuthResult(result)
    }

    private fun displayCover(image: Bitmap) {
        Log.d("SpotifyViewModel", "Displaying cover...")
        _trackImage.value = image
    }

    override fun onCleared() {
        super.onCleared()
        spotifyClient.disconnect()
    }
}

data class SpotifyUiState(
    val isConnected: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val accessToken: String? = null,
    val albumCover: Bitmap? = null
)

