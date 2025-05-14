package org.vander.spotifyclient.data.repository

import org.vander.spotifyclient.data.playlist.mapper.toDomain
import org.vander.spotifyclient.data.remote.mapper.toDomain
import org.vander.spotifyclient.domain.data.CurrentlyPlayingAndQueue
import org.vander.spotifyclient.domain.data.SpotifyPlaylistsResponse
import org.vander.spotifyclient.domain.datasource.ISpotifyRemoteDataSource
import org.vander.spotifyclient.domain.playlist.repository.ISpotifyRepository
import javax.inject.Inject

class SpotifyRepository @Inject constructor(
    private val remoteDataSource: ISpotifyRemoteDataSource
) : ISpotifyRepository {

    override suspend fun getUserQueue(): Result<CurrentlyPlayingAndQueue> {
        return remoteDataSource.fetchUserQueue().map { it.toDomain() }
    }

    override suspend fun getUserPlaylists(): Result<SpotifyPlaylistsResponse> {
        return remoteDataSource.fetchUserPlaylists().map { it.toDomain() }
    }


}