package org.vander.spotifyclient.data.repository

import org.vander.spotifyclient.data.playlist.mapper.toDomain
import org.vander.spotifyclient.data.remote.mapper.toDomain
import org.vander.spotifyclient.domain.data.CurrentlyPlayingAndQueue
import org.vander.spotifyclient.domain.data.SpotifyPlaylistsResponse
import org.vander.spotifyclient.domain.datasource.IPlaylistRemoteDataSource
import org.vander.spotifyclient.domain.playlist.repository.IPlaylistRepository
import javax.inject.Inject

class PlaylistRepository @Inject constructor(
    private val remoteDataSource: IPlaylistRemoteDataSource
) : IPlaylistRepository {

    override suspend fun getUserQueue(): Result<CurrentlyPlayingAndQueue> {
        return remoteDataSource.fetchUserQueue().map { it.toDomain() }
    }

    override suspend fun getUserPlaylists(): Result<SpotifyPlaylistsResponse> {
        return remoteDataSource.fetchUserPlaylists().map { it.toDomain() }
    }


}