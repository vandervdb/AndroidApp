package org.vander.spotifyclient.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.vander.spotifyclient.data.remote.mapper.toDomain
import org.vander.spotifyclient.domain.data.CurrentlyPlayingAndQueue
import org.vander.spotifyclient.domain.datasource.IRemoteQueueDataSource
import org.vander.spotifyclient.domain.repository.SpotifyQueueRepository
import javax.inject.Inject

class SpotifyQueueRepositoryImpl @Inject constructor(
    private val api: IRemoteQueueDataSource
) : SpotifyQueueRepository {

    private val _currentQueue = MutableStateFlow<CurrentlyPlayingAndQueue?>(null)
    override val currentQueue: StateFlow<CurrentlyPlayingAndQueue?> = _currentQueue

    override suspend fun getUserQueue(): Result<CurrentlyPlayingAndQueue> {
        return try {
            val dto = api.fetchUserQueue().getOrThrow()
            val result = dto.toDomain()
            _currentQueue.update { result }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}