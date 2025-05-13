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
import org.vander.spotifyclient.domain.playlist.repository.IPlaylistRepository
import javax.inject.Inject

class SpotifyPlaylistUseCase @Inject constructor(
    private val playlistRepository: IPlaylistRepository,
    private val tokenProvider: ITokenProvider
) {

    companion object {
        private const val TAG = "SpotifyPlaylistUseCase"
    }


//    suspend fun getPlaylist(id: String) = playlistRemoteDataSource.fetchPlaylist(id)

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

    fun observeUserQueueWhenTokenAvailable(): Flow<Result<CurrentlyPlayingAndQueue>> {
        Log.d(TAG, "observeUserPlaylistsWhenTokenAvailable")
        return tokenProvider.tokenFlow
            .filterNotNull()
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .flatMapLatest {
                Log.d(TAG, "observeUser Playlists TokenAvailable !")
                kotlinx.coroutines.flow.flow {
                    emit(playlistRepository.getUserQueue())
                }
            }
    }

}