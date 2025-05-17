package org.vander.spotifyclient.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import org.vander.spotifyclient.domain.auth.ITokenProvider
import org.vander.spotifyclient.domain.data.CurrentlyPlayingAndQueue
import org.vander.spotifyclient.domain.data.SpotifyQueue
import org.vander.spotifyclient.domain.playlist.repository.ISpotifyRepository
import org.vander.spotifyclient.domain.state.PlayerStateData
import javax.inject.Inject

class SpotifyRemoteUseCase @Inject constructor(
    private val playlistRepository: ISpotifyRepository,
) {

    companion object {
        private const val TAG = "SpotifyRemoteUseCase"
    }

    private val _currentUserQueue = MutableStateFlow<SpotifyQueue?>(null)
    val currentUserQueue: StateFlow<SpotifyQueue?> = _currentUserQueue.asStateFlow()

    suspend fun getAndEmitUserQueueFlow() {
            parseCurrentUserQueueResponse(playlistRepository.getUserQueue())?.let {
                _currentUserQueue.value = it
        }
    }

    private fun parseCurrentUserQueueResponse(result: Result<CurrentlyPlayingAndQueue>): SpotifyQueue? {
        return ((if (result.isSuccess) {
            val queue = result.getOrNull()
            queue?.queue
        } else {
            null
        }) as SpotifyQueue?)
    }

}