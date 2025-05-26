package org.vander.spotifyclient.domain.repository

import org.vander.spotifyclient.domain.player.repository.IPlayerStateRepository
import org.vander.spotifyclient.domain.usecase.SpotifyRemoteUseCase
import javax.inject.Inject

class UIQueueRepository @Inject constructor(
    val remoteUseCase: SpotifyRemoteUseCase,
    val playerStateRepository: IPlayerStateRepository,
) {

}