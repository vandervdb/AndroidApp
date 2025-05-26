package org.vander.spotifyclient.domain.datasource

import org.vander.spotifyclient.data.remote.dto.CurrentlyPlayingWithQueueDto


interface IRemoteQueueDataSource {
    suspend fun fetchUserQueue(): Result<CurrentlyPlayingWithQueueDto>
}
