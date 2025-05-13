package org.vander.spotifyclient.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyErrorResponseDto(
    @SerialName("error") val error: SpotifyErrorDetailDto
)

@Serializable
data class SpotifyErrorDetailDto(
    @SerialName("status") val status: Int,
    @SerialName("message") val message: String
)