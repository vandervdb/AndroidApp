package org.vander.spotifyclient.domain.playlist.repository

import org.vander.spotifyclient.domain.data.CurrentlyPlayingAndQueue
import org.vander.spotifyclient.domain.data.SpotifyPlaylistsResponse

interface ISpotifyRepository {
    suspend fun getUserQueue(): Result<CurrentlyPlayingAndQueue>
    suspend fun getUserPlaylists(): Result<SpotifyPlaylistsResponse>
    suspend fun isTrackSaved(trackId: String): Result<Boolean>
    suspend fun saveTrack(trackId: String): Result<Unit>
    suspend fun removeTrack(trackId: String): Result<Unit>

}