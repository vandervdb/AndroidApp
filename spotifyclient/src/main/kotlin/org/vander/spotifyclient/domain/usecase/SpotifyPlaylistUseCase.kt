package org.vander.spotifyclient.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import org.vander.spotifyclient.domain.auth.ITokenProvider
import org.vander.spotifyclient.domain.data.CurrentlyPlayingAndQueue
import org.vander.spotifyclient.domain.data.SpotifyPlaylistsResponse
import org.vander.spotifyclient.domain.data.SpotifyQueue
import org.vander.spotifyclient.domain.playlist.repository.ISpotifyRepository
import javax.inject.Inject

class SpotifyPlaylistUseCase @Inject constructor(
    private val playlistRepository: ISpotifyRepository,
    private val tokenProvider: ITokenProvider
) {

    companion object {
        private const val TAG = "SpotifyPlaylistUseCase"
    }


    fun observeUserPlaylistsWhenTokenAvailable(): Flow<Result<SpotifyPlaylistsResponse>> {
        Log.d(TAG, "observeUserPlaylistsWhenTokenAvailable")
        return tokenProvider.tokenFlow
            .filterNotNull()
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .flatMapLatest {
                Log.d(TAG, "observeUser Playlists TokenAvailable !")
                kotlinx.coroutines.flow.flow {
                    emit(playlistRepository.getUserPlaylists())
                }
            }
    }

    fun observeUserQueueWhenTokenAvailable(): Flow<SpotifyQueue> {
        Log.d(TAG, "observeUserPlaylistsWhenTokenAvailable")
        return tokenProvider.tokenFlow
            .filterNotNull()
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .flatMapLatest {
                Log.d(TAG, "observeUser Playlists TokenAvailable !")
                kotlinx.coroutines.flow.flow {
                    parseCurrentUserQueueResponse(playlistRepository.getUserQueue())?.let {
                        emit(it)
                    }
                }
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