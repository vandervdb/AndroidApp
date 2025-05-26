package org.vander.spotifyclient.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.vander.spotifyclient.domain.data.CurrentlyPlayingAndQueue

interface SpotifyQueueRepository {
    val currentQueue: StateFlow<CurrentlyPlayingAndQueue?>
    suspend fun getUserQueue(): Result<CurrentlyPlayingAndQueue>
}